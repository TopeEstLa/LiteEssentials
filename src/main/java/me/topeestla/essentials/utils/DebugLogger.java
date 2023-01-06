package me.topeestla.essentials.utils;

import me.topeestla.essentials.LiteEssentials;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class DebugLogger extends Logger {

    private final String format;
    private final boolean enable;

    public DebugLogger(LiteEssentials essentials) {
        super(essentials.getClass().getCanonicalName(), null);
        this.enable = essentials.getConfig().getBoolean("debug");
        this.format = "LiteEssentials-Debug [" + essentials.getDescription().getVersion() + "] : ";
        this.setParent(essentials.getServer().getLogger());
        this.setLevel(Level.ALL);
    }

    @Override
    public void log(LogRecord record) {
        if (this.enable) {
            record.setMessage(this.format + record.getMessage());
            super.log(record);
        }
    }
}
