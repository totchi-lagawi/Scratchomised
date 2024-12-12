package io.github.totchi_lagawi.scratchomised_plugin.utils.http;

import java.util.HashMap;
import java.util.Map;

public class HTTPRequest {
    private HTTPMethod _method;
    private HTTPVersion _version;
    private String _location;
    private HashMap<String, String> _headers;
    private String _body;

    public HTTPRequest(HTTPMethod method, HTTPVersion version, String location, HashMap<String, String> headers,
            String body) {
        this._method = method;
        this._version = version;
        this._location = location;
        this._headers = headers;
        this._body = body;
    }

    public HTTPMethod getMethod() {
        return this._method;
    }

    public HTTPVersion getVersion() {
        return this._version;
    }

    /**
     * Get the HTTP version of the request as a <code>double</code>
     * 
     * @throws IllegalStateException when the version of the request is unknown,
     *                               probably null
     * 
     * @return the double representing the version
     */
    public double getVersionDouble() throws IllegalStateException {
        switch (this._version) {
            case ZERO_POINT_NINE:
                return 0.9;
            case ONE_POINT_ZERO:
                return 1;
            case ONE_POINT_ONE:
                return 1.1;
            case TWO:
                return 2;
            case THREE:
                return 3;
            case UNKNOWN:
                return -1;
            default:
                throw new IllegalStateException("HTTP version undefined (probably null)");
        }
    }

    public String getLocation() {
        return this._location;
    }

    public HashMap<String, String> getHeaders() {
        return this._headers;
    }

    public String getBody() {
        return this._body;
    }

    public String getRawRequest() {
        StringBuilder request = new StringBuilder();
        request.append(this._method + " " + this._location + " " + this.getVersionDouble() + "\n");
        for (Map.Entry<String, String> entry : this._headers.entrySet()) {
            request.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }
        return request.toString();
    }
}