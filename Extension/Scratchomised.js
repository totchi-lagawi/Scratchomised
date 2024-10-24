
// Classe représentant l'extension
class Scratchomised {
    constructor() {
        const port = 5125;
        this.socket = new WebSocket("ws://localhost:" + port);
    }

    // Donne des informations à Scratch sur l'extension
    getInfo() {
        return {
            // ID de l'extension (unique à chaque extension)
            id: "scratchomised",

            // Nom de l'extension affiché dans Scratch
            name: "Scratchomised",

            // Blocs affichés dans Scratch
            blocks: [
                {
                    // L'opcode est le nom de la fonction appellée quand le bloc est utilisé
                    opcode: "defineProperty",

                    // Définit le type du bloc
                    // "command" -> Bloc "classique", qui exécute une action
                    blockType: "command",

                    // Texte affiché dans Scratch pour le bloc
                    // Le texte entre crochets représente les arguments
                    text: "Define property [PROPERTY] of [OBJECT] to [VALUE]",

                    // Définit les arguments du bloc
                    arguments: {
                        PROPERTY: {
                            // L'argument est un texte
                            type: "string",

                            // L'argument est un menu nommé "properties"
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
                }
            ],

            // Définit les menus des blocs de l'extension
            menus: {

                // Menu "properties"
                // Il s'agit d'un menu statique : il ne peut pas être modifié selon des évènements extérieurs
                properties: {
                    // Éléments du menu
                    items: "getProperties"
                },
                objects: {
                    // Le menu "objects" est un menu dynamique :
                    // Au lieu des éléments du menu, un nom de fonction est donné
                    // Cette fonction sera appellé chaque fois que le menu est ouvert
                    // Elle va retourner les éléments du menu
                    items: "getObjects"
                }
            }
        }
    }

    defineProperty(args) {
        try {
            this.socket.send(JSON.stringify({
                action: "define_property",
                object: "Light",
                property: "Powered",
                value: "True"
            }))
        } catch (e) {
            console.log("Error while sending data");
        }
        console.log("Defining property : " + args);
    }

    getProperty(args) {
        try {
            this.socket.send(JSON.stringify({
                action: "get_property",
                object: "Light",
                property: "Powered"
            }))
        } catch (e) {
            console.log("Error while sending data")
        }
        console.log("Getting property : " + args);
    }

    getObjects() {
        try {
            this.socket.send(JSON.stringify({
                action: "get_objects"
            }))
        } catch (e) {
            console.log("Error while sending data")
        }
        console.log("Getting objects...");
        return ["Hello", "World!"];
    }

    getProperties() {
        return ["A", "Proper", "Property!"]
    }

    _sendData(data) {

    }
}

Scratch.extensions.register(new Scratchomised());