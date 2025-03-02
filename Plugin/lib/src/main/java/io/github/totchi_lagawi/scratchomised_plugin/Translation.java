package io.github.totchi_lagawi.scratchomised_plugin;

import java.util.NoSuchElementException;

/**
 * A class holding a translation, used to type-safely parse the YAML
 * representation of a translation
 */
public class Translation {
    public String name;
    public String log_prefix;
    public Menus menus;
    public Errors errors;

    public static class Menus {
        public MenuServer server;
        public String export;

        public static class MenuServer {
            public String start;
            public String stop;
        }
    }

    public static class Errors {
        public String error;
        public ServerErrors server;

        public static class ServerErrors {
            public String socket_binding;
            public String null_server;
        }
    }

    /**
     * Get a translation from an identifier
     * 
     * @param string the identifier of the translation to get
     * @return the translation
     * @throws NoSuchElementException if the request translation was not found
     */
    public String getTranslation(String id) throws NoSuchElementException, IllegalAccessException {
        try {
            Object object = this;
            for (String field : id.split("\\.")) {
                object = object.getClass().getDeclaredField(field).get(object);
            }
            return (String) object;
        } catch (NoSuchFieldException | ClassCastException ex) {
            throw new NoSuchElementException("The requested translation (" + id + ") was not found");
        }
    }
}