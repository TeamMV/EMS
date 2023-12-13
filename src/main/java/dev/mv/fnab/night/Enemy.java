package dev.mv.fnab.night;

import dev.mv.engine.render.shared.DrawContext;

import java.util.Random;

public abstract class Enemy {

    protected Location location;
    protected final long interval;
    protected final int level;
    protected Night night;
    protected long lastMove;

    protected Enemy(long interval, int level, Location location) {
        this.interval = interval;
        this.level = level;
        this.location = location;
        lastMove = System.currentTimeMillis();
    }

    public Location getLocation() {
        return location;
    }

    public int level() {
        return level;
    }

    public long lastMove() {
        return lastMove;
    }

    public long interval() {
        return interval;
    }

    public abstract boolean opportunity();

    public abstract void move();

    public abstract String name();

    public abstract void drawDebug(DrawContext ctx);

    void setNight(Night night) {
        this.night = night;
    }

}
