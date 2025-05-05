
// Class representing the extension
class Scratchomised {
    constructor() {
        this.port = 55125;
        this.host = "localhost"
        this.protocol = "ws"
        this._reconnectSocket();
        this._objects = {}

        this._prefix = "[Scratchomised] - "
    }

    //Give informations to Scratch about the extension
    getInfo() {
        return {
            // Identifier of the extension (unique)
            id: "scratchomised",

            // Name of the extension, as shown in Scratch
            name: "Scratchomised",

            // Blocks shown in Sratch
            blocks: [
                {
                    // The opcode is the name of the function called when the block is used
                    opcode: "defineProperty",

                    // Define block type
                    // "command" -> "Classic" block
                    blockType: "command",

                    // Text shown in Scratch for the block
                    // Text between "[" and "]" is arguments
                    text: "Define property [PROPERTY] of [OBJECT] to [VALUE]",

                    // Define block's arguments
                    arguments: {
                        PROPERTY: {
                            // Argument is a text
                            type: "string",

                            // And we can choose it from a menu called "properties"
                            menu: "properties"
                        },
                        OBJECT: {
                            type: "string",
                            menu: "objects"
                        },
                        VALUE: {
                            type: "string",
                            defaultValue: ""
                        }
                    }
                },
                {
                    opcode: "getProperty",
                    blockType: "reporter",
                    text: "Property [PROPERTY] of [OBJECT]",
                    disableMonitor: true,
                    arguments: {
                        PROPERTY: {
                            type: "string",
                            menu: "properties"
                        },
                        OBJECT: {
                            type: "string",
                            menu: "objects"
                        }
                    }
                },
                {
                    opcode: "connectToHost",
                    blockType: "command",
                    text: "Connect to host [HOST] with port [PORT] using protocol [PROTOCOL]",
                    arguments: {
                        HOST: {
                            type: "string"
                        },
                        PORT: {
                            type: "number"
                        },
                        PROTOCOL: {
                            type: "string",
                            menu: "connection_protocols"
                        }
                    }
                }
            ],

            // Define the extension's menus
            menus: {

                // "Properties" menu
                // It is a static menu
                properties: {
                    // Menu elements
                    items: "getProperties"
                },
                objects: {
                    // The menu "object" is a dynamic menu
                    // Instead of giving an array, the name of a function is given
                    // This function will be called each time the menu is opened
                    // It will return an array containing the menu's elements
                    //
                    // /!\ WARNING /!\
                    // Due to a strange behaviour of the TurboWarp VM, dynamic menus only works
                    // in unsandboxed extensions
                    items: "getObjects"
                },
                connection_protocols: {
                    items: [{
                        text: "WS",
                        value: "ws"
                    },
                    {
                        text: "WSS",
                        value: "wss"
                    }]
                }
            }
        }
    }

    defineProperty(args) {
        this._send("define_property", {
            object: args.OBJECT,
            property: args.PROPERTY,
            value: args.VALUE
        });
    }

    getProperty(args) {
        try {
            return this._objects[args.OBJECT][args.PROPERTY]

        } catch (error) {
            console.error(this._prefix + "error while getting property " + args.PROPERTY + " of object " + args.OBJECT);
        }
    }

    getObjects() {
        var keys = Object.keys(this._objects);
        if (keys.length == 0) {
            return ["(Empty)"]
        } else {
            return keys
        }
    }

    getProperties() {
        var final_properties = []
        const array = Object.values(this._objects);
        for (var index = 0; index < array.length; index++) {
            const properties = Object.keys(array[index]);
            for (var index2 = 0; index2 < properties.length; index2++) {
                console.debug(this._prefix + "trying to add property : " + properties[index2]);
                if (!final_properties.includes(properties[index2])) {
                    final_properties.push(properties[index2])
                }
            }
        }
        if (final_properties.length == 0) {
            return ["(Empty)"]
        } else {
            return final_properties
        }
    }

    connectToHost(args) {
        this.host = args.HOST
        this.port = args.PORT
        this.protocol = args.PROTOCOL
        this._reconnectSocket();
    }

    // Function to send data to the server
    _send(action, args = {}) {
        try {
            this.socket.send(JSON.stringify({
                action: action,
                args: args
            }))
        } catch (error) {
            console.error(this._prefix + "error while sending data : \n" + error);
        }
    }

    // Reconnect the socket (in case it failed to connect, or the server disconnected)
    _reconnectSocket() {
        this.socket = new WebSocket(this.protocol + "://" + this.host + ":" + this.port, "scratchomised");
        this.socket.addEventListener("open", function () {
            this._send("get_objects")
        }.bind(this));
        this.socket.addEventListener("message", this._handleIncomingData.bind(this));
    }

    // Handle data sent by the server
    _handleIncomingData(event) {
        try {
            var message = JSON.parse(event.data)
        } catch (error) {
            console.error(this._prefix + "unable to parse message : \n" + event.data + "\n" + this._prefix + " got error : " + error);
            return
        }

        switch (message.action) {
            case "update_objects": {
                if (!message.args.objects) {
                    console.error(this._prefix + "missing argument for action set_objects : objects");
                }
                this._objects = message.args.objects
                break
            }
            default: {
                console.log(this._prefix + "unknown action : " + message.action)
            }
        }
    }
}

Scratch.extensions.register(new Scratchomised());