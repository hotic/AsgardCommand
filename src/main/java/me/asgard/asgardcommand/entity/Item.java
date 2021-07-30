package me.asgard.asgardcommand.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.inventory.ItemStack;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private ItemStack itemStack;
    private Integer number;
}
