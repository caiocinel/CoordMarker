package com.caiocinel.coordmarker.command;

import com.caiocinel.coordmarker.CoordMarker;
import com.caiocinel.coordmarker.CustomSubcommand;
import com.caiocinel.coordmarker.objects.CoordsObj;
import com.caiocinel.coordmarker.utils.ColorFormat;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CoordsCreateGUI extends CustomSubcommand {
    private final CoordMarker plugin;

    public CoordsCreateGUI(CoordMarker plugin) {
        super(
            "create-gui",
            "Coords Creation with GUI",
            "coordmarker.create",
            "create-gui [<public = false>]"
        );

        this.plugin = plugin;
    }

    @Override
    public boolean onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorFormat.colorize("&cThis command can only be run by a player."));
            return true;
        }

        boolean global;

        if(args.length > 0)
            global = Boolean.parseBoolean(args[0]);
        else {
            global = false;
        }

        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if(slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String name = stateSnapshot.getText();

                    if(name.isBlank())
                        name = (global ? "Global " : "")+"Coordinate "+ CoordMarker.getInstance().getDatabase().getCoordsCount(player.getUniqueId());

                    Location playerLocation = player.getLocation();
                    int x = playerLocation.getBlockX();
                    int y = playerLocation.getBlockY() + 1;
                    int z = playerLocation.getBlockZ();

                    CoordsObj coordsObj = new CoordsObj(name, player.getUniqueId(),  x, y, z, global ? 1 : 0, player.getWorld().getName(), Material.COMPASS.toString(), player.getName());
                    plugin.getDatabase().saveCoords(coordsObj);

                    sender.sendMessage(ColorFormat.colorize("&a"+(global ? "Global " : "") +"Coordinate &6" + name + "&a set to &6" + x + " " + y + " " + z + " " + player.getWorld().getName()));

                    player.performCommand("coordmarker:coords info "+coordsObj.name);

                    return List.of(AnvilGUI.ResponseAction.close());

                })
                .text((global ? "Global " : "")+"Coordinate "+ CoordMarker.getInstance().getDatabase().getCoordsCount(player.getUniqueId()))
                .title("Name your coordinate")
                .plugin(CoordMarker.getInstance())
                .open(player);


        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (args.length == 1) {
            return List.of("true", "false");
        }

        return List.of();
    }
}
