package io.github.totchi_lagawi.scratchomised_plugin;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.yaml.snakeyaml.Yaml;

public class LanguageManager implements PropertyChangeListener {
    private String _defaultLang;
    private Map<String, Translation> _translations = new HashMap<String, Translation>();

    public LanguageManager(String defaultLang) {
        this._defaultLang = defaultLang;
    }

    public String getString(String id) {
        return getString(id, this._defaultLang);
    }

    public String getString(String id, String lang) {
        this.loadTranslation(lang, false);
        try {
            return this._translations.get(lang).getTranslation(id);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public void setDefaultLang(String lang) {
        this.loadTranslation(lang, false);
        this._defaultLang = lang;
    }

    public String getDefaultLang() {
        return this._defaultLang;
    }

    public void loadTranslation(String lang, boolean replace) {
        // Check if it is really useful to load the translation
        if (replace = false) {
            if (this._translations.containsKey(lang)) {
                return;
            }
        }

        // Load the translation
        InputStream file_stream = this.getClass().getResourceAsStream("translations/" + lang + ".yaml");

        // Ensure that the translation file actually exists
        if (file_stream == null) {
            throw new NoSuchElementException("The translation file for " + lang + " was not found");
        }

        Yaml yaml = new Yaml();
        Translation translation = (Translation) yaml.loadAs(file_stream, Translation.class);
        this._translations.put(lang, translation);
    }

    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals("LANGUAGE")) {
            this._defaultLang = (String) event.getNewValue();
        }
    }
}
