package io.github.totchi_lagawi.websocket_server;

public class WebSocketFrame {
    // Is the frame the latest of the message?
    private boolean _is_final;
    // MUST be zero unless an extension of the protocol was negociated
    // TODO since this server does not support any extensions, maybe just remove
    // them? If not, add getter
    private boolean rsv1, rsv2, rsv3;
    // The opcode
    private WebSocketFrameOpcode _opcode;
    // Wether the current frame is masked
    private boolean _masked;
    // The masking key, empty if the frame isn't masked, or 4-byte (32 bits) long if
    // it is
    private byte[] _masking_key;
    // The actual payload
    // It can contain both extension data (determined by the extension) and
    // application data (what is normally used)
    // Since this server does not support any extension, it will very probably
    // contain only application data
    private byte[] _payload;

    /**
     * Constructor of a WebSocketFrame
     * 
     * @param is_final    wether the frame is the final frame of a message
     * @param opcode      the opcode of the frame
     * @param is_masked   wether the frame is masked
     * @param masking_key the masking key, or <code>null</code> if the frame is not
     *                    masked
     * @param payload     the payload
     */
    public WebSocketFrame(boolean is_final, WebSocketFrameOpcode opcode, boolean is_masked, byte[] masking_key,
            byte[] payload) {

    }

    /**
     * Constructor of a WebSocketFrame
     * 
     * @param frame bytes containing the frame
     */
    public WebSocketFrame(byte[] frame) {

    }

    /**
     * Get wether the frame is the final frame of a message
     * 
     * @return <code>true</code> if it is, otherwise <code>false</code>
     */
    public boolean isFinal() {
        return this._is_final;
    }

    /**
     * Set wether the frame is the final frame of a message
     * 
     * @param is_final wether the frame is the final frame of a message
     */
    public void setIsFinal(boolean is_final) {
        this._is_final = is_final;
    }

    /**
     * Get the opcode of the frame
     * 
     * @return a <code>WebSocketFrameOpcode</code> containing the opcode
     */
    public WebSocketFrameOpcode getOpcode() {
        return this._opcode;
    }

    /**
     * Set the opcode of the frame
     * 
     * @param opcode the opcode to set to
     */
    public void setOpcode(WebSocketFrameOpcode opcode) {
        this._opcode = opcode;
    }

    /**
     * Get wether the frame is masked
     * 
     * @return <code>true</code> if it is, otherwise <code>false</code>
     */
    public boolean isMasked() {
        return this._masked;
    }

    /**
     * Set wether the frame is masked
     * 
     * @param is_masked wether the frame is masked
     */
    public void setIsMasked(boolean is_masked) {
        this._masked = is_masked;
    }

    /**
     * Get the masking key of the frame
     * 
     * @return bytes containing the masking key, or <code>null</code> if the frame
     *         is not masked
     */
    public byte[] getMaskingKey() {
        return this._masking_key;
    }

    /**
     * Set the masking key of the frame
     * 
     * @param masking_key bytes containing the masking key
     */
    public void setMaskingKey(byte[] masking_key) {
        this._masking_key = masking_key;
    }

    /**
     * Get the lengh of the payload of the frame
     * 
     * @return the lengh of the payload
     */
    public int getPayloadLengh() {
        if (this._payload == null) {
            return 0;
        } else {
            return this._payload.length;
        }
    }

    /**
     * Get the payload of the frame
     * 
     * @return bytes containing the payload
     */
    public byte[] getPayload() {
        return this._payload;
    }

    /**
     * Set the payload of the frame
     * 
     * @param payload the payload to set to
     */
    public void setPayload(byte[] payload) {
        this._payload = payload;
    }

    /**
     * Convert the frame to bytes, ready to be sent
     * 
     * @return bytes containing the frame
     */
    public byte[] toBytes() {
        return null;
    }
}