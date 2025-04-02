package io.github.totchi_lagawi.scratchomised_plugin;

import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;

public class WebSocketMessageHandler implements WsMessageHandler {
    private LanguageManager _languageManager;

    public WebSocketMessageHandler(LanguageManager languageManager) {
        this._languageManager = languageManager;
    }

    public void handleMessage(WsMessageContext ctx) throws Exception {
        System.out.println(this._languageManager.getString("log_prefix") + "Got message : " + ctx.message() + " ("
                + ctx.session.getRemoteAddress() + ")");
    }

}
