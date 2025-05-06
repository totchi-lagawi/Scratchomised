package io.github.totchi_lagawi.scratchomised_plugin;

import java.util.Hashtable;

public class ScratchomisedRequest {
    public String action;
    public Hashtable<String, Object> args;

    public ScratchomisedRequest(String action, Hashtable<String, Object> args) {
        this.action = action;
        this.args = args;
    }

    public ScratchomisedRequest(String action) {
        this.action = action;
        this.args = new Hashtable<>();
    }

    public ScratchomisedRequest() {
        this.action = "";
        this.args = new Hashtable<>();
    }
}
