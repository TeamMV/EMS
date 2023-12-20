package dev.mv.ems;

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
import java.util.stream.Collectors;

public class Editor {
    private Simulation simulation = new Simulation("sim1");

    void setup(MVEngine engine, Window window) {
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
