package com.caiocinel.coordmarker;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

public abstract class CustomCommand extends CustomSubcommand implements CommandExecutor, TabCompleter {
    private final PluginCommand command;

    public CustomCommand(CoordMarker plugin, String commandName) {
        super(plugin, commandName);
        
        command = plugin.getCommand(commandName);
        command.setExecutor(this);
    }

    public PluginCommand getCommand() {
        return command;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        super.onCommand(sender, this, label, args);

        return true;
    };

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return super.onTabComplete(sender, this, label, args);
    }
}
