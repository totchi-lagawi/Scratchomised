package io.github.totchi_lagawi.scratchomised_plugin;

import java.util.Arrays;
import java.util.NoSuchElementException;

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
        System.out.println("Getting the translation for " + id);
        System.out.println("Resulting array : " + Arrays.toString(id.split("\\.")));
        try {
            Object object = this;
            System.out.println("Base object : " + object);
            for (String field : id.split("\\.")) {
                object = object.getClass().getDeclaredField(field).get(object);
                System.out.println("Object is now : " + object + " of type " + object.getClass().getName());
            }
            System.out.println("We now have the translation " + (String) object);
            return (String) object;
        } catch (NoSuchFieldException ex) {
            throw new NoSuchElementException("The requested translation (" + id + ") was not found");
        }
    }
}