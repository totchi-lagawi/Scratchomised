package io.github.totchi_lagawi.scratchomised_plugin.utils.http;

import java.util.HashMap;
import java.util.Map;

/**
 * A class holding an HTTP request
 */
public class HTTPRequest {
    private HTTPMethod _method;
    private HTTPVersion _version;
    private String _location;
    private HashMap<String, String> _headers;
    private String _body;

    /**
     * The constructor of <code>HTTPRequest</code>
     * 
     * @param method   the method of the request as a <code>HTTPMethod</code>
     * @param version  the version of the request as a <code>HTTPVersion</code
     * @param location  the location of the request as a <code>String</code>
     * @param headers  the headers of the request as a <code>HashMap</code>
     * @param body     the body of the request as a <code>String</code>
     */
    public HTTPRequest(HTTPMethod method, HTTPVersion version, String location, HashMap<String, String> headers,
            String body) {
        this._method = method;
        this._version = version;
        this._location = location;
        this._headers = headers;
        this._body = body;
    }

    /**
     * Get the method of the request as a <code>HTTPMethod</code>
     * 
     * @return the method of the request as a <code>HTTPMethod</code>
     */
    public HTTPMethod getMethod() {
        return this._method;
    }

    /**
     * Get the version of the request as a <code>HTTPVersion</code
     * 
     * @return the version of the request as a <code>HTTPVersion</code>
     */
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

    /**
     * Get the location of the request as a <code>String</code>
     * 
     * @return the location of the request as a <code>String</code>
     */
    public String getLocation() {
        return this._location;
    }

    /**
     * Get the headers of the request as a <code>HashMap</code>
     * 
     * @return the headers of the request as a <code>HashMap</code>
     */
    public HashMap<String, String> getHeaders() {
        return this._headers;
    }

    /**
     * Get the body of the request as a <code>String</code>
     * 
     * @return the body of the request as a <code>String</code>
     */
    public String getBody() {
        return this._body;
    }

    /**
     * Get the raw request as a <code>String</code>
     * 
     * @return the request as a <code>String</code>
     */
    public String getRawRequest() {
        StringBuilder request = new StringBuilder();

        request.append(this._method + " " + this._location + " " + this.getVersionDouble() + "\n");

        for (Map.Entry<String, String> entry : this._headers.entrySet()) {
            request.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }

        request.append(this._body);

        return request.toString();
    }
}