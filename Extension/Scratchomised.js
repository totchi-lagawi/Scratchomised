
// Class representing the extension
class Scratchomised {
    constructor(runtime) {
        this.runtime = runtime;
        this.port = 55125;
        this.server = "localhost"
        this.protocol = "ws"
        this._reconnectSocket();
        this._objects = {}

        this._prefix = "[Scratchomised] - "
    }

    // Give informations to Scratch about the extension
    getInfo() {
        return {
            // Identifier of the extension (unique)
            id: "scratchomised",

            // Name of the extension, as shown in Scratch
            name: "Scratchomised",

            // Blocks shown in Sratch
            blocks: [
                {
                    opcode: "whenPropertyIs",
                    blockType: "hat",
                    text: "When property [PROPERTY] of [OBJECT] is [VALUE]",
                    arguments: {
                        PROPERTY: {
                            type: "string",
                            menu: "properties"
                        },
                        OBJECT: {
                            type: "string",
                            menu: "objects"
                        },
                        VALUE: {
                            type: "string"
                        }
                    }
                },
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
                    opcode: "isConnectedToServer",
                    blockType: "Boolean",
                    text: "Is connected to server?"
                },
                {
                    opcode: "getServerAddress",
                    blockType: "reporter",
                    text: "Server address"
                },
                {
                    opcode: "getServerPort",
                    blockType: "reporter",
                    text: "Server port"
                },
                {
                    opcode: "getServerProtocol",
                    blockType: "reporter",
                    text: "Server protocol"
                },
                {
                    opcode: "reconnectToServer",
                    func: "_reconnectSocket",
                    blockType: "command",
                    text: "Reconnect to the current server"
                },
                {
                    opcode: "connectToServer",
                    blockType: "command",
                    text: "Connect to server [SERVER] with port [PORT] using protocol [PROTOCOL]",
                    arguments: {
                        SERVER: {
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

    // TODO
    whenPropertyIs(args) {
        return false;
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
        if (Object.keys(this._objects).length == 0) {
            return ["[No objects]"]
        }

        let objects = []

        let ids = Object.keys(this._objects);
        for (let i = 0; i < ids.length; i++) {
            objects.push({
                text: this._objects[ids[i]].name,
                value: ids[i]
            })
        }

        if (objects.length == 0) {
            console.warn(this._prefix + "looks like some objects are stored but their names can't be retrieved")
            return ["[No objects"]
        }

        return objects;
    }

    getProperties() {
        if (Object.keys(this._objects).length == 0) {
            return ["[No properties]"]
        }

        let properties = []

        let ids = Object.keys(this._objects);
        for (let i = 0; i < ids.length; i++) {
            let current_properties = Object.keys(this._objects[ids[i]]);
            for (let u = 0; u < current_properties.length; u++) {
                if (!properties.includes(current_properties[u])) {
                    properties.push(current_properties[u])
                }
            }
        }

        if (properties.length == 0) {
            console.warn(this._prefix + "looks like some objects are stored but they have no properties")
            return ["[No properties]"]
        }

        return properties;
    }

    isConnectedToServer(args) {
        if (this.socket.readyState == this.socket.OPEN) {
            return true;
        } else {
            return false;
        }
    }

    getServerAddress(args) {
        return this.server;
    }

    getServerPort(args) {
        return this.port;
    }

    getServerProtocol(args) {
        return this.protocol;
    }

    connectToServer(args) {
        this.server = args.SERVER
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
        this.socket = new WebSocket(this.protocol + "://" + this.server + ":" + this.port, "scratchomised");
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
                    console.error(this._prefix + "missing argument for action update_objects : objects");
                }
                this._objects = {}
                for (let i = 0; i < message.args.objects.length; i++) {
                    this._objects[message.args.objects[i]["id"]] = message.args.objects[i]
                }
                break
            }
            default: {
                console.log(this._prefix + "unknown action : " + message.action)
            }
        }
    }
}

Scratch.extensions.register(new Scratchomised());