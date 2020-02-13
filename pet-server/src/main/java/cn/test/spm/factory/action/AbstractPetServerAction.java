package cn.test.spm.factory.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 抽象业务处理
 * @author 郭豪豪
 * @date 2020-02-13
 **/
public abstract class AbstractPetServerAction implements Runnable {

    public AbstractPetServerAction(String socketId,Socket socket,String data){
        this.socketId = socketId;
        this.socket = socket;
        this.data = data;
    }

    protected Socket socket;

    protected String socketId;

    protected String data;

    /**
     * 业务逻辑处理
     *
     * @param socket
     * @param data
     */
    protected abstract void action(Socket socket, String data);


    @Override
    public void run() {
        action(this.socket,this.data);
    }

    public void sendMsg2Client(String msg) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream);
            writer.println(msg);
            writer.flush();
        }catch (Exception ex){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("sendMsg2Client exception socket : " + socket.toString());
        }

    }
}
