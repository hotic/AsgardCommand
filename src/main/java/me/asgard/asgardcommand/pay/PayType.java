package me.asgard.asgardcommand.pay;

import lombok.Getter;
import lombok.Setter;

public enum PayType {
    MONEY("MONEY");
    private String name;

    PayType(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
