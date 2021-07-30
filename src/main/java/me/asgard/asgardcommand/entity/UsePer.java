package me.asgard.asgardcommand.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsePer {
    private String per;
    private String message;

    public String getMessage() {
        return message == null ? "none" : message;
    }
}
