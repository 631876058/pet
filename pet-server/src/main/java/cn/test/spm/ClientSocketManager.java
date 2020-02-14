package cn.test.spm;

import cn.test.spm.executors.PetBizExecutor;
import cn.test.spm.request.PetServerRequest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用来保存客户端 Socket连接，并从中读取数据，完整的维护连接的存活心跳还没实现
 *
 * @author 郭豪豪
 * @date 2020-02-13
 **/
public class ClientSocketManager {

    private static volatile boolean shutdown = false;

    private static final CopyOnWriteArrayList<Socket> clientList = new CopyOnWriteArrayList<>();

    private static final Map<SocketChannel, StringBuffer> socketRemainingBuffer = new HashMap<>();

    private static final AtomicInteger CONNECT_NUM = new AtomicInteger();

    private static final AtomicInteger REQUEST_NUM = new AtomicInteger();


    public static void addClient(Socket client) {
        clientList.add(client);
        CONNECT_NUM.incrementAndGet();
        //方式1：最简单办法一个Client Socket，一个线程。但是这样可能导致线程太多了
        new Thread(() -> {
            try {
                InputStream inputStream = client.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String data = null;
                while ((data = br.readLine()) != null) {
                    System.out.println("Client [" + client.getInetAddress() + ":" + client.getPort() + "] data [ " + data + " ] receive finished");
                    PetServerRequest request =
                            new PetServerRequest(data, client);
                    REQUEST_NUM.incrementAndGet();
                    PetBizExecutor.execute(request);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }, "clientT:" + client.getInetAddress() + ":" + client.getPort()).start();
    }

    public static void shutdown() {
        shutdown = true;
    }


}
