package me.okay.coordsaver.command;

import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.CustomSubcommand;
import me.okay.coordsaver.utils.ColorFormat;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class CoordsCreateGUI extends CustomSubcommand {
    private final CoordSaver plugin;

    public CoordsCreateGUI(CoordSaver plugin) {
        super(
            "create-gui",
            "Coords Creation with GUI",
            "coordsaver.coords.clear"
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

        new AnvilGUI.Builder()
                .onClose(stateSnapshot -> {
                    stateSnapshot.getPlayer().sendMessage("You closed the inventory.");
                })
                .onClick((slot, stateSnapshot) -> { // Either use sync or async variant, not both
                    if(slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    if(stateSnapshot.getText().equalsIgnoreCase("you")) {
                        stateSnapshot.getPlayer().sendMessage("You have magical powers!");
                        return Arrays.asList(AnvilGUI.ResponseAction.close());
                    } else {
                        return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText("Try again"));
                    }
                })
                .preventClose()                                                    //prevents the inventory from being closed
                .text("What is the meaning of life?")                              //sets the text the GUI should start with
                .title("Enter your answer.")                                       //set the title of the GUI (only works in 1.14+)
                .plugin(CoordSaver.getInstance())                                          //set the plugin instance
                .open(player);                                                   //opens the GUI for the player provided


        return true;
    }
}
