package io.github.totchi_lagawi.scratchomised_plugin;

import org.eclipse.jetty.util.log.Logger;

public class PluginServerLogger implements Logger {
    private LanguageManager _languageManager;
    private PluginServerLogLevel _logLevel = PluginServerLogLevel.INFO;

    public PluginServerLogger(LanguageManager languageManager) {
        this._languageManager = languageManager;
    }

    @Override
    public String getName() {
        return "Scratchomised Plugin Logger";
    }

    @Override
    public void warn(String msg, Object... args) {
        if (this._logLevel.compareTo(PluginServerLogLevel.WARN) <= 0) {
            System.err.println(this._languageManager.getString("log_prefix") + "[WARN] " + msg);
        }
    }

    @Override
    public void warn(Throwable thrown) {
        if (this._logLevel.compareTo(PluginServerLogLevel.WARN) <= 0) {
            System.err.println(this._languageManager.getString("log_prefix") + "[WARN] "
                    + thrown.getClass().getCanonicalName() + ": " + thrown.getMessage());
        }
    }

    @Override
    public void warn(String msg, Throwable thrown) {
        if (this._logLevel.compareTo(PluginServerLogLevel.WARN) <= 0) {
            System.err.println(this._languageManager.getString("log_prefix") + "[WARN] "
                    + thrown.getClass().getCanonicalName() + ": " + msg);
        }
    }

    @Override
    public void info(String msg, Object... args) {
        if (this._logLevel.compareTo(PluginServerLogLevel.INFO) <= 0) {
            System.out.println(this._languageManager.getString("log_prefix") + msg);
        }
    }

    @Override
    public void info(Throwable thrown) {
        if (this._logLevel.compareTo(PluginServerLogLevel.INFO) <= 0) {
            System.out.println(this._languageManager.getString("log_prefix") + thrown.getClass().getCanonicalName()
                    + ": " + thrown.getMessage());
        }
    }

    @Override
    public void info(String msg, Throwable thrown) {
        if (this._logLevel.compareTo(PluginServerLogLevel.INFO) <= 0) {
            System.out.println(
                    this._languageManager.getString("log_prefix") + thrown.getClass().getCanonicalName() + ": " + msg);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return this._logLevel.compareTo(PluginServerLogLevel.DEBUG) <= 0;
    }

    @Override
    public void setDebugEnabled(boolean enabled) {
        this._logLevel = PluginServerLogLevel.DEBUG;
    }

    @Override
    public void debug(String msg, Object... args) {
        if (this._logLevel.compareTo(PluginServerLogLevel.DEBUG) <= 0) {
            System.out.println(this._languageManager.getString("log_prefix") + "[DEBUG] " + msg);
        }
    }

    @Override
    public void debug(String msg, long value) {
        if (this._logLevel.compareTo(PluginServerLogLevel.DEBUG) <= 0) {
            System.out.println(this._languageManager.getString("log_prefix") + "[DEBUG] " + msg);
        }
    }

    @Override
    public void debug(Throwable thrown) {
        if (this._logLevel.compareTo(PluginServerLogLevel.DEBUG) <= 0) {
            System.out.println(this._languageManager.getString("log_prefix") + "[DEBUG] "
                    + thrown.getClass().getCanonicalName() + ": " + thrown.getMessage());
        }
    }

    @Override
    public void debug(String msg, Throwable thrown) {
        if (this._logLevel.compareTo(PluginServerLogLevel.DEBUG) <= 0) {
            System.out.println(this._languageManager.getString("log_prefix") + "[DEBUG] "
                    + thrown.getClass().getCanonicalName() + ": " + msg);
        }
    }

    @Override
    public Logger getLogger(String name) {
        return this;
    }

    @Override
    public void ignore(Throwable ignored) {
    }

}
