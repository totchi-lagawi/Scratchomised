package io.github.totchi_lagawi.scratchomised_plugin.utils.http;

public class HTTPException extends Exception {
    private int status_code;

    /**
     * Create a new HTTPException
     * 
     * @param status_code   The HTTP error code of the exception
     */
    public HTTPException(int status_code) {
        super("HTTP error " + status_code);
        this.status_code = status_code;
    }

    /**
     * Get the HTTP error code of the exception
     * 
     * @return error code of the exception
     */
    public int getStatusCode() {
        return this.status_code;
    }
}
