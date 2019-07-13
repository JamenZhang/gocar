package com.gocar.enums;


public enum CarStateEnum {
    AVAILABLE("未借"),
    BORROWED("已借"),
    REPAIR("需维修"),
    BREAKDOWN("报废");

    private String state;

    public String getState() {
        return state;
    }

    CarStateEnum(String state) {
        this.state = state;
    }
}
