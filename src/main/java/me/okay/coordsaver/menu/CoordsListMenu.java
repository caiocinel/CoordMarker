package me.okay.coordsaver.menu;

import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.objects.CoordsObj;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonMenu;
import org.mineacademy.fo.menu.button.StartPosition;
import org.mineacademy.fo.menu.button.annotation.Position;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoordsListMenu extends Menu {

    private final Map<Integer, Button> buttons = new HashMap<>();

    @Override
    protected List<Button> getButtonsToAutoRegister() {
        return new ArrayList<>(buttons.values());
    }

    private int getNextAvailableChestPosition(int curPosition){

        if((curPosition < 10) || (curPosition > 44) || (curPosition % 9 == 0) || (curPosition % 9 == 8))
            return getNextAvailableChestPosition(curPosition + 1);

        return curPosition;
    }

    public CoordsListMenu(Player targetPlayer, int page) {
        List<CoordsObj> coordinates = CoordSaver.getInstance().getDatabase().getCoordsList(targetPlayer.getUniqueId(), page);


        int currentPos = 10;

        for (CoordsObj coordinate : coordinates) {
            Button button = new Button() {
                @Override
                public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                    player.teleport(new Location(player.getWorld(), coordinate.x, coordinate.y, coordinate.z, 0, 0));
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.fromString(coordinate.item), "&6&lTeleport to "+coordinate.name, "Player: "+coordinate.playerName+"\nCoords: "+String.format("%s %s %s",coordinate.x, coordinate.y, coordinate.z)).make();
                }
            };


            currentPos = getNextAvailableChestPosition(currentPos);
            buttons.put(currentPos, button);

            currentPos++;

        }

        int pageCount = (int) Math.ceil(CoordSaver.getInstance().getDatabase().getCoordsCount(targetPlayer.getUniqueId()) / (double) CoordSaver.COORDS_PER_PAGE);

        if(pageCount > page)
            buttons.put(53, new Button() {
                @Override
                public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                    new CoordsListMenu(targetPlayer, page+1).displayTo(player);
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.SPECTRAL_ARROW, "Go to page "+(page+1)).make();
                }
            });

        if((page > 1) && (pageCount > 1))
            buttons.put(45, new Button() {
                @Override
                public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                    new CoordsListMenu(targetPlayer, page-1).displayTo(player);
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.SPECTRAL_ARROW, "Go to page "+(page-1)).make();
                }
            });

        buttons.put(48, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                if(click.isRightClick())
                    player.performCommand("coords create-gui true");
                else
                    player.performCommand("coords create-gui");
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.HOPPER, "Public Only", "Create a New Coord in current location\nRight click to create a Public coord").make();
            }
        });

        buttons.put(49, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                if(click.isRightClick())
                    player.performCommand("coords create-gui true");
                else
                    player.performCommand("coords create-gui");


            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.GREEN_WOOL, "New Coord", "Create a New Coord in current location\nRight click to create a Public coord").make();
            }
        });

        setTitle("&8Main Menu");
        setSize(9 * 6);
    }

    @Override
    public ItemStack getItemAt(int slot) {
        if (buttons.containsKey(slot))
            return buttons.get(slot).getItem();
        if ((slot < 10) || (slot > 44) || (slot % 9 == 0) || (slot % 9 == 8))
            return ItemCreator.of(CompMaterial.BLACK_STAINED_GLASS_PANE, " ").make();
        else
            return ItemCreator.of(CompMaterial.AIR, " ").make();
    }

    private class PreferencesMenu extends Menu {

        @Position(start = StartPosition.CENTER, value = -1)
        private final Button clearInventoryButton;

        @Position(start = StartPosition.CENTER, value = 1)
        private final Button refillHealthButton;

        PreferencesMenu() {
            super(CoordsListMenu.this);

            setTitle("&8Preferences");
            setSize(9 * 3);

            this.clearInventoryButton = new Button() {
                @Override
                public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                    player.getInventory().clear();
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.LAVA_BUCKET, "&6&lClear Inventory").make();
                }
            };

            this.refillHealthButton = new Button() {
                @Override
                public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                    player.setHealth(player.getMaxHealth());

                    restartMenu("&2Refilled health!");
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.GOLDEN_APPLE,
                            "&6&lRefill Health",
                            "",
                            "&fCurrent Health: &c" + Math.round(getViewer().getHealth()),
                            "",
                            "Click to refill.").make();
                }
            };
        }

        @Override
        protected void onPostDisplay(Player viewer) {

            animate(20, new MenuRunnable() {
                boolean toggle = true;

                @Override
                public void run() {
                    setItem(getCenterSlot(), ItemCreator.of(toggle ? CompMaterial.RED_WOOL : CompMaterial.BLUE_WOOL).make());

                    toggle = !toggle;
                }
            });
        }

        @Override
        protected String[] getInfo() {
            return new String[]{
                    "Click bucket to clear your inventory.",
                    "",
                    "Click apple to refill your health."
            };
        }
    }



}