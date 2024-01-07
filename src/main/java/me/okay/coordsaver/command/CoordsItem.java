package me.okay.coordsaver.command;

import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.CustomSubcommand;
import me.okay.coordsaver.objects.CoordsObj;
import me.okay.coordsaver.utils.ColorFormat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CoordsItem extends CustomSubcommand {
    private final CoordSaver plugin;

    public CoordsItem(CoordSaver plugin) {
        super(
            "item",
            "Set item used to display in menu",
            "coordsaver.item",
            "item <name>"
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
            sender.sendMessage(ColorFormat.colorize("&cUsage: /coordsaver:coords item <name>"));
            return true;
        }

        CoordsObj coords = plugin.getDatabase().getCoord(((Player) sender).getUniqueId(), String.join(" ", Arrays.copyOfRange(args, 0, args.length)));

        if(coords == null){
            sender.sendMessage(ColorFormat.colorize("&cCoordinate not found"));
            return true;
        }

        if(!coords.uuid.toString().equals(player.getUniqueId().toString())){
            sender.sendMessage(ColorFormat.colorize("&cYou only can change own coordinates"));
            return true;
        }


        coords.item = player.getInventory().getItemInMainHand().getType().toString();

        if(coords.item.equals("AIR")) {
            sender.sendMessage(ColorFormat.colorize("&cYou need have a item in your hands"));
            return true;
        }

        plugin.getDatabase().saveCoords(coords);

        sender.sendMessage("Block Set");

        return true;
    }
}
