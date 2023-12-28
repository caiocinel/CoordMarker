package me.okay.coordsaver.objects;

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
}
