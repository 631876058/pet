import java.io.IOException;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

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
    public static Integer CLIENT = 1000;

    /**
     * 单客户端请求数量
     */
    public static Integer PRE_CLIENT_REQUEST_NUM = 10;

    public static Long startToSendTime  = 0L;

    /**
     * 等到所有的client都建立连接了，模拟同时数据发送
     */
    public static CyclicBarrier clientReadyBarrier = new CyclicBarrier(CLIENT, () -> {
        System.out.println("all client ready");
        startToSendTime = System.currentTimeMillis();

    });

    /**
     * 等都数据全部发送完毕，用来获取服务端的值
     */
    public static CountDownLatch dataSendFinishedLatch = new CountDownLatch(CLIENT);


    /**
     * 这里用main方法测试吧，Junit 在主线程运行完，不管有没有子线程就退出了，没发接受结果
     *
     * @param args
     * @throws InterruptedException
     * @throws IOException
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        finalTestGET();
        //等待数据发送完毕
        dataSendFinishedLatch.await();

        //保证客户端已经接受完，服务端的返回数据
        Thread.sleep(200);

        System.out.println("spend time : " + (System.currentTimeMillis() - startToSendTime) + "ms");

        printActualData();

        printServerData();
    }

    public static void finalTestGET() throws InterruptedException {
        for (int i = 0; i < CLIENT; i++) {
            FinalClientTest.ClientTestThread thread = new FinalClientTest.ClientTestThread("client" + i);
            thread.start();
            //适当休息一下，同时建立连接的时候会出现Connect Reset的情况
            Thread.sleep(20);
        }
    }

    public static void printServerData() throws IOException {
        System.out.println("Server Data : ==============");
        PetClient petClient = new PetClient();
        petClient.startClient();
        petClient.sendMsg2Server("LIST");
    }

    public static class ClientTestThread extends Thread {

        private String[] petList = {"cat", "dog", "duck", "panda", "chick", "pig", "elephant"};

        private String tName;

        public ClientTestThread(String t) {
            tName = t;
        }

        //按道理来说这里存储的值是最真实的，每一次发送前都会累加这里的值。
        @Override
        public void run() {
            PetClient petClient = new PetClient();
            try {
                petClient.startClient();
                //一个客户端准备就绪
                System.out.println(tName + " ready!");
                clientReadyBarrier.await();
                System.out.println(tName + " start send!");
                for (int i = 0; i < PRE_CLIENT_REQUEST_NUM; i++) {
                    Random random = new Random();
                    int idx = random.nextInt(petList.length);
                    String pet = petList[idx];
                    System.out.println(tName + "send[ GET:" + pet + " ]");
                    petClient.sendMsg2Server("GET:" + pet);
                    //在本地存一份宠物数量
                    increment(pet);
                }
                //表示一个客户端完成已经发送任务
                dataSendFinishedLatch.countDown();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    //下面：每次客户端发送请求时，都会对这个map进行增加，使用严格的synchronized
    public static volatile ConcurrentHashMap<String, Integer> actualNum = new ConcurrentHashMap<>();

    public static void increment(String pet) {
        //ConcurrentHashMap get没有加锁，所以你拿的值并不是最新的
        //如果判断为空就设置进去，会少算。
        synchronized (actualNum) {
            Integer num = actualNum.get(pet);
            if (num == null) {
                num = 0;
            }
            actualNum.put(pet, ++num);
        }
    }

    public static void printActualData(){
        System.out.println("Actual Data : " + "==============");
        actualNum.entrySet()
                .stream()
                .sorted(Comparator.comparing(e->-e.getValue()))
                .map(e->e.getKey()+":"+e.getValue())
                .forEach(System.out::println);
    }

}
