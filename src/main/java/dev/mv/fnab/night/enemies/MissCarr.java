package dev.mv.fnab.night.enemies;

import dev.mv.engine.render.shared.DrawContext;
import dev.mv.fnab.night.Enemy;
import dev.mv.fnab.night.Location;
import dev.mv.fnab.night.PlayerState;

import java.util.Random;

public class MissCarr extends Enemy {

    Random random = new Random(System.currentTimeMillis());
    int progress;
    final static int neededProgress = 2;
    int lastRoll = 0;

    public MissCarr(int level) {
        super(3200, level, Location.SCI3);
    }

    @Override
    public boolean opportunity() {
        if (location == Location.SCI1) {
            if (night.player == PlayerState.COMPUTER)
            lastRoll = random.nextInt(2);
            return lastRoll == 0;
        }
        lastMove = System.currentTimeMillis();
        lastRoll = random.nextInt(21);
        return level > lastRoll;
    }

    @Override
    public void move() {
        switch (location) {
            case M2 -> {
                location = Location.VENT_M2;
                progress = 0;
            }
            case SPA -> {
                location = Location.VENT_SPA;
                progress = 0;
            }
            case SCI2 -> location = Location.OUTSIDE;
            case SCI_HALLWAY -> location = Location.SCI2;
            case SCI3 -> {
                progress = 0;
                switch (random.nextInt(4)) {
                    case 0, 1 -> location = Location.VENT_SCI3;
                    case 2 -> location = Location.SCI_HALLWAY;
                    case 3 -> location = Location.H3;
                }
            }
            case PRIMARY -> {
                location = Location.VENT_PRIMARY;
                progress = 0;
            }
            case H1 -> location = Location.M2;
            case H2 -> location = Location.H1;
            case H3 -> {
                switch (random.nextInt(3)) {
                    case 0, 1 -> location = Location.OUTSIDE;
                    case 2 -> location = Location.H4;
                }
            }
            case H4 -> location = Location.PRIMARY;
            case OUTSIDE -> {
                switch (random.nextInt(10)) {
                    case 0 -> location = Location.SCI1;
                    case 1, 2, 3, 4 -> location = Location.H2;
                    default -> {}
                }
            }
            case VENT_SCI1 -> location = Location.SCI1;
            case VENT_M2 -> {
                progress++;
                if (progress >= neededProgress) {
                    location = Location.VENT_SPA;
                    progress = 0;
                }
            }
            case VENT_SPA, VENT_PRIMARY, VENT_SCI3 -> {
                progress++;
                if (progress >= neededProgress) {
                    location = Location.VENT_SCI1;
                    progress = 0;
                }
            }
            case SCI1 -> {
                if (!night.attack) {
                    night.attack = true;
                    night.currentAttack = this;
                    night.blockOn = PlayerState.STUDYING;
                    new Thread(() -> {
                        try {
                            Thread.sleep(2500 - (level * 100L));
                            if (night.player == PlayerState.STUDYING) {
                                Thread.sleep(3500);
                                location = Location.SCI3;
                                float dim = 0.0f;
                                while (dim < 1.0f) {
                                    night.dim = dim;
                                    dim += 0.05f;
                                    Thread.sleep(2);
                                }
                                night.currentAttack = null;
                                night.blockOn = null;
                                while (dim > 0.0f) {
                                    night.dim = dim;
                                    dim -= 0.01f;
                                    Thread.sleep(20);
                                }
                                night.dim = 0.0f;
                                night.attack = false;
                            }
                            else {
                                Thread.sleep(2500);
                                night.currentAttack = null;
                                night.blockOn = null;
                                location = Location.DEATH;
                                night.death(this);
                            }
                        } catch (InterruptedException ignored) {}
                        lastMove = System.currentTimeMillis();
                    }).start();
                }
            }
            case DEATH -> night.death(this);
            default -> location = Location.SCI3;
        }
    }

    @Override
    public String name() {
        return "Miss Carr";
    }

    @Override
    public void drawDebug(DrawContext ctx) {
        ctx.text(false, 1400, 1000, 40, "Miss Carr");
        ctx.text(false, 1400, 960, 40, "Location: " + location);
        ctx.text(false, 1400, 920, 40, "Progress: " + progress);
        ctx.text(false, 1400, 880, 40, "Level: " + level);
        ctx.text(false, 1400, 840, 40, "Interval: " + interval);
        ctx.text(false, 1400, 800, 40, "Last move: " + lastMove);
        ctx.text(false, 1400, 760, 40, "Last roll: " + lastRoll);
    }
}
