package net.trueog.launchpadsog.Commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LaunchPadCommandCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String label, @NotNull String[] args)
    {

        if (args.length == 1) {

            // build the list to return
            ArrayList<String> result = new ArrayList<>();
            result.add("set");
            result.add("give");
            result.add("delete");
            result.add("reloadconfig");
            result.add("help");

            // sort the list
            Collections.sort(result);

            return result;

        } else if (args.length == 2) {

            if (args[0].equalsIgnoreCase("help")) {

                // build the list to return
                ArrayList<String> result = new ArrayList<>();
                result.add("set");
                result.add("delete");

                // sort the list
                Collections.sort(result);

                return result;

            }

        }

        return null;

    }

}
