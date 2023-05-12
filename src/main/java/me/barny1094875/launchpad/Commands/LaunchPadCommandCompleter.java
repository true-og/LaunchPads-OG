package me.barny1094875.launchpad.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LaunchPadCommandCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(args.length == 1){

            // build the list to return
            ArrayList<String> result = new ArrayList<>();
            result.add("set");
            result.add("give");
            result.add("delete");
            result.add("reloadconfig");

            // sort the list
            Collections.sort(result);

            return result;

        }

        return null;
    }

}
