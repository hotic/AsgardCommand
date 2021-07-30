package me.asgard.asgardcommand.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Command {
    private String key;
    private UseMoney useMoney;
    private UsePer usePermission;
    private UseItem useItems;
    private UseLevel useLevel;
    private Integer cooldown;
    private Integer chance;
    private String sourceCommand;
    private Boolean noArg;
    private List <String> deCommandParam;
    private List <String> noMessages;
    private List <String> deCommands;
    private List <String> commands;
}
