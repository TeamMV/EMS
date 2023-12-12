package dev.mv.fnab;

import dev.mv.engine.ApplicationConfig;
import dev.mv.engine.MVEngine;
import dev.mv.engine.utils.misc.Version;

public class Main {

    public static final Version VERSION = Version.parse("v0.1.0");

    public static void main(String[] args) {
        ApplicationConfig config = new ApplicationConfig()
            .setAmountAsyncWorkers(3)
            .setDimension(ApplicationConfig.GameDimension.COMBINED)
            .setName("Five Nights at BISM")
            .setVersion(VERSION)
            .setSimultaneousAudioSources(128)
            .setRenderingApi(ApplicationConfig.RenderingAPI.OPENGL);
        try (MVEngine engine = MVEngine.init(config)) {
            new FNAB().run();
        }
        MVEngine.finish();
    }
}