package dev.mv.ems;

import dev.mv.ems.graph.GraphPlot;
import dev.mv.ems.parser.Parser;
import dev.mv.ems.parser.ast.Statement;
import dev.mv.ems.runtime.Program;
import dev.mv.ems.runtime.Runtime;
import dev.mv.ems.sim.Simulation;
import dev.mv.engine.MVEngine;
import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.files.Directory;
import dev.mv.engine.files.FileManager;
import dev.mv.engine.gui.components.Button;
import dev.mv.engine.gui.components.Element;
import dev.mv.engine.gui.components.InputBox;
import dev.mv.engine.gui.event.ClickListener;
import dev.mv.engine.render.shared.Window;
import dev.mv.engine.resources.R;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Editor {
    private Simulation simulation = new Simulation("sim1");

    void setup(MVEngine engine, Window window, GraphPlot gp) {
        Button input = R.guis.get("default").findGui("main").getRoot().findElementById("input");
        input.attachListener(new ClickListener() {
            @Override
            public void onCLick(Element element, int i) {

            }

            @Override
            public void onRelease(Element element, int i) {
                File txt = getSimFile();
                try {
                    Desktop.getDesktop().edit(txt);
                } catch (IOException e) {
                    Exceptions.send(e);
                }
            }
        });

        InputBox repeat = R.guis.get("default").findGui("main").getRoot().findElementById("repeat");
        InputBox xAxis = R.guis.get("default").findGui("main").getRoot().findElementById("xAxis");
        InputBox yAxis = R.guis.get("default").findGui("main").getRoot().findElementById("yAxis");

        Button run = R.guis.get("default").findGui("main").getRoot().findElementById("run");
        run.attachListener(new ClickListener() {
            @Override
            public void onCLick(Element element, int i) {

            }

            @Override
            public void onRelease(Element element, int i) {
                File txt = getSimFile();
                Path path = txt.toPath();
                try {
                    String code = String.join("\n", Files.readAllLines(path));
                    int repeatTimes = Integer.parseInt(repeat.getText().isEmpty() ? "100" : repeat.getText());
                    String x = xAxis.getText();
                    String[] y = Arrays.stream(yAxis.getText().split(",")).map(String::trim).toArray(String[]::new);

                    Parser parser = new Parser(code);
                    Program program = parser.parse();
                    for (Statement stmt : program.stmts) {
                        System.out.print(stmt);
                    }

                    Runtime runtime = new Runtime(program);
                    Map<String, double[]> result = runtime.run(repeatTimes, true);

                    double[][] datasets = new double[y.length][];
                    for (int j = 0; j < y.length; j++) {
                        datasets[j] = result.get(y[j]);
                    }

                    System.out.println(Arrays.deepToString(datasets));

                    List<double[]> datas = new ArrayList<>(Arrays.asList(datasets));

                    gp.setDatasets(datas);
                    gp.setxAxis(result.get(x));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private File getSimFile() {
        Directory dir = FileManager.getDirectory("EMS");
        return dir.getFile(this.simulation.getName() + ".txt");
    }
}
