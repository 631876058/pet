package cn.test.spm.executors;



import cn.test.spm.factory.AbstractPetServerActionFactory;
import cn.test.spm.factory.PetServerActionFactory;
import cn.test.spm.factory.action.AbstractPetServerAction;
import cn.test.spm.request.PetServerRequest;

import java.util.concurrent.*;

/**
 * 核型执行器，是一个线程池
 *
 * @author 郭豪豪
 * @date 2020-02-13
 **/
public class PetBizExecutor {

    /**
     * 创建一个固定长度的线程池
     */
    private static final ExecutorService bizExecutor =  new ThreadPoolExecutor(
            4,
            4, 0,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(),
            new ThreadPoolExecutor.AbortPolicy());

    private static final PetServerActionFactory petServerActionFactory = new PetServerActionFactory();


    
    public static void execute(PetServerRequest request){
        AbstractPetServerAction action =
                petServerActionFactory.createAction(request);
        bizExecutor.execute(action);
    }

    public static void shutdown(){
        bizExecutor.shutdown();
    }





}
