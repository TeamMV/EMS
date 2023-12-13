package dev.mv.fnab;

import dev.mv.engine.ApplicationLoop;
import dev.mv.engine.MVEngine;
import dev.mv.engine.gui.Gui;
import dev.mv.engine.gui.elements.GuiButton;
import dev.mv.engine.gui.style.BorderStyle;
import dev.mv.engine.gui.style.value.GuiValueJust;
import dev.mv.engine.gui.style.value.GuiValueParentPercentage;
import dev.mv.engine.gui.style.value.GuiValuePercentage;
import dev.mv.engine.input.Input;
import dev.mv.engine.input.InputCollector;
import dev.mv.engine.input.processors.GuiInputProcessor;
import dev.mv.engine.input.processors.MainInputProcessor;
import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.DrawContext;
import dev.mv.engine.render.shared.Window;
import dev.mv.engine.resources.R;
import dev.mv.fnab.night.Night;
import dev.mv.fnab.state.GlobalState;
import dev.mv.fnab.state.MenuState;
import dev.mv.fnab.state.Save;

public class Manager implements ApplicationLoop {

    Save save;

    DrawContext ctx;
    InputCollector input;

    GlobalState state;
    Gui gui;

    public Manager() {
        state = GlobalState.MENU;
        state.value = MenuState.TITLE;
    }

    @Override
    public void start(MVEngine engine, Window window) throws Exception {
        save = engine.getGame().getGameDirectory().getFileAsObject("save.dat", new Save.SaveSaver());

        engine.createResources();

        ctx = new DrawContext(window);
        ctx.font(R.font.get("mvengine.default"));
        input = new InputCollector(window);
        input.start();

        gui = new Gui();

        GuiButton newGame = new GuiButton();
        newGame.setText("New Game");
        newGame.moveTo(100, (int) (window.getHeight() * 0.6));
        newGame.style.text.size = new GuiValueJust<>(75);
        newGame.style.border.color = new GuiValueJust<>(new Color(0));
        newGame.style.text.color = new GuiValueJust<>(new Color(255, 255, 255, 255));
        newGame.style.backgroundColor = new GuiValueJust<>(new Color(0));

        newGame.onclick((e, b, x, y) -> {
            state = GlobalState.NIGHT;
            state.value = new Night(1);
        });

        GuiButton cont = new GuiButton();
        cont.setText("Continue");
        cont.moveTo(100, (int) (window.getHeight() * 0.5));
        cont.style.text.size = new GuiValueJust<>(75);
        cont.style.border.color = new GuiValueJust<>(new Color(0));
        cont.style.text.color = new GuiValueJust<>(new Color(255, 255, 255, 255));
        cont.style.backgroundColor = new GuiValueJust<>(new Color(0));

        gui.addElement(newGame);
        gui.addElement(cont);

        GuiInputProcessor.addGui(gui);
    }

    @Override
    public void update(MVEngine engine, Window window) throws Exception {
        if (Input.keys[Input.KEY_ESCAPE]) {
            window.close();
        }
        if (state == GlobalState.NIGHT) {
            Night night = state.night();
            night.update();
        }
    }

    @Override
    public void draw(MVEngine engine, Window window) throws Exception {
        if (state == GlobalState.MENU) {
            MenuState menu = state.menu();
            gui.draw(ctx);
        }
        else if (state == GlobalState.NIGHT) {
            Night night = state.night();
            night.drawDebug(ctx);
        }
    }

    @Override
    public void exit(MVEngine engine, Window window) throws Exception {
        engine.getGame().getGameDirectory().saveFileObject("save.dat", save, new Save.SaveSaver());
    }
}
