package com.caiocinel.coordmarker.command;

import com.caiocinel.coordmarker.CoordMarker;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.caiocinel.coordmarker.CustomCommand;
import com.caiocinel.coordmarker.CustomSubcommand;


public class Coords extends CustomCommand {
    public Coords(CoordMarker plugin) {
        super(plugin, "coords");

        addSubcommand(new CoordsClear(plugin));
        addSubcommand(new CoordsCreate(plugin));
        addSubcommand(new CoordsDelete(plugin));
        addSubcommand(new CoordsGUI(plugin));
        addSubcommand(new CoordsItem(plugin));
        addSubcommand(new CoordsCreateGUI(plugin));
        addSubcommand(new CoordsRenameGUI(plugin));
        addSubcommand(new CoordsInfo(plugin));
    }

    @Override
    public boolean onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (args.length == 0) {
            return Bukkit.dispatchCommand(sender, "coordmarker:coords gui");
        }

        return false;
    }
}
