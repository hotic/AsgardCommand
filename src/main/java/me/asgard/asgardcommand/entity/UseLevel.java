package me.asgard.asgardcommand.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UseLevel {
    private Boolean consume;
    private Integer level;
    private String message;

    public String getMessage() {
        return message == null ? "none" : message;
    }
}
