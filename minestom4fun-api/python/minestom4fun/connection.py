import asyncio
import json
import logging
from typing import AsyncIterator
import websockets
from websockets.exceptions import ConnectionClosed

logger = logging.getLogger("minestom4fun")

class Connection:
    def __init__(self, host: str, port: int, reconnect_delay: float = 3.0):
        self._uri = f"ws://{host}:{port}/events"
        self._reconnect_delay = reconnect_delay

    async def messages(self) -> AsyncIterator[dict]:
        """Yield parsed messages, auto-reconnecting on disconnect."""
        while True:
            try:
                async with websockets.connect(self._uri) as ws:
                    logger.info(f"Connected to {self._uri}")
                    async for raw in ws:
                        try:
                            yield json.loads(raw)
                        except json.JSONDecodeError:
                            logger.warning(f"Invalid JSON: {raw}")
            except ConnectionClosed:
                logger.warning("Disconnected, reconnecting...")
            except OSError:
                logger.warning(f"Cannot connect to {self._uri}, retrying in {self._reconnect_delay}s...")
            await asyncio.sleep(self._reconnect_delay)