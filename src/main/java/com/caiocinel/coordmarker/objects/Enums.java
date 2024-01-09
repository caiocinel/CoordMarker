package com.caiocinel.coordmarker.objects;

public class Enums{

    public enum CommandResult {
        SUCCESS,
        USAGE_FAILURE,
        PERMISSION_FAILURE;

        public boolean isSuccess() {
            return this == SUCCESS;
        }



    }


    public enum LEFT_CLICK_ACTION {
        INFO,
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
        MY;

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
    }

}