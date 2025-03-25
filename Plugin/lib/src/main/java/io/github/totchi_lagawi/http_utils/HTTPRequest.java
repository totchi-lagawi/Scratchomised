package io.github.totchi_lagawi.http_utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
     * Parse an InputStreamReader to an HTTPRequest
     * 
     * @param input                    the InputStreamReader containing the request
     * @param acceptUnknownHTTPMethod  whether to allow not yet known HTTP methods
     * @param acceptUnknownHTTPVersion whether to allow not yet known HTTP versions
     * 
     * @throws IOException
     * @throws HTTPException if the given HTTP request if malformed
     */
    public HTTPRequest(InputStreamReader input, boolean acceptUnknownHTTPMethod, boolean acceptUnknownHTTPVersion)
            throws IOException, HTTPException {
        BufferedReader reader = new BufferedReader(input);

        String headerLine = reader.readLine();

        if (headerLine == null || headerLine.length() == 0 || Character.isWhitespace(headerLine.charAt(0))) {
            throw new HTTPException(400, "The given request didn't contain a valid HTTP header line");
        }

        String[] requestInfos = headerLine.split("\\s");

        if (requestInfos.length != 3 || requestInfos[1].indexOf("/") != 0 || requestInfos[2].indexOf("HTTP/") != 0) {
            throw new HTTPException(400, "Then given request didn't contain a valid HTTP header line");
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
                    throw new HTTPException(400, "The given request contained an unknown HTTP method");
                }
        }

        String location = requestInfos[1];

        HTTPVersion version;
        switch (requestInfos[2]) {
            case "HTTP/0.9":
                version = HTTPVersion.ZERO_POINT_NINE;
                break;
            case "HTTP/1":
                version = HTTPVersion.ONE_POINT_ZERO;
                break;
            case "HTTP/1.1":
                version = HTTPVersion.ONE_POINT_ONE;
                break;
            case "HTTP/2":
                version = HTTPVersion.TWO;
                break;
            case "HTTP/3":
                version = HTTPVersion.THREE;
                break;
            default:
                if (acceptUnknownHTTPVersion) {
                    version = HTTPVersion.UNKNOWN;
                    break;
                } else {
                    throw new HTTPException(400, "The given request contained an unknown HTTP version");
                }
        }

        HashMap<String, String> headers = new HashMap<>();

        String header = reader.readLine();
        while (header != null && !header.isEmpty()) {
            int index = header.indexOf(":");
            if (index == -1) {
                throw new HTTPException(400, "The given request contained a malformed header field");
            }
            headers.put(header.substring(0, index), header.substring(index + 2));
            header = reader.readLine();
        }

        // Body is hard to get

        // // Got an empty line, switching to body
        // String bodyLine = reader.readLine();
        // StringBuilder body = new StringBuilder();
        // while (bodyLine != null) {
        // body.append(header);
        // bodyLine = reader.readLine();
        // }

        this._method = method;
        this._version = version;
        this._location = location;
        this._headers = headers;
        this._body = "";
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