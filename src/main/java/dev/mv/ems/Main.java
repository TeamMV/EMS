package dev.mv.ems;

import dev.mv.engine.ApplicationConfig;
import dev.mv.engine.MVEngine;
import dev.mv.engine.render.WindowCreateInfo;
import dev.mv.engine.render.shared.Window;
import dev.mv.utils.misc.Version;

public class Main {
    public static void main(String[] args) {
        try (MVEngine engine = MVEngine.init(new ApplicationConfig()
                .setDimension(ApplicationConfig.GameDimension.ONLY_2D)
                .setName("EMS")
                .setVersion(Version.parse("1.0.0"))
        )) {

            engine.setExceptionHandler(Throwable::printStackTrace);
            WindowCreateInfo info = new WindowCreateInfo();
            info.title = "Easy Model Simulation";
            info.decorated = true;
            info.vsync = true;
            info.width = 800;
            info.height = 600;
            info.resizeable = true;

            Window window = engine.createWindow(info);
            window.run(new Loop());
        }
    }
}
