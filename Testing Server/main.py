#!/usr/bin/env python3
import asyncio
import json
from websockets.server import serve

async def main():

    print("Server started")
    async with serve(handle_connection, "localhost", 5125):
        await asyncio.get_running_loop().create_future()
    print("Goodbye!")

async def handle_connection(socket):
    async for message in socket:
        instruction = json.loads(message)
        match instruction["action"]:
            case "get_objects":
                print("Getting objects...")
            case "define_property":
                print(instruction)
                print("Defining property " + instruction["property"] + " of object " + instruction["object"] + " to " + instruction["value"] + "...")
            case "get_property":
                print(instruction)
                print("Getting property " + instruction["property"] + " of object " + instruction["object"] + "...")
            case _:
                print("Unknown action : " + instruction["action"])

asyncio.run(main())