package cn.test.spm.factory.action;

import java.net.Socket;

/**
 * 异常反馈
 *
 * @author 郭豪豪
 * @date 2020-02-13
 **/
public class ExceptionPetServerAction extends AbstractPetServerAction {

    public ExceptionPetServerAction(String socketId, Socket socket, String data) {
        super(socketId, socket, data);
    }

    @Override
    protected void action(Socket socket, String data) {
        //异常发生时的Action
        System.out.println("ExceptionPetServerAction : " + data);
    }

}
