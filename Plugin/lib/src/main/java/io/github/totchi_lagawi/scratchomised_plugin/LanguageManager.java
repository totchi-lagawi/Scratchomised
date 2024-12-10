package io.github.totchi_lagawi.scratchomised_plugin;

public class LanguageManager {
    public static String getString(String id, String lang) {
        if (lang == null) {
            lang = getLang();
        }

        switch (id) {
            case "plugin.menu": {
                return "Scratchomised";
            }

            case "export.name": {
                return "Export...";
            }

            case "server.name.start": {
                return "Start server";
            }

            case "server.name.stop": {
                return "Stop server";
            }

            case "error.error": {
                return "Error";
            }

            case "error.server.socket_bind": {
                return "Error while binding server socket.";
            }

            case "error.server.null_server": {
                return "The server couldn't be instanciated, please rerun SweetHome3D.";
            }

            case "log_prefix": {
                return "[Scratchomised] - ";
            }
        }

        return null;
    }

    public static String getLang() {
        // TODO
        return null;
    }
}
