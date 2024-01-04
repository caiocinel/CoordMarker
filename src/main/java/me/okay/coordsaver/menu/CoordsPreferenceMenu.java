package me.okay.coordsaver.menu;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.objects.Enums;
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

        buttons.put(10, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                targetPlayer.sendMessage("Track");
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.ENDER_PEARL, "Left-click action").make();
            }
        });

        buttons.put(11, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                targetPlayer.sendMessage("AAA");
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.HOPPER, "Default Filter").make();
            }
        });

        buttons.put(12, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                if(Enums.DEFAULT_ORDER.valueOf("VISIBILITY").equals(Enums.DEFAULT_ORDER.VISIBILITY))
                    player.sendMessage("TRUE");
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.HOPPER_MINECART, "Default Order").make();
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