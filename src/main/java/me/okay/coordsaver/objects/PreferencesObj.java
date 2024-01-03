package me.okay.coordsaver.objects;

import java.util.UUID;

public class PreferencesObj {
    public Enums.LEFT_CLICK_ACTION leftClickAction;
    public Enums.DEFAULT_FILTER defaultFilter;
    public Enums.DEFAULT_ORDER defaultOrder;
    public Enums.PROGRESS_MENU_STYLE progressMenuStyle;
    public int privateMode;
    public int dimensionFilter;

    public PreferencesObj() {

    }

    public static PreferencesObj get(String playerName){
        return new PreferencesObj();
    }

}
