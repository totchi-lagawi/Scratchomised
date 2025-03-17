package io.github.totchi_lagawi.http_utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    public HTTPResponse(InputStreamReader input, boolean acceptUnknownHTTPVersion, boolean acceptUnknownHTTPStatus)
            throws IOException, HTTPException {
        BufferedReader reader = new BufferedReader(input);

        String headerLine = reader.readLine();

        if (headerLine == null || headerLine.length() == 0 || Character.isWhitespace(headerLine.charAt(0))) {
            throw new HTTPException(400, "The given response didn't contain a valid HTTP header line");
        }

        String[] headerInfos = headerLine.split("\\s");

        if (headerInfos.length < 2 || headerInfos[0].indexOf("HTTP/") != 0 || headerInfos[1].length() != 3) {
            throw new HTTPException(400, "The given response didn't contain a valid HTTP header line");
        }

        HTTPVersion version;
        switch (headerInfos[0]) {
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
            default:
                if (acceptUnknownHTTPVersion) {
                    version = HTTPVersion.UNKNOWN;
                    break;
                } else {
                    throw new HTTPException(400, "The given response contained an unknown HTTP version");
                }
        }

        HTTPStatusCode statusCode;
        try {
            statusCode = HTTPStatusCode.getByValue(Integer.parseInt(headerInfos[1]));
        } catch (NumberFormatException ex) {
            throw new HTTPException(400, "The given response contained a malformed HTTP status code");
        }

        if (statusCode.name() == "UNKOWN" && !acceptUnknownHTTPStatus) {
            throw new HTTPException(400, "The given response contained an unkown HTTP status code");
        }

        HashMap<String, String> headers = new HashMap<>();

        String header = reader.readLine();
        while (header != null && !header.isEmpty()) {
            int index = header.indexOf(":");
            if (index == -1) {
                throw new HTTPException(400, "The given response contained a malformed header field");
            }
            headers.put(header.substring(0, index), header.substring(index + 2));
            header = reader.readLine();
        }

        String bodyLine = reader.readLine();
        StringBuilder body = new StringBuilder();
        while (bodyLine != null) {
            body.append(bodyLine);
            bodyLine = reader.readLine();
        }

        this._version = version;
        // this._status =
        this._headers = headers;
        this._body = body.toString();

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
