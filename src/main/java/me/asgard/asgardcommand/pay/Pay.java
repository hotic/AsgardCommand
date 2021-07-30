package me.asgard.asgardcommand.pay;

public interface Pay {
    void takeBalance (String playerName, Double money);
    void addBalance (String playerName, Double money);
    Double getBalance (String playerName);
}
