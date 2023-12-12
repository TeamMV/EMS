package dev.mv.fnab.state;

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
}
