#!/usr/bin/env python3
import asyncio
import json
from websockets.server import serve

objects = {
    "wall": {
        "colour": "white"
    },
    "lamp": {
        "powered": True
    }
}

async def main():
    print("Server started")
    async with serve(handle_connection, "localhost", 55125):
        await asyncio.get_running_loop().create_future()
    print("Goodbye!")

async def handle_connection(socket):
    async for message in socket:
        instruction = json.loads(message)
        print("Got message!")
        match instruction["action"]:
            case "get_objects":
                print("Getting objects...")
                await send(socket, "set_objects", {
                    "objects": objects
                })
            case "define_property":
                print("Defining property " + instruction["args"]["property"] + " of object " + instruction["args"]["object"] + " to " + instruction["args"]["value"] + "...")
            case "get_property":
                print("Getting property " + instruction["args"]["property"] + " of object " + instruction["args"]["object"] + "...")
            case _:
                print("Unknown action : " + instruction["action"])

async def send(socket, action, args = {}):
    await socket.send(json.dumps({
        "action": action,
        "args": args
    }))

asyncio.run(main())