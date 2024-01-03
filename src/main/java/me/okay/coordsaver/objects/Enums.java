package me.okay.coordsaver.objects;

public class Enums{
    public enum LEFT_CLICK_ACTION {
        TELEPORT,
        TRACK;

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public enum PROGRESS_MENU_STYLE{
        BOSSBAR,
        ACTIONBAR,
        TABMENU,
        SCOREBOARD;

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public enum DEFAULT_FILTER{
        ANY,
        GLOBAL,
        PRIVATE;

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public enum DEFAULT_ORDER{
        NAME,
        NAME_REVERSE,
        VISIBILITY,
        VISIBILITY_REVERSE;

        @Override
        public String toString() {
            return super.toString();
        }

        public static DEFAULT_ORDER fromString(String from){
            return DEFAULT_ORDER.valueOf(from);
        }
    }

}