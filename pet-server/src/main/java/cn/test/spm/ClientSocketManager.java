package cn.test.spm;

import cn.test.spm.db.PetService;
import cn.test.spm.db.impl.SimplePetServiceImpl;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用来保存客户端 Socket连接，并从中读取数据，完整的维护连接的存活心跳还没实现
 *
 * @author 郭豪豪
 * @date 2020-02-13
 **/
public class ClientSocketManager {

    /**
     * 记录连接数
     */
    private static final AtomicInteger CONNECT_NUM = new AtomicInteger();

    /**
     * 记录请求次数
     */
    private static final AtomicInteger REQUEST_NUM = new AtomicInteger();

    private static PetService petService = new SimplePetServiceImpl();


    public static void addClient(Socket client) {
        CONNECT_NUM.incrementAndGet();
        //方式1：最简单办法一个Client Socket，一个线程。缺点就是这样开线程太多了
        new Thread(() -> {
            try {
                InputStream inputStream = client.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String data = null;
                while ((data = br.readLine()) != null) {
                    System.out.println("Client [" + client.getInetAddress() + ":" + client.getPort() + "] data [ " + data + " ] receive finished");
                    REQUEST_NUM.incrementAndGet();

                    //业务处理
                    String result = null;
                    if (data.indexOf("GET") >= 0) {
                        String[] param = data.split(":");
                        result = petService.getPet(param[1]);
                    }
                    if (data.indexOf("LIST") >= 0) {
                        result = petService.getRankList();
                    }
                    //向客户端返回结果
                    OutputStream outputStream = client.getOutputStream();
                    PrintWriter pw = new PrintWriter(outputStream);
                    pw.println(result);
                    pw.flush();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }, "clientT:" + client.getInetAddress() + ":" + client.getPort()).start();
    }


}
