package dev.mv.fnab.state;

import dev.mv.fnab.night.Night;

public enum GlobalState {

    MENU,
    NIGHT,
    MINIGAME;

    public Object value;

    public MenuState menu() {
        if (this == MENU) {
            return (MenuState) value;
        }
        return null;
    }

    public Night night() {
        if (this == NIGHT) {
            return (Night) value;
        }
        return null;
    }
}
