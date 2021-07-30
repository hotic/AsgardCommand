package me.asgard.asgardcommand.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UseMoney {
    private Boolean consume;
    private Double value;
    private String message;

    public String getMessage() {
        return message == null ? "none" : message;
    }
}
