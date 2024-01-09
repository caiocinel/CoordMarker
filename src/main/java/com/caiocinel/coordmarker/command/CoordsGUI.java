package com.caiocinel.coordmarker.command;

import com.caiocinel.coordmarker.CoordMarker;
import com.caiocinel.coordmarker.CustomSubcommand;
import com.caiocinel.coordmarker.menu.CoordsListMenu;
import com.caiocinel.coordmarker.utils.ColorFormat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CoordsGUI extends CustomSubcommand {
    private final CoordMarker plugin;

    public CoordsGUI(CoordMarker plugin) {
        super(
            "gui",
            "Open Coords GUI",
            "coordmarker.list",
            "gui [<page>] [<player>]"
        );

        this.plugin = plugin;
    }

    @Override
    public boolean onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        int page = 1;

        if(args.length > 0){
            try{
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {
                return false;
            }
        }

        Player targetPlayer;
        if (args.length < 2) {
            if (sender instanceof Player)
                targetPlayer = (Player) sender;
            else {
                sender.sendMessage(ColorFormat.colorize("&cYou must specify a player if using this command from console."));
                return false;
            }
        }
        else {
            targetPlayer = plugin.getServer().getPlayer(args[1]);
            if (targetPlayer == null) {
                sender.sendMessage(ColorFormat.colorize("&cPlayer not found."));
                return true;
            }
        }

        new CoordsListMenu(targetPlayer, page, -1).displayTo(targetPlayer);


        sender.sendMessage("Coords GUI");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (args.length == 2 && sender.hasPermission("coordmarker.coords.list.others")) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(name -> name.startsWith(args[1])).collect(Collectors.toList());
        }

        return List.of();
    }
}
