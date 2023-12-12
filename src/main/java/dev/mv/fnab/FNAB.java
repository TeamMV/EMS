package dev.mv.fnab;

import dev.mv.engine.game.Game;
import dev.mv.engine.game.WindowStyle;
import dev.mv.engine.utils.misc.Version;
import org.jetbrains.annotations.NotNull;

public class FNAB extends Game {

    @Override
    public void run() {
        initialize();
        manage().mainWindow(
            WindowStyle.fullscreen(),
            WindowStyle.staticSize(),
            WindowStyle.title("Five Nights at BISM"),
            WindowStyle.ups(20)
        );
        manage().getMainWindow().run(new Manager());
    }

    @Override
    public void loadAssets() {

    }

    @Override
    public @NotNull String getGameId() {
        return "fnab";
    }

    @Override
    public @NotNull Version getVersion() {
        return Main.VERSION;
    }

    @Override
    public @NotNull String getName() {
        return "Five Nights at BISM";
    }

    @Override
    public @NotNull ModState getModState() {
        return ModState.NOT_MODDED;
    }
}
