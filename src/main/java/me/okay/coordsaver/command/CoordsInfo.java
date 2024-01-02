package me.okay.coordsaver.command;

import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.CustomSubcommand;
import me.okay.coordsaver.menu.CoordsInfoMenu;
import me.okay.coordsaver.objects.CoordsObj;
import me.okay.coordsaver.utils.ColorFormat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CoordsInfo extends CustomSubcommand {
    private final CoordSaver plugin;

    public CoordsInfo(CoordSaver plugin) {
        super(
            "info",
            "Set item used to display in menu",
            "coordsaver.coords.list",
            "info [name]"
        );

        this.plugin = plugin;
    }

    @Override
    public boolean onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorFormat.colorize("&cThis command can only be run by a player."));
            return true;
        }

        if(args.length == 0){
            sender.sendMessage(ColorFormat.colorize("&cUsage: /coordsaver:coords info <name>"));
            return true;
        }

        Player player = (Player) sender;

        CoordsObj coords = plugin.getDatabase().getCoord(((Player) sender).getUniqueId(), String.join(" ", Arrays.copyOfRange(args, 0, args.length)));

        if(coords == null){
            sender.sendMessage(ColorFormat.colorize("&cCoordinate not found"));
            return true;
        }

        new CoordsInfoMenu(coords, player).displayTo(player);

        return true;
    }
}
