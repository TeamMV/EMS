package dev.mv.ems.graph;

import dev.mv.engine.gui.components.Element;
import dev.mv.engine.gui.event.EventListener;
import dev.mv.engine.gui.utils.VariablePosition;
import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.DrawContext2D;
import dev.mv.engine.render.shared.Window;
import dev.mv.engine.render.shared.font.BitmapFont;
import dev.mv.engine.resources.R;
import dev.mv.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GraphPlot extends Element {
    private List<double[]> datasets = new ArrayList<>();
    private List<Color> colors = new ArrayList<>();
    private double[] xAxis = new double[0];

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
        if (datasets == null || datasets.size() <= 0) {
            ctx.color(Color.BLACK);
            ctx.text(false, getX(), getY(), 50, "No Values");
            return;
        }
        int textSize = 15;

        double max = 0, min = 0;
        for (double[] dataset : datasets) {
            for (int i = 0; i < dataset.length; i++) {
                if (Double.isNaN(dataset[i])) continue;
                max = Math.max(dataset[i], max);
                min = Math.min(dataset[i], min);
            }
        }

        BitmapFont font = R.fonts.get("default");
        ctx.font(font);
        int maxTextWidth = 0;

        for (int i = 0; i < 10; i++) {
            String value = String.valueOf(Utils.map((float) i, (float) 0, 10F, (float) min, (float) max));
            int textWidth = font.getWidth(value, textSize);
            maxTextWidth = Math.max(maxTextWidth, textWidth);
        }
        ctx.color(Color.BLACK);

        for (int i = 0; i < 10; i++) {
            String value = String.valueOf(Utils.map((float) i, (float) 0, 10F, (float) min, (float) max));
            int textWidth = font.getWidth(value, textSize);
            int y = (int) Utils.map(i, 0, 10, getY() + textSize + 5, getHeight() + getY());
            ctx.text(false, getX() + maxTextWidth - textWidth, y - textSize / 2, textSize, value);

            int linesX = getX() + maxTextWidth + 5;
            ctx.line(linesX, y, getX() + getWidth(), y, 1);

            int y0 = (int) Utils.map(0f, (float) min, (float) max, getY() + textSize + 5, getHeight() + getY());
            ctx.line(linesX, y0, getX() + getWidth(), y0, 5);
        }

        for (int i = 0; i < 10; i++) {
            int x = (int) Utils.map(i, 0, 10, getX() + maxTextWidth + 5, getX() + getWidth());
            String value = String.valueOf(xAxis[(int) Utils.map(i, 0, 10, 0, xAxis.length)]);
            int textWidth = font.getWidth(value, textSize);

            ctx.text(false, x - textWidth / 2, getY(), textSize, value);

            ctx.line(x, getY() + textSize + 5, x, getY() + getHeight(), i == 0 ? 5 : 1);
        }

        int colorI = 0;
        for (double[] dataset : datasets) {
            Color color = colors.get(colorI++);
            for (int i = 0; i < dataset.length - 1; i++) {
                if (Double.isNaN(dataset[i]) || Double.isNaN(dataset[i + 1])) continue;
                int x = (int) Utils.map(i, 0, dataset.length, getX() + maxTextWidth + 5, getX() + getWidth());
                int xn = (int) Utils.map(i + 1, 0, dataset.length, getX() + maxTextWidth + 5, getX() + getWidth());
                int y = (int) Utils.map((float) dataset[i], (float) min, (float) max, getY() + textSize + 5, getY() + getHeight());
                int yn = (int) Utils.map((float) dataset[i + 1], (float) min, (float) max, getY() + textSize + 5, getY() + getHeight());

                ctx.color(color);
                ctx.line(x, y, xn, yn, 3);
            }
        }
    }

    @Override
    public void attachListener(EventListener eventListener) {

    }
}
