# Scratchomised
Scratch extension to interact with Sweet Home 3D

> [!WARNING]
> This extension is a work in progress, and is currently somewhat usable. Here is the list of the features remaining to do :
>
> * Add a password protection on the server
> * More blocks in the client
> * Export options in SweetHome3D
>
> If you have any need for this, even very small need, open an issue, and I'll continue development. I stopped working on it because I did not find much use for it.

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

# Compiling
## Scratch Plugin
As JavaScript is an interpreted language, it takes source file and interprets them instead of running a compiled version of the program. This means that all you have to do is to take the JavaScript file and give it to Scratch as it. You can minify it if you want, though.
## SweetHome3D Plugin
SweetHome3D plugins are written in Java, and so is this plugin. This means that you must install a JDK (not a JRE, which just runs Java programs), at minimum a Java 8 compiler, and a Java 17 runtime, because Gradle cannot run with an older runtime. You can use Java JDK 17, which also brings a runtime, to make things easier. Here a the instructions for some platforms. If yours is not listed, open an issue if no one is already opened, and either wait for me to answer or search by yourself (and tell me what you found).
### Installing a JDK
#### Arch Linux and derivatives
Java is included in Arch Linux's repositories. You may simply run, *as __root__*, for the latest JDK :
```bash
pacman -Syu jdk-openjdk
```
For Java 21 :
```bash
pacman -Syu jdk21-openjdk
```
Please refer to [Arch Linux's wiki page](https://wiki.archlinux.org/title/Java) for more information.
#### Debian, Ubuntu and derivatives :
Java is included in Debian's and Ubuntu's repositories. All you have to do is to run, for Java 17 :
```bash
apt install openjdk-17-jdk
```
Please refer to [Debian's wiki page](https://wiki.debian.org/Java) and [Ubuntu's documentation page](https://documentation.ubuntu.com/ubuntu-for-developers/howto/java-setup/#installing-java-development-kit) (which also includes instructions to get an IDE).
#### Other Linux distributions/OS
There are probably some good resources on how to get a JDK installed on your computer. Search for them on a web browser using something like `<os_name> jdk install`. Always prefer official documentation.
#### Windows/macOS
You may either use :

- [Temurin® JDK](https://adoptium.net/temurin/releases/)
- [Oracle OpenJDK](https://www.oracle.com/java/technologies/downloads/)
- Any other JDK you like

Simply download the installer (preferably the "normal"/EXE installer if you're on Windows, take the DGM/PKG installer on macOS), run it and voila! You may also download a ZIP archive and unzip it somewhere, but you will have to tell Gradle where to find you JDK (tip : set the `JAVA_HOME` environment variable, and add `%JAVA_HOME/bin` to the `PATH` environment variable. Look on the Internet on how to do so on your OS).
### Compiling
Enter the `Plugin` directory, then run, depending on your platform :
#### Windows
```batch
.\gradlew.bat shadowJar
```
#### Linux distribution/BSD/macOS
```bash
./gradlew shadowJar
```
Gradle will then download and compile everything that is needed to compile the plugin. You will find the resulting JAR file (`lib-all.jar`) in `Plugin/lib/build/libs`. If you want to distribute it, remove the `.jar` extension and replace it with `.sh3p`. You may name the file however you like.