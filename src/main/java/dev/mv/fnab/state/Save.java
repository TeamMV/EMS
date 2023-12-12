package dev.mv.fnab.state;

import dev.mv.engine.files.Savable;
import dev.mv.engine.files.Saver;

public class Save implements Savable<Save> {

    // Save state
    public byte night;
    public boolean completed;
    public boolean six;
    public boolean ultimate;

    // Challenges completed
    public boolean nightmare;

    public Save() {
        night = 1;
    }

    @Override
    public Saver<Save> getSaver() {
        return null;
    }

    public static class SaveSaver implements Saver<Save> {

        @Override
        public Save load(byte[] bytes) {
            if (bytes == null) return new Save();
            if (bytes.length != 5) return new Save();
            Save save = new Save();
            save.night = bytes[0];
            save.completed = bytes[1] != 0;
            save.six = bytes[2] != 0;
            save.ultimate = bytes[3] != 0;
            save.nightmare = bytes[4] != 0;
            return save;
        }

        @Override
        public byte[] save(Save save) {
            byte[] bytes = new byte[5];
            bytes[0] = save.night;
            bytes[1] = (byte) (save.completed ? 1 : 0);
            bytes[2] = (byte) (save.six ? 1 : 0);
            bytes[3] = (byte) (save.ultimate ? 1 : 0);
            bytes[4] = (byte) (save.nightmare ? 1 : 0);
            return bytes;
        }
    }
}
