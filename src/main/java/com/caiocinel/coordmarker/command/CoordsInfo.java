package com.caiocinel.coordmarker.command;

import com.caiocinel.coordmarker.CoordMarker;
import com.caiocinel.coordmarker.CustomSubcommand;
import com.caiocinel.coordmarker.menu.CoordsInfoMenu;
import com.caiocinel.coordmarker.objects.CoordsObj;
import com.caiocinel.coordmarker.utils.ColorFormat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CoordsInfo extends CustomSubcommand {
    private final CoordMarker plugin;

    public CoordsInfo(CoordMarker plugin) {
        super(
            "info",
            "Set item used to display in menu",
            "coordmarker.info",
            "info [name]"
        );

        this.plugin = plugin;
    }

    @Override
    public boolean onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorFormat.colorize("&cThis command can only be run by a player."));
            return true;
        }

        if(args.length == 0){
            sender.sendMessage(ColorFormat.colorize("&cUsage: /coordmarker:coords info <name>"));
            return true;
        }

        CoordsObj coords = plugin.getDatabase().getCoord(((Player) sender).getUniqueId(), String.join(" ", Arrays.copyOfRange(args, 0, args.length)));

        if(coords == null){
            sender.sendMessage(ColorFormat.colorize("&cCoordinate not found"));
            return true;
        }


        new CoordsInfoMenu(coords, player).displayTo(player);

        return true;
    }
}
