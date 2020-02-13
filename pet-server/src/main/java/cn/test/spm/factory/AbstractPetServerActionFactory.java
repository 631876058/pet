package cn.test.spm.factory;


import cn.test.spm.factory.action.AbstractPetServerAction;
import cn.test.spm.request.PetServerRequest;

/**
 * 工厂，通过一个Request对象产生Action交给Executor去执行
 *
 * @author 郭豪豪
 * @date 2020-02-13
 **/
public abstract class AbstractPetServerActionFactory {

    /**
     * 根据data创建PetServerRequest对象
     *
     * @param request
     * @return
     */
    public abstract AbstractPetServerAction createAction(PetServerRequest request);
}
