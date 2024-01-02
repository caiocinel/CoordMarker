package me.okay.coordsaver.command;

import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.CustomSubcommand;
import me.okay.coordsaver.objects.CoordsObj;
import me.okay.coordsaver.utils.ColorFormat;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CoordsRenameGUI extends CustomSubcommand {
    private final CoordSaver plugin;

    public CoordsRenameGUI(CoordSaver plugin) {
        super(
            "rename-gui",
            "Coords Renaming with GUI",
            "coordsaver.coords.clear",
            "rename-gui [old]"
        );

        this.plugin = plugin;
    }

    @Override
    public boolean onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorFormat.colorize("&cThis command can only be run by a player."));
            return true;
        }

        if(args.length < 1)
            return false;

        CoordsObj coord = plugin.getDatabase().getCoord(player.getUniqueId(), String.join(" ", Arrays.copyOfRange(args, 0, args.length)));

        if(coord == null){
            sender.sendMessage(ColorFormat.colorize("&cCoordinate not found."));
            return true;
        }

        if(!coord.uuid.toString().equals(player.getUniqueId().toString())){
            sender.sendMessage(ColorFormat.colorize("&cYou can't rename other player's coordinates."));
            return true;
        }

        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if(slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String name = stateSnapshot.getText();

                    if(name.isBlank())
                        return Collections.emptyList();

                    plugin.getDatabase().renameCoords(coord.name, name);

                    sender.sendMessage("Coordinate "+coord.name+" renamed to "+name+" successfully.");

                    player.performCommand("coordsaver:coords info "+name);

                    return List.of(AnvilGUI.ResponseAction.close());

                })
                .text(coord.name)
                .title("Renaming "+coord.name)
                .plugin(CoordSaver.getInstance())
                .open(player);


        return true;
    }

}
