package cn.test.spm.enums;

import cn.test.spm.factory.action.GetPetServerAction;
import cn.test.spm.factory.action.ListPetServerAction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 指定类型
 *
 * @author 郭豪豪
 * @date 2020-02-13
 **/
public enum PetRequestMethodEnum {

    GET("GET", "领养宠物", GetPetServerAction.class),
    LIST("LIST", "领取排行", ListPetServerAction.class);


    public static final Map<String, PetRequestMethodEnum> petCommandEnumMap =
            new ConcurrentHashMap<String, PetRequestMethodEnum>(2);

    static {
        for (PetRequestMethodEnum petCommandEnum : values()){
            petCommandEnumMap.put(petCommandEnum.getCode(),petCommandEnum);
        }
    }

    private String code;

    private String description;

    private Class action;

    PetRequestMethodEnum(String code, String description,Class processorType) {
        this.code = code;
        this.description = description;
        this.action = processorType;
    }

    public static Map<String, PetRequestMethodEnum> getPetCommandEnumMap() {
        return petCommandEnumMap;
    }

    public Class getAction() {
        return action;
    }

    public void setAction(Class action) {
        this.action = action;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static PetRequestMethodEnum findByCode(String code){
       return petCommandEnumMap.get(code);
    }
}
