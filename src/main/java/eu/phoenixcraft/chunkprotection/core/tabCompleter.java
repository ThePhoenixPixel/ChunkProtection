package eu.phoenixcraft.chunkprotection.core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class tabCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (command.getName().toString().equals("cp")) {
            if (args.length == 1) {
                String input = args[0].toLowerCase();
                List<String> options = Arrays.asList("claim", "unclaim", "info", "buy", "resell");
                List<String> filteredOptions = new ArrayList<>();
                for (String option : options) {
                    if (option.startsWith(input))
                        filteredOptions.add(option);
                }
                return filteredOptions;
            }
        }

        return null;
    }
}

