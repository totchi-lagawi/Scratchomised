package io.github.totchi_lagawi.scratchomised_plugin.utils.http;

import java.util.HashMap;

public class HTTPRequest {
    private HTTPMethod _method;
    private HTTPVersion _version;
    private String _location;
    private HashMap<String, String> _headers;

    public HTTPRequest(HTTPMethod method, HTTPVersion version, String location, HashMap<String, String> headers) {
        this._method = method;
        this._version = version;
        this._location = location;
        this._headers = headers;
    }

    public HTTPMethod getMethod() {
        return this._method;
    }

    public HTTPVersion getVersion() {
        return this._version;
    }

    /**
     * Get the HTTP version as a <code>double</code>
     * 
     * @return the double representing the version
     */
    public double getVersionDouble() {
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
}