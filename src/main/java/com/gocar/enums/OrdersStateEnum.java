package com.gocar.enums;


public enum OrdersStateEnum {
    NOT_RETURN("未归还"),
    RETURN("归还,汽车完好"),
    RETURN_REPAIR("归还,汽车需维修"),
    RETURN_BREAKDOWN("归还,汽车已报废");

    private String state;

    public String getState() {
        return state;
    }

    OrdersStateEnum(String state) {
        this.state = state;
    }
}
