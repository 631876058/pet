package cn.test.spm;

import cn.test.spm.executors.PetBizExecutor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * 宠物管理平台服务端
 *
 * @author 郭豪豪
 * @date 2020-02-13
 **/
public class PetManageServer {

    private static volatile boolean shutdown = false;

    public static void main(String[] args) throws IOException {
        startServer(6666);
    }

    public static void startServer(Integer port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6666);

        //启动一个监听程序，用来关闭Server
        Thread thread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            System.out.println("PetServer ShutDownListener start...");
            System.out.println("Enter 1 to shutdown PetServer : ");
            int i = scanner.nextInt();
            if (i == 1) {
                try {
                    ClientSocketManager.shutdown();
                    PetBizExecutor.shutdown();
                    shutdown = true;
                    serverSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        //启动ClientSocket管理器，对已连接的socket进行巡检
        ClientSocketManager.start();

        //接受客户端请求
        while(!shutdown){
            try {
                System.out.println("PetServer wait for client connect...");
                Socket client = serverSocket.accept();
                System.out.println("PetServer accept a client connect [" + client.getInetAddress() + ":" + client.getPort() + "]");
                //将这个client交给ClientSocketManager管理
                ClientSocketManager.addClient(client);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

    }


}
