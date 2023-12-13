package dev.mv.fnab.night;

import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.DrawContext;
import dev.mv.engine.utils.Utils;
import dev.mv.engine.utils.collection.Vec;
import dev.mv.fnab.night.enemies.MissCarr;

import java.util.HashMap;

public class Night {

    public float dim = 0.0f;

    public Vec<Enemy> enemies = new Vec<>();
    public HashMap<Integer, Runnable> changes = new HashMap<>();
    public int level;
    public int time = 12;
    public ComputerState computer = ComputerState.CAMS;
    public PlayerState player = PlayerState.IDLE;
    public CameraLocation cam = CameraLocation.BASKETBALL;
    public VentDoor closedVent = VentDoor.NONE;
    public boolean attack = false;
    public Enemy currentAttack = null;
    public PlayerState blockOn = null;
    public long startTime;

    public boolean dead = false;

    public Night(int level) {
        startTime = System.currentTimeMillis();
        this.level = level;
        switch (level) {
            case 1 -> setupNightOne();
            default -> {}
        }
    }

    void setupNightOne() {
        add(new MissCarr(0));
        changes.put(2, () -> {
            enemies.remove(0);
            add(new MissCarr(2));
        });
    }

    public void add(Enemy enemy) {
        enemy.setNight(this);
        enemies.push(enemy);
    }

    public void death(Enemy enemy) {
        dead = true;
    }

    public void update() {
        long now = System.currentTimeMillis();
        long passed = now - startTime;
        if (passed >= 70000 && time == 12) {
            time = 1;
            Utils.ifNotNull(changes.get(1)).then(Runnable::run);
        }
        else if (passed >= 140000 && time == 1) {
            time = 2;
            Utils.ifNotNull(changes.get(2)).then(Runnable::run);
        }
        else if (passed >= 210000 && time == 2) {
            time = 3;
            Utils.ifNotNull(changes.get(3)).then(Runnable::run);
        }
        else if (passed >= 280000 && time == 3) {
            time = 4;
            Utils.ifNotNull(changes.get(4)).then(Runnable::run);
        }
        else if (passed >= 350000 && time == 4) {
            time = 5;
            Utils.ifNotNull(changes.get(5)).then(Runnable::run);
        }
        else if (passed >= 420000 && time == 5) {
            //special case
            time = 6;
            return;
        }

        if (!attack) {
            for (Enemy enemy : enemies) {
                if (now - enemy.lastMove() >= enemy.interval()) {
                    if (enemy.opportunity()) {
                        enemy.move();
                    }
                }
            }
        }
    }

    public void drawDebug(DrawContext ctx) {
        ctx.text(false, 50, 1000, 40, "Night: " + level);
        ctx.text(false, 50, 960, 40, "Player: " + player);
        ctx.text(false, 50, 920, 40, "Computer: " + computer);
        ctx.text(false, 50, 880, 40, "Cam: " + cam);
        ctx.text(false, 50, 840, 40, "Vent: " + closedVent);
        ctx.text(false, 50, 800, 40, "Time: " + time + "am");
        ctx.text(false, 50, 760, 40, "Dim: " + dim);
        ctx.text(false, 50, 720, 40, "Attack: " + attack);
        ctx.text(false, 50, 680, 40, "Current: " + (currentAttack == null ? "null" : currentAttack.name()));
        ctx.text(false, 50, 640, 40, "Blocked: " + blockOn);
        ctx.text(false, 50, 600, 40, "Dead: " + dead);
        ctx.text(false, 50, 560, 40, "Real time: " + ((System.currentTimeMillis() - startTime) / 1000));
        for (Enemy enemy : enemies) {
            enemy.drawDebug(ctx);
        }
        ctx.color(0, 0, 0, (int) (dim * 255));
        ctx.rectangle(0, 0, 1920, 1080);
        ctx.color(Color.WHITE);
    }

}
