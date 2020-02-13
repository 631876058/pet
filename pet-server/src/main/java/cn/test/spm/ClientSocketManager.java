package cn.test.spm;

import cn.test.spm.executors.PetBizExecutor;
import cn.test.spm.request.PetServerRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;
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

    private static final Map<Socket,StringBuffer> socketRemainingBuffer = new HashMap<>();

    private static final AtomicInteger CONNECT_NUM = new AtomicInteger();

    private static final AtomicInteger REQUEST_NUM = new AtomicInteger();


    public static void start() {
        System.out.println("ClientSocketManager start...");
        Thread thread = new Thread(() -> {
            while (!shutdown) {
                Iterator<Socket> iterator = clientList.iterator();
                while (iterator.hasNext()) {
                    //此处仅仅会将显式关闭的和流被关闭的给剔除掉，偷偷摸摸掉线的剔除在下面还没写
                    Socket client = iterator.next();

                    if (client.isClosed()) {
                        System.out.println("Client [" + client.getInetAddress() + ":" + client.getPort() + "] in exception will be removed");
                        clientList.remove(client);
                    }
                    try {
                        //当客户端存在数据的时候，提交一个读取数据的任务
                        InputStream inputStream = client.getInputStream();
                        //available：调用后返回可读字节，注意：比如说返回有13个字节，即使你reader.readLine()读取了7个字节，
                        // 下次调用available它也不会把剩余的4给你
                        int available =  inputStream.available();
                        if (available > 0) {

                            InputStreamReader reader = new InputStreamReader(inputStream);
                            char[] buff = new char[2048];
                            int length = reader.read(buff);
                            StringBuffer sb = Optional.ofNullable(socketRemainingBuffer.get(client)).orElse(new StringBuffer());
                            for(int i = 0 ; i < length ; i++){
                                if(buff[i] == '\n' || buff[i] == '\r'){
                                    String data = sb.toString();
                                    sb = new StringBuffer();
                                    if(data == null || data.length() == 0) {
                                        continue;
                                    }
                                    System.out.println("Client [" + client.getInetAddress() + ":" + client.getPort() + "] data [ " + data + " ] receive finished");
                                    PetServerRequest request =
                                            new PetServerRequest(data, client);
                                    REQUEST_NUM.incrementAndGet();
                                    PetBizExecutor.execute(request);
                                }else {
                                    sb.append(buff[i]);
                                }
                            }
                            //将剩余的残缺数据存起来，数据来了的话，接着拼接
                            if(sb.length() > 0){
                                socketRemainingBuffer.put(client,sb);
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        try {
                            //这个client 异常了就关闭移除掉
                            System.out.println("Client [" + client.getInetAddress() + ":" + client.getPort() + "] in exception will be closed and removed");
                            client.close();
                            clientList.remove(client);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

            }

        }, "clientSocketManagerThread");
        thread.setPriority(1);
        thread.start();
    }

    /**
     * 这里虽然可以将获取不到inputStream流的 和 已经关闭的Socket给移除掉。
     * 但是对于那些偷偷默默掉线（如进程被杀死，断电了）的客户端来说并不能及时剔除。
     * 对于这种客户必须通过心跳来实现提出。
     */
    public static void killNotOnlineClient(){
//        ScheduledExecutorService scheduledExecutorService =
//                Executors.newScheduledThreadPool(1);
//        scheduledExecutorService.schedule(()->{
//            //TODO 先不做吧，感觉还得写点
//        },10L,TimeUnit.SECONDS);
    }


    public static void addClient(Socket client) {
        clientList.add(client);
        CONNECT_NUM.incrementAndGet();
    }

    public static void shutdown() {
        shutdown = true;
    }


}
