package dev.mv.ems.graph;

import dev.mv.engine.gui.components.Element;
import dev.mv.engine.gui.event.EventListener;
import dev.mv.engine.gui.utils.VariablePosition;
import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.DrawContext2D;
import dev.mv.engine.render.shared.Window;
import dev.mv.engine.resources.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GraphPlot extends Element {
    private List<double[]> datasets = new ArrayList<>();
    private List<Color> colors = new ArrayList<>();
    private double[] xAxis;

    public GraphPlot(Window window, int x, int y, int width, int height, Element parent) {
        super(window, x, y, width, height, parent);
    }

    public GraphPlot(Window window, VariablePosition position, Element parent) {
        super(window, position, parent);
    }

    public void setDatasets(List<double[]> datasets) {
        this.datasets = datasets;
        colors.clear();

        Random random = new Random();
        for (int i = 0; i < datasets.size(); i++) {
            colors.add(new Color(random.nextInt(200), random.nextInt(200), random.nextInt(200), 255));
        }
    }

    public void setxAxis(double[] xAxis) {
        this.xAxis = xAxis;
    }

    @Override
    public void draw(DrawContext2D ctx) {

    }

    @Override
    public void attachListener(EventListener eventListener) {

    }
}
