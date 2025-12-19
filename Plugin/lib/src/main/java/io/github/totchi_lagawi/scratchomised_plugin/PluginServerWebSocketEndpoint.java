package io.github.totchi_lagawi.scratchomised_plugin;

import com.eteks.sweethome3d.model.CollectionEvent;
import com.eteks.sweethome3d.model.CollectionListener;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.CollectionEvent.Type;
import com.fasterxml.jackson.jr.ob.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;

public class PluginServerWebSocketEndpoint
        implements WebSocketListener, CollectionListener<HomePieceOfFurniture>, PropertyChangeListener {
    private Home _home;
    private LanguageManager _languageManager;
    private Session _session;
    private JSON _json;

    public PluginServerWebSocketEndpoint(LanguageManager languageManager, Home home) {
        this._home = home;
        this._languageManager = languageManager;
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        System.out.println(this._languageManager.getString("log_prefix") + this._session.getRemoteAddress()
                + " disconnected (reason : "
                + reason + ")");
        for (HomePieceOfFurniture furniture : this._home.getFurniture()) {
            furniture.removePropertyChangeListener(this);
        }
    }

    @Override
    public void onWebSocketConnect(Session session) {
        this._json = new JSON();
        this._session = session;
        System.out.println(this._languageManager.getString("log_prefix") + session.getRemoteAddress() + " connected");
        this._home.addFurnitureListener(this);

        for (HomePieceOfFurniture furniture : this._home.getFurniture()) {
            furniture.addPropertyChangeListener(this);
        }

        this.updateObjects();
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        System.out.println(this._languageManager.getString("log_prefix") + "error in connection to "
                + this._session.getRemoteAddress() + " : " + cause.getClass().getCanonicalName());
        cause.printStackTrace();
    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
    }

    @Override
    public void onWebSocketText(String message) {
        ScratchomisedRequest request = new ScratchomisedRequest();
        try {
            request = this._json.beanFrom(ScratchomisedRequest.class, message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (request.action) {
            case "define_property":
                for (HomePieceOfFurniture furniture : this._home.getFurniture()) {
                    if (furniture.getId().equals(request.args.get("object"))) {

                        Field field;
                        try {
                            field = furniture.getClass().getDeclaredField(String.valueOf(request.args.get("property")));
                        } catch (NoSuchFieldException ex) {
                            System.out.println(this._languageManager.getString("log_prefix") + "the object "
                                    + furniture.getClass().getCanonicalName() + " does not have the field "
                                    + request.args.get("property"));
                            return;
                        }
                        Class<?> fieldType = field.getType();

                        String setMethodName = "set"
                                + String.valueOf(request.args.get("property")).substring(0, 1).toUpperCase()
                                + String.valueOf(request.args.get("property")).substring(1);
                        Method setMethod = null;
                        try {
                            setMethod = furniture.getClass().getDeclaredMethod(setMethodName, fieldType);
                        } catch (NoSuchMethodException ex) {
                            System.out.println(this._languageManager.getString("log_prefix") + "The object "
                                    + furniture.getClass().getCanonicalName()
                                    + " does not implement a setter for the property " + request.args.get("property")
                                    + ", falling back to direct field manipulation (expected setter : "
                                    + setMethodName + "(" + fieldType.getCanonicalName() + "))");
                        }

                        Object value = null;

                        if (fieldType == int.class) {
                            value = Integer.parseInt(String.valueOf(request.args.get("value")));
                        } else if (fieldType == float.class) {
                            value = Float.parseFloat(String.valueOf(request.args.get("value")));
                        } else if (fieldType == boolean.class) {
                            value = Boolean.parseBoolean(String.valueOf(request.args.get("value")));
                        } else if (fieldType == String.class) {
                            value = String.valueOf(request.args.get("value"));
                        } else {
                            System.out.println(this._languageManager.getString("log_prefix") + "Unsupported field type "
                                    + fieldType.getCanonicalName() + " for field "
                                    + field.getName() + " of object "
                                    + furniture.getClass().getCanonicalName());
                        }

                        if (setMethod == null) {
                            field.setAccessible(true);
                            try {
                                field.set(furniture, value);
                            } catch (IllegalArgumentException | IllegalAccessException ex) {
                                ex.printStackTrace();
                            }
                            this.updateObjects();
                        } else {
                            try {
                                setMethod.invoke(furniture, value);
                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                                ex.printStackTrace();
                            }
                        }
                        return;
                    }
                }
                break;
        }
    }

    @Override
    public void collectionChanged(CollectionEvent<HomePieceOfFurniture> event) {
        if (event.getType() == Type.ADD) {
            event.getItem().addPropertyChangeListener(this);
        }
        this.updateObjects();
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        this.updateObjects();
    }

    @SuppressWarnings("unchecked")
    private void updateObjects() {
        Hashtable<String, Object> requestArguments = new Hashtable<>();
        requestArguments.put("objects", new ArrayList<Map<String, Object>>());
        ObjectMapper mapper = new ObjectMapper();
        for (HomePieceOfFurniture furniture : this._home.getFurniture()) {
            HashMap<String, Object> mappedObject = mapper.convertValue(furniture, HashMap.class);
            ArrayList<String> classes = new ArrayList<String>();
            Class<?> current_class = furniture.getClass();
            while (true) {
                if (current_class == Object.class) {
                    break;
                }
                classes.add(current_class.getCanonicalName());
                current_class = current_class.getSuperclass();
            }
            mappedObject.put("__scratchomisedClasses", classes);
            ((ArrayList<Map<String, Object>>)requestArguments.get("objects")).add(mappedObject);
        }
        ScratchomisedRequest updateObjectRequest = new ScratchomisedRequest("update_objects", requestArguments);
        try {
            String sent = this._json.asString(updateObjectRequest);
            this._session.getRemote().sendString(sent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
