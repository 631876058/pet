package cn.test.spm.request;

import cn.test.spm.enums.CommandEnum;
import cn.test.spm.executors.BaseExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 * 客户端请求转发给相关执行者
 *
 * @author 郭豪豪
 * @date 2020-02-13
 **/
public class PetServerRequestDispatcher {

    public static final Map<CommandEnum, BaseExecutor> executorMap = new HashMap<CommandEnum, BaseExecutor>(4);

    public void dispatch(){

    }
}
