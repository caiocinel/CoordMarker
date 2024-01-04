package me.okay.coordsaver.menu;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.objects.CoordsObj;
import org.bukkit.Location;
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

public class CoordsClearMenu extends Menu {

    private final Map<Integer, Button> buttons = new HashMap<>();

    @Override
    protected List<Button> getButtonsToAutoRegister() {
        return new ArrayList<>(buttons.values());
    }

    Player targetPlayer;

    public CoordsClearMenu(Player targetPlayer) {

        this.targetPlayer = targetPlayer;

        setTitle("Delete all my coordinates");
        setSize(9 * 3);

        buttons.put(11, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                new CoordsListMenu(targetPlayer, 1).displayTo(player);
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.RED_WOOL, "Cancel").make();
            }
        });

        buttons.put(15, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                CoordSaver.getInstance().getDatabase().clearCoords(player.getUniqueId());
                new CoordsListMenu(targetPlayer, 1).displayTo(player);
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.GREEN_WOOL, "Delete All My Coordinates").make();
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