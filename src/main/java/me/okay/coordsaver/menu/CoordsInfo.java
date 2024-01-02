package me.okay.coordsaver.menu;

import com.palmergames.bukkit.towny.object.Coord;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.okay.coordsaver.CoordSaver;
import me.okay.coordsaver.objects.CoordsObj;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.model.MenuClickLocation;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.nbt.NBT;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoordsInfo extends Menu {

    private final Map<Integer, Button> buttons = new HashMap<>();

    @Override
    protected List<Button> getButtonsToAutoRegister() {
        return new ArrayList<>(buttons.values());
    }

    CoordsObj coordinate;
    Player targetPlayer;

    public CoordsInfo(CoordsObj coordinate, Player targetPlayer) {

        this.coordinate = coordinate;
        this.targetPlayer = targetPlayer;

        setTitle("&8Coordinate "+coordinate.name);
        setSize(9 * 3);


        buttons.put(10, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                player.teleport(new Location(player.getWorld(), coordinate.x, coordinate.y, coordinate.z, 0, 0));
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.ENDER_PEARL, "Teleport").make();
            }
        });

        buttons.put(11, new Button() {
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

        buttons.put(12, new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                CoordSaver.getInstance().getLogger().info("Clicked");
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.fromString(coordinate.item.equals("AIR") ? "COMPASS" : coordinate.item), "Change Item", "Drag and drop item here to change").make();
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
    }


    @Override
    protected boolean isActionAllowed(MenuClickLocation location, int slot, @Nullable ItemStack clicked, @Nullable ItemStack cursor, InventoryAction action) {

        CoordSaver.getInstance().getLogger().info("Slot: "+slot);
        CoordSaver.getInstance().getLogger().info("Action: "+action.toString());
        CoordSaver.getInstance().getLogger().info("Location : "+location.toString());

        if(clicked != null)
            CoordSaver.getInstance().getLogger().info("Clicked: "+clicked.getType().toString());

        if(cursor != null)
            CoordSaver.getInstance().getLogger().info("Cursor: "+cursor.getType().toString());


        if(location.equals(MenuClickLocation.PLAYER_INVENTORY))
            return true;

        if(location.equals(MenuClickLocation.MENU) && cursor == null)
            return false;

        if(location.equals(MenuClickLocation.MENU) && slot == 12 && action == InventoryAction.SWAP_WITH_CURSOR) {
            coordinate.item = cursor.getType().toString();
            CoordSaver.getInstance().getDatabase().saveCoords(coordinate);

            new CoordsInfo(coordinate, targetPlayer).displayTo(targetPlayer);
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