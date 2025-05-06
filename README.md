# Scratchomised
Scratch extension to interact with Sweet Home 3D

> [!WARNING]
> This extension is a work in progress, and is currently barely usable. Here is the list of the features remaining to do :
> * Add a password protection on the server
> * More blocks in the client
> * Export options in SweetHome3D

# Usage

## Connecting to the server
You first need to start the server, by opening the "Scratchomised" menu on SweetHome3D, then clicking on "Start server". Then, connect to the server in Scratch. If you loaded the extension after starting the server, the extension should already have connected to the server. Otherwise, just scroll down to the "Scratchomised" block section, and press "Reconnect". You can check whether the extension is connected with the "is Scratchomised connected" block.

## Using the blocks
There are multiples blocks to interact with the objects you created in SweetHome3D. Some are object-specific, like "Turn on [OBJECT]" for a lamp, and some are object-agnostic, like "Set the property [PROPERTY] of object [OBJECT] to [VALUE]". Some other blocks are there for some advanced usage, like "Connect to host [HOST] using protocol [PROTOCOL]". Here is the full list of all the available blocks, sorted by category :
* TODO : block categories
* Object-agnostic :
* * `Set the property [PROPERTY] of object [OBJECT] to [VALUE]` - Sets the specified property of the specified object to the specified value
* * `Property [PROPERTY] of object [OBJECT]` - Returns the value of the specified property
* Advanced :
* * `Reconnect Scratchomised to the server` - Tries to reconnect the extension to the server
* * `Is Scratchomised connected` - Returns whether the extension is currently connected to the server
* * `Connect to server [SERVER] using protocol [PROTOCOL]` - Connect to the specified server using the specified protocol.

# Installation

## Scratch

### TurboWarp and derivatives (PenguinMod, ...)
* Click on the extension button, in the bottom right corner
* Scroll down a bit, and click on "Custom Extension"
* Either :
* * Download the extension script from [there](https://github.com/totchi-lagawi/Scratchomised/blob/main/Extension/Scratchomised.js) and import it using the "Files" page
* * Copy the extension script from [there](https://github.com/totchi-lagawi/Scratchomised/blob/main/Extension/Scratchomised.js), and paste it in the "Text" menu
* **Make sure you selected "Run without sandbox", as TurboWarp has a bug making the extension unusable in sandboxed mode**
* You can now push "Load", and the extension will appear in the block menu, at the left

### Eureka
*TODO*

### Xcratch
Unfortunately, Xcratch only allows loading an extension from a URL, but GitHub only serves raw files with the MIME type `text/plain`, while the browser require it to be `text/javascript`, so this environment is not supported.

## SweetHome3D
* Download the plugin from the [release tab](https://github.com/totchi-lagawi/Scratchomised/releases/). It is named `Scratchomised-Plugin.sh3p`
* Try opening the file as it. If it runs SweetHome3D, then you plugin is installed
* If SweetHome3D hasn't opened, you need to manually install the plugin. Simply move the file to the following location, depending on your operating system :
* * Most Linux distributions : `/home/<username>/.eteks/sweethome3d/plugins`
* * Windows : `C:\Users\<username>\AppData\Roaming\eTeks\Sweet Home 3D\plugins`
* * macOS : `/Users/<username>/Library/Application Support/eTeks/Sweet Home 3D/plugins`

# Bugs and suggestions
They should be posted to [the issue tab](https://github.com/totchi-lagawi/Scratchomised/issues), using the adequate form. And thanks for taking time to report these!