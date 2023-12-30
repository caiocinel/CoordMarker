package me.okay.coordsaver.objects;

import me.okay.coordsaver.utils.ColorFormat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CoordsObj {
    public final String name;
    public final UUID uuid;
    public final int x;
    public final int y;
    public final int z;
    public final int global;
    public final String world;

    public CoordsObj(String name, UUID uuid, int x, int y, int z, int global, String world) {
        this.name = name;
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.global = global;
        this.world = world;
    }

    public String toString() {
        return ColorFormat.colorize("&b") + name + ColorFormat.colorize(": &3" + x + " " + y + " " + z + " &9" + world + (global == 1 ? " &a(Global)" : ""));
    }

    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(Material.COMPASS);



        return null;
    }
}
