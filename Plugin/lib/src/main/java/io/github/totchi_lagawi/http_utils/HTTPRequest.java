package io.github.totchi_lagawi.http_utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
     * Parse a <code>BufferedReader</code> to an HTTP request
     * 
     * @param reader                   the BufferedReader to parse from
     * @param acceptUnknownHTTPMethod  wether the function should abort if an
     *                                 unknown HTTP method is found. Useful for
     *                                 future compatibility, if the request method
     *                                 doesn't matter
     * @param acceptUnknownHTTPVersion wether the function should abort if an
     *                                 unknown HTTP version is found. Useful for
     *                                 future compatibility, if the version doesn't
     *                                 matter.
     * 
     * @exception IOException   If there is some I/O error
     * @exception HTTPException If the given request is malformed (thrown with error
     *                          code 400)
     */
    // TODO Replace the use of BufferedReader to String, allowing the end of the
    // stream to be detected
    public static HTTPRequest parse(String request, boolean acceptUnknownHTTPMethod,
            boolean acceptUnknownHTTPVersion)
            throws IOException, HTTPException {

        BufferedReader reader = new BufferedReader(new StringReader(request));

        String requestLine = reader.readLine();

        if (requestLine == null || requestLine.length() == 0 || Character.isWhitespace(requestLine.charAt(0))) {
            throw new HTTPException(400);
        }

        String[] requestInfos = requestLine.split("\\s");

        if (requestInfos.length != 3 || requestInfos[1].indexOf("/") != 0 || requestInfos[2].indexOf("HTTP/") != 0) {
            throw new HTTPException(400);
        }

        HTTPMethod method;

        switch (requestInfos[0]) {
            case "GET":
                method = HTTPMethod.GET;
                break;
            default:
                if (acceptUnknownHTTPMethod) {
                    method = HTTPMethod.UNKNOWN;
                    break;
                } else {
                    throw new HTTPException(400);
                }
        }

        String location = requestInfos[1];

        HTTPVersion version;
        switch (requestInfos[2]) {
            case "HTTP/1":
                version = HTTPVersion.ONE_POINT_ZERO;
                break;
            case "HTTP/1.1":
                version = HTTPVersion.ONE_POINT_ONE;
                break;
            case "HTTP/2":
                version = HTTPVersion.TWO;
            default:
                if (acceptUnknownHTTPVersion) {
                    version = HTTPVersion.UNKNOWN;
                    break;
                } else {
                    throw new HTTPException(400);
                }
        }

        HashMap<String, String> headers = new HashMap<>();

        String header = reader.readLine();
        while (header != null && !header.isEmpty()) {
            int index = header.indexOf(":");
            if (index == -1) {
                throw new HTTPException(400);
            }
            headers.put(header.substring(0, index), header.substring(index + 2));
            header = reader.readLine();
        }

        // Got an empty line, switching to body
        String bodyLine = reader.readLine();
        StringBuilder body = new StringBuilder();
        while (bodyLine != null) {
            body.append(header);
            bodyLine = reader.readLine();
        }

        return new HTTPRequest(method, version, location, headers, body.toString());

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

        if (this._headers != null) {
            for (Map.Entry<String, String> entry : this._headers.entrySet()) {
                request.append(entry.getKey() + ": " + entry.getValue() + "\n");
            }
        }

        request.append("\n");

        if (this._body != null) {
            request.append(this._body);
        }

        return request.toString();
    }
}