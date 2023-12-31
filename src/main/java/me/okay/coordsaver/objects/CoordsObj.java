package me.okay.coordsaver.objects;

import me.okay.coordsaver.utils.ColorFormat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CoordsObj {
    public String name;
    public UUID uuid;
    public int x;
    public int y;
    public int z;
    public int global;
    public String world;
    public String item;
    public String playerName;

    public CoordsObj(String name, UUID uuid, int x, int y, int z, int global, String world, String item, String playerName) {
        this.name = name;
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.global = global;
        this.world = world;
        this.item = item;
        this.playerName = playerName;
    }

    public String toString() {
        return ColorFormat.colorize("&b") + name + ColorFormat.colorize(": &3" + x + " " + y + " " + z + " &9" + world + (global == 1 ? " &a(Global)" : ""));
    }

    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(Material.COMPASS);



        return null;
    }
}
