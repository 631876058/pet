package cn.test.spm.request;

import cn.test.spm.command.PetBaseCommand;
import cn.test.spm.enums.CommandEnum;

import java.net.Socket;

/**
 * 用来封装 客户端请求
 *
 * @author 郭豪豪
 * @date 2020-02-13
 **/
public class PetServerRequest {

    public PetServerRequest(Socket socket,String data){
        this.socket = socket;
        this.params = data;
    }

    private Socket socket;

    private String params;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * 此方法会根据
     * @return
     */
    public PetBaseCommand buildCommand(){
        return null;
    }
}
