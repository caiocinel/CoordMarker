package com.caiocinel.coordmarker;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import lombok.Getter;
import com.caiocinel.coordmarker.command.Coords;
import com.caiocinel.coordmarker.objects.CoordsObj;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CoordMarker extends SimplePlugin {
    public static final String BORDER_LINE = ChatColor.DARK_BLUE + "" + ChatColor.STRIKETHROUGH + "----------------------------------------------------";
    public static final int COORDS_PER_PAGE = 28;
    public static final int DISTANCE_TOLERANCE = 2;

    public static class TrackedCoords {
        public Player player;
        public BossBar bossBar;
        public CoordsObj coords;
        public Location initialPos;

        public TrackedCoords(Player player, CoordsObj coords) {
            this.player = player;
            this.bossBar = Bukkit.createBossBar("Tracking "+coords.name, org.bukkit.boss.BarColor.BLUE, org.bukkit.boss.BarStyle.SOLID);
            this.coords = coords;
            this.initialPos = player.getLocation();

            bossBar.addPlayer(player);
        }
    }

    public static Map<UUID, TrackedCoords> trackedCoords = new HashMap<>();

    @Getter
    private Database database;

    @Override
    public void onPluginStart() {

        if (!getDataFolder().mkdir())
            getLogger().info("Data Folder not Created.");

        database = new Database(this);

        FileConfiguration conf = this.getConfig();
        conf.options().copyDefaults(true);

        if(this.getConfig().get("teleport-exp-cost") == null)
            this.getConfig().set("teleport-exp-cost", 0);

        this.saveConfig();

        // Commands
        new Coords(this);


        // Runnable
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for(TrackedCoords tracked : trackedCoords.values()) {

                if(!tracked.player.getWorld().getName().equals(tracked.coords.world))
                    continue;

                if(!tracked.player.isOnline()) {
                    tracked.bossBar.removeAll();
                    trackedCoords.remove(tracked.player.getUniqueId());
                    continue;
                }

                int InitialDistance = (int) tracked.initialPos.distance(tracked.coords.getLocation());
                int currentDistance = (int) tracked.player.getLocation().distance(tracked.coords.getLocation());

                if(currentDistance < DISTANCE_TOLERANCE){
                    tracked.bossBar.removeAll();
                    trackedCoords.remove(tracked.player.getUniqueId());
                    tracked.player.sendMessage(ChatColor.GOLD + "You've arrived at your destination.");

                    for(ItemStack item : tracked.player.getInventory().getContents()){
                        if(item == null)
                            continue;

                        if(!NBTEditor.getBoolean(item, "coordmarker"))
                            continue;

                        CompassMeta meta = (CompassMeta) item.getItemMeta();

                        if(meta == null)
                            continue;

                        if(meta.getLodestone() == null)
                            continue;

                        if(meta.getLodestone().equals(tracked.coords.getLocation()))
                            tracked.player.getInventory().remove(item);
                    }

                    continue;
                }

                int distance = (int) (((double) currentDistance / (double) InitialDistance) * 100);

                if(distance > 100)
                    distance = 100;

                tracked.bossBar.setProgress((double) distance / 100);
                tracked.bossBar.setTitle("Tracking "+tracked.coords.name+" ("+currentDistance+"m)");
            }
        }, 0, 5);
    }

    @Override
    public void onPluginStop() {
        database.safeDisconnect();
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {

        if(event.getItem() == null)
            return;

        if(event.getItem().getType() != CompMaterial.COMPASS.getMaterial())
            return;

        if(!(event.getAction() == Action.RIGHT_CLICK_AIR) && !(event.getAction() == Action.RIGHT_CLICK_BLOCK))
            return;

        if(!NBTEditor.getBoolean(event.getItem(), "coordmarker"))
            return;

        CompassMeta meta = (CompassMeta) event.getItem().getItemMeta();

        if(meta == null)
            return;

        Location loc = meta.getLodestone();

        if(loc == null)
            return;

        if(!event.getPlayer().getWorld().getName().equals(Objects.requireNonNull(loc.getWorld()).getName()))
            return;

        int distance = (int) event.getPlayer().getLocation().distance(loc);


        if(distance > DISTANCE_TOLERANCE) {
            event.getPlayer().sendMessage(ChatColor.GOLD + "Distance: " + ChatColor.WHITE + distance);
            return;
        }

        event.getPlayer().getInventory().remove(event.getItem());
        event.getPlayer().sendMessage("You've arrived at your destination.");

        trackedCoords.get(event.getPlayer().getUniqueId()).bossBar.removeAll();
        trackedCoords.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onItemSpawn(PlayerDropItemEvent event) {
        if(event.getItemDrop().getItemStack().getType() != Material.COMPASS)
            return;

        if(!NBTEditor.getBoolean(event.getItemDrop().getItemStack(), "coordmarker"))
            return;

        event.getItemDrop().remove();
        if(trackedCoords.get(event.getPlayer().getUniqueId()) != null)
            trackedCoords.get(event.getPlayer().getUniqueId()).bossBar.removeAll();
        trackedCoords.remove(event.getPlayer().getUniqueId());
    }

    public static CoordMarker getInstance() {
        return getPlugin(CoordMarker.class);
    }
}