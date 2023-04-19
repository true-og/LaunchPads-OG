package me.barny1094875.launchpad.Commands;

import me.barny1094875.launchpad.LaunchPad;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class reloadPadConfig implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(command.getName().equalsIgnoreCase("reloadpadconfig")) {
            if(sender instanceof Player){
                if(sender.hasPermission("launchpad.canreload")){
                    LaunchPad.config().reloadConfig();
                }
            }
        }

        return true;
    }
}
