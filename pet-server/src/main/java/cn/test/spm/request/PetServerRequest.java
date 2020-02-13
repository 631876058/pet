package cn.test.spm.request;

import cn.test.spm.enums.PetRequestMethodEnum;

import java.net.Socket;

/**
 * 将客户端数据包装成一个Request对象
 *
 * @author 郭豪豪
 * @date 2020-02-13
 **/
public class PetServerRequest {

    private String param;

    private PetRequestMethodEnum requestMethod;

    private Socket socket;

    private String socketId;

    public PetServerRequest(String data, Socket socket){
        String[] commandLine = data.split(":");
        PetRequestMethodEnum commandEnum =
                PetRequestMethodEnum.findByCode(commandLine[0].toUpperCase());
        String commandData = commandLine.length > 1 ? commandLine[1] : null;
        this.param = commandData;
        this.socket = socket;
        this.requestMethod = commandEnum;
        this.socketId = socket.getInetAddress()+":"+socket.getPort();
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public PetRequestMethodEnum getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(PetRequestMethodEnum requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }


    @Override
    public String toString(){
        return "PetServerRequest[param=" + this.param +
                ",socketId=" + this.socketId +
                ",requestMethod=" + this.requestMethod == null ? "NONE" : this.requestMethod.getCode() + "]";
    }

  }
