package io.github.totchi_lagawi.http_utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HTTPResponse {
    private HTTPVersion _version;
    private int _status;
    private HashMap<String, String> _headers;
    private String _body;

    /**
     * Constructor of the response
     * 
     * @param version the version of the response as a <code>HTTPVersion</code>
     * @param status  the status of the response as an <code>int</code
     * @param headers the headers of the response as a <code>HashMap</code>
     * @param body    the body of the response as a <code>String</code>
     */
    public HTTPResponse(HTTPVersion version, int status, HashMap<String, String> headers,
            String body) {
        this._version = version;
        this._status = status;
        this._headers = headers;
        this._body = body;
    }

    /**
     * Parse a <code>String</code> to an <code>HTTPResponse</code>
     * 
     * @param response                 the <code>String</code> containing the
     *                                 response
     * @param acceptUnknownHTTPVersion wether the function should abort if an
     *                                 unknown HTTP version is found. Useful for
     *                                 future compatibility if the response HTTP
     *                                 version doesn't matter
     * @param acceptUnknownHTTPStatus  wether the function should abort if an
     *                                 unknown HTTP status code is encountered.
     *                                 Useful for future compatibility if the
     *                                 reponse HTTP status code doesn't matter
     * @return the parsed <code>HTTPReponse</code>
     * 
     * @throws IOException   If there is some I/O error
     * @throws HTTPException If the request is malformed (thrown with error code
     *                       400)
     */
    public HTTPResponse parse(String response, boolean acceptUnknownHTTPVersion, boolean acceptUnknownHTTPStatus)
            throws IOException, HTTPException {
        // TODO
        return new HTTPResponse(null, 0, null, null);
    }

    /**
     * Get the HTTP version of the response as a <code>HTTPVersion</code>
     * 
     * @return the <code>HTTPVersion</code> representing the version
     */
    public HTTPVersion getVersion() {
        return this._version;
    }

    /**
     * Get the HTTP version of the response as a <code>double</code>
     * 
     * @throws IllegalStateException when the version of the request is unknown,
     *                               probably null
     * 
     * @return the <code>double</code> representing the version
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
     * Get the headers of the response as a <code>HashMap</code>
     * 
     * @return a <code>HashMap</code> containing the headers
     */
    public HashMap<String, String> getHeaders() {
        return this._headers;
    }

    /**
     * Get the raw body of the response as a <code>String</code>
     * 
     * @return a <code>String</code> containing the raw body
     */
    public String getBody() {
        return this._body;
    }

    /**
     * Get the raw response as a <code>String</code>
     * 
     * @return a <code>String</code> containing the raw response
     */
    public String getRawResponse() {
        StringBuilder response = new StringBuilder();

        response.append("HTTP/" + this.getVersionDouble() + " " + this._status + "\n");

        if (this._headers != null) {
            for (Map.Entry<String, String> entry : this._headers.entrySet()) {
                response.append(entry.getKey() + ": " + entry.getValue() + "\n");
            }
        }

        response.append("\n");

        if (this._body != null) {
            response.append(this._body);
        }

        return response.toString();
    }
}
