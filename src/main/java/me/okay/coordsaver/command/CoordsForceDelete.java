package me.okay.coordsaver.command;

import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.CustomSubcommand;
import me.okay.coordsaver.utils.ColorFormat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoordsForceDelete extends CustomSubcommand {
    private final CoordSaver plugin;

    public CoordsForceDelete(CoordSaver plugin) {
        super(
            "force-delete",
            "Delete a saved coordinate from another player",
            "coordsaver.coords.forcedelete",
            "force-delete <player-name> <name>"
        );

        this.plugin = plugin;
    }

    @Override
    public boolean onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorFormat.colorize("&cThis command can only be run by a player."));
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 2)
            return false;

        String playerName = args[0];
        String coordName = args[1];

        boolean removed = plugin.getDatabase().forceDeleteCoord(playerName, coordName);
        if (removed) {
            player.sendMessage(ColorFormat.colorize("&aCoordinate &6") + coordName + ColorFormat.colorize(" &adeleted."));
        }
        else {
            player.sendMessage(ColorFormat.colorize("&cCoordinate &6") + ColorFormat.colorize(" &cnot found."));
        }

        return true;
    }
}
