package com.caiocinel.coordmarker.menu;

import com.caiocinel.coordmarker.CoordMarker;
import com.caiocinel.coordmarker.objects.CoordsObj;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
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

        int initialPos = 10;

        if(targetPlayer.hasPermission("coordmarker.track")) {
            buttons.put(initialPos, new Button() {
                @Override
                public void onClickedInMenu(Player player, Menu menu, ClickType click) {

                    if (!targetPlayer.hasPermission("coordmarker.track")) {
                        player.sendMessage("You don't have permission to do this");
                        return;
                    }

                    for (ItemStack item : player.getInventory().getContents()) {
                        if (item == null)
                            continue;

                        if (!NBTEditor.getBoolean(item, "coordmarker"))
                            continue;

                        CompassMeta meta = (CompassMeta) item.getItemMeta();

                        if (meta == null)
                            continue;

                        if (meta.getLodestone() == null)
                            continue;

                        if (meta.getLodestone().getBlockX() == coordinate.x && meta.getLodestone().getBlockY() == coordinate.y && meta.getLodestone().getBlockZ() == coordinate.z) {
                            player.getInventory().remove(item);
                            player.getInventory().addItem(player.getInventory().getItemInMainHand());
                            player.getInventory().setItemInMainHand(item);

                            return;
                        } else {
                            player.getInventory().remove(item);
                        }
                    }

                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage("Inventory is full!");
                        return;
                    }

                    ItemStack compass = ItemCreator.of(CompMaterial.COMPASS, "Track").make();
                    CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();

                    Objects.requireNonNull(compassMeta);

                    compassMeta.setLodestoneTracked(false);
                    compassMeta.setLodestone(coordinate.getLocation());
                    compassMeta.setDisplayName("Track " + coordinate.name);
                    compass.setItemMeta(compassMeta);

                    compass = NBTEditor.set(compass, true, "coordmarker");

                    player.getInventory().addItem(player.getInventory().getItemInMainHand());
                    player.getInventory().setItemInMainHand(compass);

                    CoordMarker.TrackedCoords trackedCoords = new CoordMarker.TrackedCoords(player, coordinate);
                    CoordMarker.trackedCoords.put(player.getUniqueId(), trackedCoords);

                    player.sendMessage("Tracking " + coordinate.name);

                    player.closeInventory();
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.COMPASS, "Track").make();
                }
            });
            initialPos++;
        }


        if(targetPlayer.hasPermission("coordmarker.teleport")) {
            buttons.put(initialPos, new Button() {
                @Override
                public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                    if (!targetPlayer.hasPermission("coordmarker.teleport")) {
                        player.sendMessage("You don't have permission to do this");
                        return;
                    }

                    int exp = player.getExpToLevel();

                    if(!player.getGameMode().equals(GameMode.CREATIVE) && exp < CoordMarker.getInstance().getConfig().getInt("teleport-exp-cost")){
                        player.sendMessage("You need at least 30 exp levels to do this");
                        return;
                    }

                    player.teleport(coordinate.getLocation());

                    player.giveExpLevels(CoordMarker.getInstance().getConfig().getInt("teleport-exp-cost") * -1);
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.ENDER_PEARL, "Teleport").make();
                }
            });
            initialPos++;
        }

        if(targetPlayer.hasPermission("coordmarker.rename")) {
            buttons.put(initialPos, new Button() {
                @Override
                public void onClickedInMenu(Player player, Menu menu, ClickType click) {

                    if (!targetPlayer.hasPermission("coordmarker.rename")) {
                        player.sendMessage("You don't have permission to do this");
                        return;
                    }

                    if (!targetPlayer.getUniqueId().toString().equals(coordinate.uuid.toString())) {
                        targetPlayer.sendMessage("You can change only your coordinates");
                        return;
                    }

                    player.closeInventory();
                    player.performCommand("coordmarker:coords rename-gui " + coordinate.name);
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.PAPER, "Rename").make();
                }
            });
            initialPos++;
        }

        if(targetPlayer.hasPermission("coordmarker.changeitem")) {
            buttons.put(initialPos, new Button() {
                @Override
                public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.fromString(coordinate.item.equals("AIR") ? "COMPASS" : coordinate.item), "Change Item", "Drag and drop item here to change").make();
                }
            });
            initialPos++;
        }

        if(targetPlayer.hasPermission("coordmarker.createglobal")) {
            buttons.put(initialPos, new Button() {
                @Override
                public void onClickedInMenu(Player player, Menu menu, ClickType click) {

                    if (!targetPlayer.hasPermission("coordmarker.createglobal")) {
                        player.sendMessage("You don't have permission to do this");
                        return;
                    }

                    if (!targetPlayer.getUniqueId().toString().equals(coordinate.uuid.toString())) {
                        targetPlayer.sendMessage("You can change only your coordinates");
                        return;
                    }

                    coordinate.global = coordinate.global == 1 ? 0 : 1;
                    CoordMarker.getInstance().getDatabase().saveCoords(coordinate);
                    new CoordsInfoMenu(coordinate, targetPlayer).displayTo(targetPlayer);
                }

                @Override
                public ItemStack getItem() {
                    CompMaterial item;

                    if (coordinate.global == 1)
                        item = CompMaterial.REDSTONE_TORCH;
                    else
                        item = CompMaterial.TORCH;


                    return ItemCreator.of(item, "Visibility", "Allow another users to see this coord\n\nCurrent: " + (coordinate.global == 1 ? "Global " : "Private")).make();
                }
            });
            initialPos++;
        }

        if(targetPlayer.hasPermission("coordmarker.delete")) {
            buttons.put(16, new Button() {
                @Override
                public void onClickedInMenu(Player player, Menu menu, ClickType click) {

                    if (!targetPlayer.hasPermission("coordmarker.delete")) {
                        player.sendMessage("You don't have permission to do this");
                        return;
                    }

                    if (!targetPlayer.getUniqueId().toString().equals(coordinate.uuid.toString())) {
                        targetPlayer.sendMessage("You can change only your coordinates");
                        return;
                    }


                    player.closeInventory();
                    player.performCommand("coordmarker:coords delete " + coordinate.name);
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.LAVA_BUCKET, "&6&lDelete " + coordinate.name).make();
                }
            });
        }

        buttons.put(18, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                if(!targetPlayer.hasPermission("coordmarker.info")){
                    player.sendMessage("You don't have permission to do this");
                    return;
                }

                new CoordsListMenu(targetPlayer, 1, -1).displayTo(player);
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

            if(!targetPlayer.getUniqueId().toString().equals(coordinate.uuid.toString())) {
                targetPlayer.sendMessage("You can change only your coordinates");
                return false;
            }

            if(!targetPlayer.hasPermission("coordmarker.changeitem")){
                targetPlayer.sendMessage("You don't have permission to do this");
                return false;
            }

            coordinate.item = cursor.getType().toString();
            CoordMarker.getInstance().getDatabase().saveCoords(coordinate);

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