package com.caiocinel.coordmarker.objects;

import com.caiocinel.coordmarker.CoordMarker;

import java.util.UUID;

public class PreferencesObj {
    public UUID uuid;
    public Enums.LEFT_CLICK_ACTION leftClickAction;
    public Enums.DEFAULT_FILTER defaultFilter;
    public Enums.DEFAULT_ORDER defaultOrder;
    public Enums.PROGRESS_MENU_STYLE progressMenuStyle;
    public int privateMode;
    public int dimensionFilter;

    public PreferencesObj(UUID uuid, Enums.LEFT_CLICK_ACTION leftClickAction, Enums.DEFAULT_FILTER defaultFilter, Enums.DEFAULT_ORDER defaultOrder, Enums.PROGRESS_MENU_STYLE progressMenuStyle, int privateMode, int dimensionFilter) {
        this.uuid = uuid;
        this.leftClickAction = leftClickAction;
        this.defaultFilter = defaultFilter;
        this.defaultOrder = defaultOrder;
        this.progressMenuStyle = progressMenuStyle;
        this.privateMode = privateMode;
        this.dimensionFilter = dimensionFilter;
    }

    public PreferencesObj(String uuid, String leftClickAction, String defaultFilter, String defaultOrder, String progressMenuStyle, int privateMode, int dimensionFilter) {
        this.uuid = UUID.fromString(uuid);
        this.leftClickAction = Enums.LEFT_CLICK_ACTION.valueOf(leftClickAction);
        this.defaultFilter = Enums.DEFAULT_FILTER.valueOf(defaultFilter);
        this.defaultOrder = Enums.DEFAULT_ORDER.valueOf(defaultOrder);
        this.progressMenuStyle = Enums.PROGRESS_MENU_STYLE.valueOf(progressMenuStyle);
        this.privateMode = privateMode;
        this.dimensionFilter = dimensionFilter;
    }

    public static PreferencesObj get(UUID playerUUID){
        return CoordMarker.getInstance().getDatabase().getPreferences(playerUUID);
    }

}
