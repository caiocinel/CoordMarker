package me.okay.coordsaver.command;

import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.CustomSubcommand;
import me.okay.coordsaver.objects.CoordsObj;
import me.okay.coordsaver.utils.ColorFormat;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CoordsItem extends CustomSubcommand {
    private final CoordSaver plugin;

    public CoordsItem(CoordSaver plugin) {
        super(
            "item",
            "Set item used to display in menu",
            "coordsaver.coords.list",
            "list [<page>] [<player>]"
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
            sender.sendMessage(ColorFormat.colorize("&cUsage: /coordsaver:coords item <name>"));
            return true;
        }

        Player player = (Player) sender;

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
