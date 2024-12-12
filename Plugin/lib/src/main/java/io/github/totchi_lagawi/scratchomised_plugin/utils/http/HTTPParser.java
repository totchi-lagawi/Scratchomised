package io.github.totchi_lagawi.scratchomised_plugin.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Parser class, mainly holding the <code>parse</code> static method
 */
public class HTTPParser {

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
     * @exception HTTPException If the given request is malformed (error code 400)
     */
    public static HTTPRequest parseRequest(BufferedReader reader, boolean acceptUnknownHTTPMethod,
            boolean acceptUnknownHTTPVersion)
            throws IOException, HTTPException {

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
        while (header.length() > 0) {
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
}
