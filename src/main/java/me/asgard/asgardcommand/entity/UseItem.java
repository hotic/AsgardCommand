package me.asgard.asgardcommand.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UseItem {
    private Boolean consume;
    private List<Item> items;
    private String message;
}
