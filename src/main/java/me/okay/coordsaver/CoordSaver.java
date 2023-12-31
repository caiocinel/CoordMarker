package me.okay.coordsaver;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import me.okay.coordsaver.command.Coords;
import org.mineacademy.fo.plugin.SimplePlugin;

public class CoordSaver extends SimplePlugin {
    public static final String BORDER_LINE = ChatColor.DARK_BLUE + "" + ChatColor.STRIKETHROUGH + "----------------------------------------------------";
    public static final int COORDS_PER_PAGE = 28;

    private Database database;

    @Override
    public void onPluginStart() {
        if (!getDataFolder().mkdir())
            getLogger().info("Data Folder not Created.");

        database = new Database(this);

        // Commands
        new Coords(this);
    }

    @Override
    public void onPluginStop() {
        database.safeDisconnect();
    }

    public Database getDatabase() {
        return database;
    }

    public static CoordSaver getInstance() {
        return getPlugin(CoordSaver.class);
    }
}