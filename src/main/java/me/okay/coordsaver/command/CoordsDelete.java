package me.okay.coordsaver.command;

import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.CustomSubcommand;
import me.okay.coordsaver.utils.ColorFormat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CoordsDelete extends CustomSubcommand {
    private final CoordSaver plugin;

    public CoordsDelete(CoordSaver plugin) {
        super(
            "delete",
            "Delete a saved coordinate",
            "coordsaver.coords.delete",
            "delete <name>"
        );

        this.plugin = plugin;
    }

    @Override
    public boolean onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorFormat.colorize("&cThis command can only be run by a player."));
            return true;
        }

        if (args.length < 1)
            return false;

        String name = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

        boolean removed = plugin.getDatabase().deleteCoords(player.getUniqueId(), name);
        if (removed) {
            player.sendMessage(ColorFormat.colorize("&aCoordinate &6") + name + ColorFormat.colorize(" &adeleted."));
        }
        else {
            player.sendMessage(ColorFormat.colorize("&cCoordinate &6") + name + ColorFormat.colorize(" &cnot found."));
        }

        return true;
    }
}
