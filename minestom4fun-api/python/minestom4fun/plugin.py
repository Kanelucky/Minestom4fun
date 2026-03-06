import asyncio
import logging
from collections import defaultdict
from typing import Callable, Type
from .connection import Connection
from .events.base import MinestomEvent
from .events.registry import deserialize

logger = logging.getLogger("minestom4fun")

class Plugin:
    """
    Base class for Minestom4fun Python plugins.

    Usage:
        plugin = Plugin()

        @plugin.on(PlayerJoinEvent)
        async def on_join(event: PlayerJoinEvent):
            print(f"{event.player.username} joined!")

        plugin.run()
    """

    def __init__(self, host: str = "localhost", port: int = 25575):
        self._connection = Connection(host, port)
        self._listeners: dict[Type[MinestomEvent], list[Callable]] = defaultdict(list)

    def on(self, event_type: Type[MinestomEvent]):
        """Decorator to register an event listener by event class."""
        def decorator(func: Callable):
            self._listeners[event_type].append(func)
            return func
        return decorator

    def run(self):
        """Start the plugin (blocking)."""
        logging.basicConfig(level=logging.INFO)
        asyncio.run(self._loop())

    async def _loop(self):
        async for payload in self._connection.messages():
            event_name = payload.get("event")
            data = payload.get("data", {})

            event = deserialize(event_name, data)
            if event is None:
                continue

            for listener in self._listeners.get(type(event), []):
                try:
                    if asyncio.iscoroutinefunction(listener):
                        await listener(event)
                    else:
                        listener(event)
                except Exception as e:
                    logger.error(f"Error in listener {listener.__name__}: {e}")