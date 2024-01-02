package me.okay.coordsaver.menu;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.objects.CoordsObj;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.Lightable;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.scheduler.BukkitScheduler;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.model.MenuClickLocation;
import org.mineacademy.fo.remain.CompMaterial;

import javax.annotation.Nullable;
import java.util.*;

public class CoordsInfoMenu extends Menu {

    private final Map<Integer, Button> buttons = new HashMap<>();

    @Override
    protected List<Button> getButtonsToAutoRegister() {
        return new ArrayList<>(buttons.values());
    }

    CoordsObj coordinate;
    Player targetPlayer;

    public CoordsInfoMenu(CoordsObj coordinate, Player targetPlayer) {

        this.coordinate = coordinate;
        this.targetPlayer = targetPlayer;

        setTitle("&8"+coordinate.name);
        setSize(9 * 3);

        buttons.put(10, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                for(ItemStack item : player.getInventory().getContents()){
                    if(item == null)
                        continue;

                    if(!NBTEditor.getBoolean(item, "coordsaver"))
                        continue;

                    CompassMeta meta = (CompassMeta) item.getItemMeta();

                    if(meta == null)
                        continue;

                    if(meta.getLodestone() == null)
                        continue;

                    if(meta.getLodestone().getBlockX() == coordinate.x && meta.getLodestone().getBlockY() == coordinate.y && meta.getLodestone().getBlockZ() == coordinate.z){
                        player.getInventory().remove(item);
                        player.getInventory().addItem(player.getInventory().getItemInMainHand());
                        player.getInventory().setItemInMainHand(item);

                        return;
                    }else{
                        player.getInventory().remove(item);
                    }
                }

                if(player.getInventory().firstEmpty() == -1){
                    player.sendMessage("Inventory is full!");
                    return;
                }

                ItemStack compass = ItemCreator.of(CompMaterial.COMPASS, "Track").make();
                CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();

                Objects.requireNonNull(compassMeta);

                compassMeta.setLodestoneTracked(false);
                compassMeta.setLodestone(coordinate.getLocation());
                compassMeta.setDisplayName("Track "+coordinate.name);
                compass.setItemMeta(compassMeta);

                compass = NBTEditor.set(compass, true, "coordsaver");

                //move hand item to inventory, and give compass
                player.getInventory().addItem(player.getInventory().getItemInMainHand());
                player.getInventory().setItemInMainHand(compass);

                CoordSaver.TrackedCoords trackedCoords = new CoordSaver.TrackedCoords(player, coordinate);
                CoordSaver.trackedCoords.put(player.getUniqueId(), trackedCoords);

                player.sendMessage("Tracking "+coordinate.name);

                player.closeInventory();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.COMPASS, "Track").make();
            }
        });

        buttons.put(11, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                player.teleport(new Location(player.getWorld(), coordinate.x, coordinate.y, coordinate.z, 0, 0));
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.ENDER_PEARL, "Teleport").make();
            }
        });

        buttons.put(12, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                player.closeInventory();
                player.performCommand("coordsaver:coords rename-gui "+coordinate.name);
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.PAPER, "Rename").make();
            }
        });

        buttons.put(13, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.fromString(coordinate.item.equals("AIR") ? "COMPASS" : coordinate.item), "Change Item", "Drag and drop item here to change").make();
            }
        });

        buttons.put(14, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {

                coordinate.global = coordinate.global == 1 ? 0 : 1;
                CoordSaver.getInstance().getDatabase().saveCoords(coordinate);
                new CoordsInfoMenu(coordinate, targetPlayer).displayTo(targetPlayer);
            }

            @Override
            public ItemStack getItem() {
                CompMaterial item;

                if(coordinate.global == 1)
                    item = CompMaterial.REDSTONE_TORCH;
                else
                    item = CompMaterial.TORCH;


                return ItemCreator.of(item, "Visibility", "Allow another users to see this coord\n\nCurrent: "+(coordinate.global == 1 ? "Global ": "Private")).make();
            }
        });

        buttons.put(16, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                player.closeInventory();
                player.performCommand("coordsaver:coords delete "+coordinate.name);
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.LAVA_BUCKET, "&6&lDelete "+coordinate.name).make();
            }
        });

        buttons.put(18, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                new CoordsListMenu(targetPlayer, 1).displayTo(player);
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.RED_BED, "Go To Main Menu").make();
            }
        });

    }


    @Override
    protected boolean isActionAllowed(MenuClickLocation location, int slot, @Nullable ItemStack clicked, @Nullable ItemStack cursor, InventoryAction action) {
        if(location.equals(MenuClickLocation.PLAYER_INVENTORY))
            return true;

        if(location.equals(MenuClickLocation.MENU) && cursor == null)
            return false;

        if(location.equals(MenuClickLocation.MENU) && slot == 13 && action == InventoryAction.SWAP_WITH_CURSOR) {
            coordinate.item = cursor.getType().toString();
            CoordSaver.getInstance().getDatabase().saveCoords(coordinate);

            new CoordsInfoMenu(coordinate, targetPlayer).displayTo(targetPlayer);
            return false;
        }

        return false;
    }

    @Override
    public ItemStack getItemAt(int slot) {
        if (buttons.containsKey(slot))
            return buttons.get(slot).getItem();
        if ((slot < 10) || (slot > 18) || (slot % 9 == 0) || (slot % 9 == 8))
            return ItemCreator.of(CompMaterial.BLACK_STAINED_GLASS_PANE, " ").make();
        else
            return ItemCreator.of(CompMaterial.AIR, " ").make();
    }
}