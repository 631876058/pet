package cn.test.spm.enums;

/**
 * 指定类型
 *
 * @author 郭豪豪
 * @date 2020-02-13
 **/
public enum CommandEnum {

    GET("GET","领养宠物"),
    LIST("LIST","领取排行");

    private String command;

    private String description;

    CommandEnum(String command,String description){
        this.command = command;
        this.description = description;
    }


}
