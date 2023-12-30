package me.okay.coordsaver.menu;

import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.objects.CoordsObj;
import org.bukkit.Location;
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

    public CoordsListMenu(Player targetPlayer) {
        List<CoordsObj> coordinates = CoordSaver.getInstance().getDatabase().getCoordsList(targetPlayer.getUniqueId(), 1);


        for (CoordsObj coordinate : coordinates) {
            Button button = new Button() {
                @Override
                public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                    player.teleport(new Location(player.getWorld(), coordinate.x, coordinate.y, coordinate.z, 0, 0));
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.COMPASS, "&6&lTeleport to "+coordinate.name, "UUID: "+coordinate.uuid.toString()+"\nCoords: "+String.format("%s %s %s",coordinate.x, coordinate.y, coordinate.z)).make();
                }
            };


            buttons.put(buttons.size(), button);

        }


        setTitle("&8Main Menu");
        setSize(9 * 6);
    }

    @Override
    public ItemStack getItemAt(int slot) {
        if (buttons.containsKey(slot))
            return buttons.get(slot).getItem();
        else
            return ItemCreator.of(CompMaterial.BLACK_STAINED_GLASS_PANE, " ").make();
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