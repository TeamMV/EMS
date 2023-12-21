package dev.mv.ems;

import dev.mv.ems.graph.GraphPlot;
import dev.mv.ems.parser.Parser;
import dev.mv.ems.parser.ast.Statement;
import dev.mv.ems.runtime.Program;
import dev.mv.ems.runtime.Runtime;
import dev.mv.engine.ApplicationLoop;
import dev.mv.engine.MVEngine;
import dev.mv.engine.gui.GuiRegistry;
import dev.mv.engine.gui.components.layouts.VerticalLayout;
import dev.mv.engine.gui.pages.Page;
import dev.mv.engine.gui.paging.Pager;
import dev.mv.engine.gui.paging.transitions.LinearShiftTransition;
import dev.mv.engine.gui.parsing.GuiConfig;
import dev.mv.engine.gui.theme.Theme;
import dev.mv.engine.gui.utils.VariablePosition;
import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.DrawContext2D;
import dev.mv.engine.render.shared.Window;
import dev.mv.engine.render.shared.font.BitmapFont;
import dev.mv.engine.resources.R;
import dev.mv.engine.resources.ResourceLoader;
import dev.mv.utils.Utils;
import dev.mv.utils.generic.pair.Pair;

import java.util.*;

public class Loop implements ApplicationLoop {
    private DrawContext2D ctx2D;
    private Editor editor = new Editor();
    private GraphPlot gp;

    @Override
    public void start(MVEngine mvEngine, Window window) throws Exception {
        ctx2D = new DrawContext2D(window);

        ResourceLoader.markLayout("main", "main.xml");
        ResourceLoader.markTheme("main", "main.xml");
        ResourceLoader.markPage("main", "main.xml");
        ResourceLoader.markFont("default", "/fonts/default.png", "/fonts/default.fnt");
        ResourceLoader.load(mvEngine, new GuiConfig("/gui/guiConfig.xml"));

        Page main = R.pages.get("main");
        GuiRegistry registry = main.getRegistry();
        registry.applyRenderer(ctx2D);
        Pager pager = main.getPager();
        pager.open("main");
        Theme theme = R.themes.get("main");
        BitmapFont font = R.fonts.get("default");
        ctx2D.font(font);
        System.out.println("font: " + font);
        theme.setFont(font);
        registry.applyTheme(theme);

        gp = new GraphPlot(window, VariablePosition.getPosition("100px", "100px", "100%", "400px"), null);

        VerticalLayout layout = registry.findGui("main").getRoot().findElementById("mainVert");
        layout.addElement(gp);

        editor.setup(mvEngine, window, gp);
    }

    @Override
    public void update(MVEngine mvEngine, Window window) throws Exception {

    }

    @Override
    public void draw(MVEngine mvEngine, Window window) throws Exception {
        ctx2D.color(255, 255, 255, 255);
        ctx2D.rectangle(0, 0, window.getWidth(), window.getHeight());

        gp.setWidth(window.getWidth());

        R.guis.get("default").renderGuis();
    }

    @Override
    public void exit(MVEngine mvEngine, Window window) throws Exception {

    }
}
