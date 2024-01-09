package com.caiocinel.coordmarker.command;

import com.caiocinel.coordmarker.CoordMarker;
import com.caiocinel.coordmarker.CustomSubcommand;
import com.caiocinel.coordmarker.objects.CoordsObj;
import com.caiocinel.coordmarker.utils.ColorFormat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CoordsCreate extends CustomSubcommand {
    private final CoordMarker plugin;

    public CoordsCreate(CoordMarker plugin) {
        super(
            "create",
            "Create a Marker",
            "coordmarker.create",
            "create <name> [<x> <y> <z>] [private = true] [<world>]"
        );

        this.plugin = plugin;
    }

    @Override
    public boolean onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        int x, y, z;
        String world;
        boolean global;

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorFormat.colorize("&cThis command can only be run by a player."));
            return true;
        }

        if (args.length < 1)
            return false;

        String name = args[0];
        if (name.isBlank()) {
            player.sendMessage(ColorFormat.colorize("&cName cannot be empty."));
            return true;
        }

        if(args.length == 1)
        {
            Location playerLocation = player.getLocation();
            x = playerLocation.getBlockX();
            y = playerLocation.getBlockY() + 1;
            z = playerLocation.getBlockZ();
        }else{
            try {
                x = Integer.parseInt(args[1]);
                y = Integer.parseInt(args[2]);
                z = Integer.parseInt(args[3]);
            }
            catch (NumberFormatException e) {
                sender.sendMessage(ColorFormat.colorize("&cx, y, and z must be integers."));
                return true;
            }
        }

        if(args.length > 4)
            global = Boolean.parseBoolean(args[4]);
        else
            global = false;

        if(args.length > 5)
            world = args[5];
        else
            world = player.getWorld().getName();

        CoordsObj coordsObj = new CoordsObj(name, player.getUniqueId(),  x, y, z, global ? 1 : 0, world, Material.COMPASS.toString(), player.getName());
        plugin.getDatabase().saveCoords(coordsObj);

        sender.sendMessage(ColorFormat.colorize("&a"+(global ? "Global " : "") +"Coordinate &6" + name + "&a set to &6" + x + " " + y + " " + z + " " + world));

        return true;
    }

    private List<String> suggestCoordinate(int actualCoord, String typedArg) {
        if (String.valueOf(actualCoord).startsWith(typedArg)) {
            return List.of(String.valueOf(actualCoord));
        }
        else {
            return List.of();
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return List.of();
        }

        Location playerLocation = player.getLocation();

        if (args.length == 2) {
            return suggestCoordinate(playerLocation.getBlockX(), args[1]);
        }
        else if (args.length == 3) {
            return suggestCoordinate(playerLocation.getBlockY(), args[2]);
        }
        else if (args.length == 4) {
            return suggestCoordinate(playerLocation.getBlockZ(), args[3]);
        }
        else if (args.length == 5) {
            return List.of("true", "false");
        }
        else if (args.length == 6) {
            List<World> worlds = Bukkit.getWorlds();
            List<String> worldNames = new ArrayList<>();

            for (World world : worlds) {
                if (world.getName().startsWith(args[5])) {
                    worldNames.add(world.getName());
                }
            }

            return worldNames;
        }

        return List.of();
    }
}
