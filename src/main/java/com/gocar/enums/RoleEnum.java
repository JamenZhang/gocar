package com.gocar.enums;

/**
 *角色枚举
 */
public enum RoleEnum {

    SUPER_MANAGER("超级管理员"),
    MANAGER("普通管理员");

    private String massage;

    public String getMassage() {
        return massage;
    }


    RoleEnum(String massage){
        this.massage = massage;
    }

}
