from ollama import chat
import urllib
from ollama_handler import BoltOllamaHandler
from websockets.asyncio.server import serve
import asyncio

handler = BoltOllamaHandler()


def get_query_param(path, key):
    query = urllib.parse.urlparse(path).query
    params = urllib.parse.parse_qs(query)
    values = params.get(key, [])
    if len(values) == 1:
        return values[0]
    
async def ollama_handler(websocket):
    token = get_query_param()
    async for message in websocket:
        try:
            response = handler.get_summary(message)

            for chunk in response:
                await websocket.send(chunk['message']['content'])
        
        except Exception as e:
            print(e)
    

async def main():
    async with serve(ollama_handler, "localhost", 8765, ping_interval=None) as server:
        await server.serve_forever()

if __name__ == "__main__":
    asyncio.run(main())