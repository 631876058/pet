package cn.test.spm.factory;

import cn.test.spm.enums.PetRequestMethodEnum;
import cn.test.spm.factory.action.*;
import cn.test.spm.request.PetServerRequest;

import java.lang.reflect.Constructor;
import java.net.Socket;


/**
 * @author 郭豪豪
 * @date 2020-02-13
 **/
public class PetServerActionFactory extends AbstractPetServerActionFactory {


    @Override
    public AbstractPetServerAction createAction(PetServerRequest request) {

        PetRequestMethodEnum requestMethod = request.getRequestMethod();
        System.out.println(request.toString());

        String socketId = request.getSocketId();
        Socket socket = request.getSocket();
        String param = request.getParam();

        if (requestMethod == null) {
            return new NonePetServerAction(socketId, socket, param);
        }
        try {
            //下面通过反射的方式实现
            Class action = requestMethod.getAction();
            Constructor declaredConstructor =
                    action.getDeclaredConstructor(String.class, Socket.class, String.class);
            declaredConstructor.setAccessible(true);
            return (AbstractPetServerAction)declaredConstructor.newInstance(socketId, socket, param);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ExceptionPetServerAction(socketId, socket, param);
        }

    }

}
