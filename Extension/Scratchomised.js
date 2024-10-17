
// Classe représentant l'extension
class Scratchomised {
    // Rien à définir pour l'instant!
    constructor () {
    }

    // Donne des informations à Scratch sur l'extension
    getInfo () {
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
                    items : ["property", "foo", "bar"]
                },
                objects: {
                    // Le menu "object" est un menu dynamique :
                    // Au lieu des éléments du menu, un nom de fonction est donné
                    // Cette fonction sera appellé chaque fois que le menu est ouvert
                    // Elle va retourner les éléments du menu
                    items: "getObjects"
                }
            }
        }
    }

    defineProperty(args) {
        console.log("Defining property : " + args);
    }

    getProperty(args) {
        console.log("Getting property : " + args);
    }

    getObjects() {
        console.log("Getting objects...");
        return ["Hello", "World!"];
    }
}

Scratch.extensions.register(new Scratchomised());