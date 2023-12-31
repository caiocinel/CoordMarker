package me.okay.coordsaver.command;

import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.CustomSubcommand;
import me.okay.coordsaver.menu.CoordsListMenu;
import me.okay.coordsaver.objects.CoordsObj;
import me.okay.coordsaver.utils.ColorFormat;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class CoordsGUI extends CustomSubcommand {
    private final CoordSaver plugin;

    public CoordsGUI(CoordSaver plugin) {
        super(
            "gui",
            "Open Coords GUI",
            "coordsaver.coords.list",
            "gui [<page>] [<player>]"
        );

        this.plugin = plugin;
    }

    @Override
    public boolean onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (args.length > 1 && !sender.hasPermission("coordsaver.coords.list.others")) {
            sender.sendMessage(ColorFormat.colorize("&cYou do not have permission to view other players' coordinates. &7(coordsaver.coords.list.others)"));
            return true;
        }

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


        new CoordsListMenu(targetPlayer, page).displayTo(targetPlayer);


        sender.sendMessage("Coords GUI");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (args.length == 2 && sender.hasPermission("coordsaver.coords.list.others")) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(name -> name.startsWith(args[1])).collect(Collectors.toList());
        }

        return List.of();
    }
}
