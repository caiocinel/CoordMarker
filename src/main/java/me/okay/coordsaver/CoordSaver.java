package me.okay.coordsaver;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import lombok.Getter;
import me.okay.coordsaver.objects.CoordsObj;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.boss.BossBar;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.plugin.java.JavaPlugin;

import me.okay.coordsaver.command.Coords;
import net.wesjd.anvilgui.AnvilGUI;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CoordSaver extends SimplePlugin {
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

                        if(!NBTEditor.getBoolean(item, "coordsaver"))
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

        if(!NBTEditor.getBoolean(event.getItem(), "coordsaver"))
            return;

        CompassMeta meta = (CompassMeta) event.getItem().getItemMeta();

        if(meta == null)
            return;

        Location loc = meta.getLodestone();

        if(loc == null)
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

        if(!NBTEditor.getBoolean(event.getItemDrop().getItemStack(), "coordsaver"))
            return;

        event.getItemDrop().remove();
        if(trackedCoords.get(event.getPlayer().getUniqueId()) != null)
            trackedCoords.get(event.getPlayer().getUniqueId()).bossBar.removeAll();
        trackedCoords.remove(event.getPlayer().getUniqueId());
    }

    public static CoordSaver getInstance() {
        return getPlugin(CoordSaver.class);
    }
}