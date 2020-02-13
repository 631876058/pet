import org.junit.Test;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 最终版测试
 * 先运行GET，等Server那边不在输出任何东西时，运行List。
 *
 * @author 郭豪豪
 * @date 2020-02-14
 **/
public class FinalClientTest {
    /**
     * 客户端数
     */
    public static Integer CLIENT = 500;

    /**
     * 单客户端请求数量
     */
    public static Integer PRE_CLIENT_REQUEST_NUM = 10;

    public static Integer CLIENT_CONNECT_INTERVAL = 20;
    /**
     * 每一个工人完成工作之后就会countDown一下
     */
    public static CountDownLatch countDownLatch = new CountDownLatch(CLIENT);

    /**
     * 这里用main方法测试吧，Junit 在主线程运行完，不管有没有子线程就退出了，没发接受结果
     * @param args
     * @throws InterruptedException
     * @throws IOException
     */
    public static void main(String[] args ) throws InterruptedException, IOException {
//        finalTestGET();
        finalTestLIST();
    }

    public static void finalTestGET() throws InterruptedException {
        for (int i = 0; i < CLIENT; i++) {
            FinalClientTest.ClientTestThread thread = new FinalClientTest.ClientTestThread();
            thread.start();
            //这里适当睡一下，连接数就可以上去了。
            Thread.sleep(CLIENT_CONNECT_INTERVAL);
        }
        //在所有工人工作完成之前要等待，完成之后，输出实际的结果。
        countDownLatch.await();
        System.out.println("Actual Data : ==============");
        FinalClientTest.ClientTestThread.actualNum.forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });
    }

    public static void finalTestLIST() throws IOException {
        System.out.println("Server Data : ==============");
        PetClient petClient = new PetClient();
        petClient.startClient();
        petClient.sendMsg2Server("LIST");
    }

    public static class ClientTestThread extends Thread {

        private String[] petList = {"cat", "dog", "duck", "panda", "chick", "pig", "elephant"};

        //按道理来说这里存储的值是最真实的，每一次发送前都会累加这里的值。
        //最后在发送一个LIST指令来进行对比
        public static volatile ConcurrentHashMap<String, AtomicInteger> actualNum = new ConcurrentHashMap<>();

        @Override
        public void run() {
            PetClient petClient = new PetClient();
            try {
                petClient.startClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < PRE_CLIENT_REQUEST_NUM; i++) {
                Random random = new Random();
                int idx = random.nextInt(petList.length);
                String pet = petList[idx];
                petClient.sendMsg2Server("GET:" + pet);
                increment(pet);
            }
            countDownLatch.countDown();
        }

        public static void increment(String pet) {
            //ConcurrentHashMap get没有加锁，所以你拿的值并不是最新的
            //如果判断为空就设置进去，会少算。
            AtomicInteger integer = actualNum.get(pet);
            if (integer == null) {
                synchronized (actualNum) {
                    if(integer == null) {
                        integer = new AtomicInteger(0);
                    }
                }
            }
            integer.incrementAndGet();
            actualNum.put(pet,integer);
        }


    }

}
