package cn.test.spm.factory.action;

import cn.test.spm.db.PetDataBase;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

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
    protected void action(Socket socket, String data) {
        System.out.println("GetPetServerAction : " + data);
        AtomicInteger num =
                PetDataBase.petData.get(data);
        //ConcurrentMap是并发包的类，但是它不能保证，对于value的递增操作是原子的。
        if (num == null) {
            synchronized (PetDataBase.petData) {
                if (num == null) {
                    num = new AtomicInteger(0);
                }
            }
        }
        int currentNum = num.incrementAndGet();
        PetDataBase.petData.put(data, num);
        sendMsg2Client("OK");
    }
}
