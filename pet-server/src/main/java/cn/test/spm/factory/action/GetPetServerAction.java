package cn.test.spm.factory.action;

import cn.test.spm.db.PetDataBase;

import java.net.Socket;

/**
 * 领取宠物处理器
 *
 * @author 郭豪豪
 * @date 2020-02-13
 **/
public class GetPetServerAction extends AbstractPetServerAction {

    public GetPetServerAction(String socketId, Socket socket, String data) {
        super(socketId, socket, data);
    }

    @Override
    protected void action(Socket socket, String pet) {
        System.out.println("GetPetServerAction : " + pet);
        //通过最简单的同步代码块实现吧
        synchronized (PetDataBase.petData){
            Integer num = PetDataBase.petData.get(pet);
            if (num == null) {
                num = 0;
            }
            PetDataBase.petData.put(pet,++num);
        }
        sendMsg2Client("OK");
    }
}
