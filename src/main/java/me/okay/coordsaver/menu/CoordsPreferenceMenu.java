package me.okay.coordsaver.menu;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.objects.Enums;
import me.okay.coordsaver.objects.PreferencesObj;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.*;

public class CoordsPreferenceMenu extends Menu {

    private final Map<Integer, Button> buttons = new HashMap<>();

    @Override
    protected List<Button> getButtonsToAutoRegister() {
        return new ArrayList<>(buttons.values());
    }

    Player targetPlayer;

    public CoordsPreferenceMenu(Player targetPlayer) {
        setTitle("&8Preferences");
        setSize(9 * 3);

        PreferencesObj prefs = PreferencesObj.get(targetPlayer.getUniqueId());

        buttons.put(10, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                if(prefs.leftClickAction.equals(Enums.LEFT_CLICK_ACTION.INFO))
                    prefs.leftClickAction = Enums.LEFT_CLICK_ACTION.TRACK;
                else if(prefs.leftClickAction.equals(Enums.LEFT_CLICK_ACTION.TRACK))
                    prefs.leftClickAction = Enums.LEFT_CLICK_ACTION.TELEPORT;
                else if(prefs.leftClickAction.equals(Enums.LEFT_CLICK_ACTION.TELEPORT))
                    prefs.leftClickAction = Enums.LEFT_CLICK_ACTION.INFO;

                CoordSaver.getInstance().getDatabase().savePreferences(prefs);
                new CoordsPreferenceMenu(player).displayTo(player);
            }

            @Override
            public ItemStack getItem() {
                CompMaterial item = CompMaterial.WRITABLE_BOOK;

                if(prefs.leftClickAction.equals(Enums.LEFT_CLICK_ACTION.TRACK))
                    item = CompMaterial.COMPASS;
                if(prefs.leftClickAction.equals(Enums.LEFT_CLICK_ACTION.TELEPORT))
                    item = CompMaterial.ENDER_PEARL;

                return ItemCreator.of(item, "Left-click action").make();
            }
        });

        buttons.put(11, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                if(prefs.defaultFilter.equals(Enums.DEFAULT_FILTER.ANY))
                    prefs.defaultFilter = Enums.DEFAULT_FILTER.MY;
                else if(prefs.defaultFilter.equals(Enums.DEFAULT_FILTER.MY))
                    prefs.defaultFilter = Enums.DEFAULT_FILTER.GLOBAL;
                else
                    prefs.defaultFilter = Enums.DEFAULT_FILTER.ANY;

                CoordSaver.getInstance().getDatabase().savePreferences(prefs);
                new CoordsPreferenceMenu(player).displayTo(player);
            }

            @Override
            public ItemStack getItem() {
                CompMaterial item = CompMaterial.HOPPER;

                if(prefs.defaultFilter.equals(Enums.DEFAULT_FILTER.MY))
                    item = CompMaterial.DECORATED_POT;
                if(prefs.defaultFilter.equals(Enums.DEFAULT_FILTER.GLOBAL))
                    item = CompMaterial.CAULDRON;

                return ItemCreator.of(item, "Default Filter").make();
            }
        });

        buttons.put(12, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                if(prefs.defaultOrder.equals(Enums.DEFAULT_ORDER.NAME))
                    prefs.defaultOrder = Enums.DEFAULT_ORDER.NAME_REVERSE;
                else if(prefs.defaultOrder.equals(Enums.DEFAULT_ORDER.NAME_REVERSE))
                    prefs.defaultOrder = Enums.DEFAULT_ORDER.VISIBILITY;
                else if(prefs.defaultOrder.equals(Enums.DEFAULT_ORDER.VISIBILITY))
                    prefs.defaultOrder = Enums.DEFAULT_ORDER.VISIBILITY_REVERSE;
                else
                    prefs.defaultOrder = Enums.DEFAULT_ORDER.NAME;

                CoordSaver.getInstance().getDatabase().savePreferences(prefs);
                new CoordsPreferenceMenu(player).displayTo(player);
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.HOPPER_MINECART, "Default Order", "Current: "+prefs.defaultOrder.toString()).make();
            }
        });

        buttons.put(13, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                targetPlayer.sendMessage("AAA");
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.END_CRYSTAL, "Progress-menu style").make();
            }
        });

        buttons.put(14, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                targetPlayer.sendMessage("AAA");
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.LANTERN, "Private Mode").make();
            }
        });

        buttons.put(15, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                targetPlayer.sendMessage("AAA");
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.GRASS_BLOCK, "Dimension Filter").make();
            }
        });

        buttons.put(16, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                new CoordsClearMenu(player).displayTo(player);
             }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.LAVA_BUCKET, "Delete all my coordinates").make();
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
    public ItemStack getItemAt(int slot) {
        if (buttons.containsKey(slot))
            return buttons.get(slot).getItem();
        if ((slot < 10) || (slot > 18) || (slot % 9 == 0) || (slot % 9 == 8))
            return ItemCreator.of(CompMaterial.BLACK_STAINED_GLASS_PANE, " ").make();
        else
            return ItemCreator.of(CompMaterial.AIR, " ").make();
    }
}