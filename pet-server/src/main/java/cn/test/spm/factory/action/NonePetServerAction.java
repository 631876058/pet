package cn.test.spm.factory.action;

import java.net.Socket;

/**
 * 不支持该服务
 *
 * @author 郭豪豪
 * @date 2020-02-13
 **/
public class NonePetServerAction extends AbstractPetServerAction {

    public NonePetServerAction(String socketId,Socket socket,String data){
        super(socketId,socket,data);
    }


    @Override
    protected void action(Socket socket, String data) {
        //当找不到执行那个Action的时候默认的Action
        System.out.println("NonePetServerAction : " + data);
    }
}
