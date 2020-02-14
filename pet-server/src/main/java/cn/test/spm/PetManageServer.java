package cn.test.spm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
