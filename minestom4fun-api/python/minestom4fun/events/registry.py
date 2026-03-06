from typing import Type
from .base import MinestomEvent
from minestom4fun.events.player import PlayerJoinEvent, PlayerLeaveEvent, PlayerChatEvent

_REGISTRY: dict[str, Type[MinestomEvent]] = {
    "player_join":  PlayerJoinEvent,
    "player_leave": PlayerLeaveEvent,
    "player_chat":  PlayerChatEvent,
}

def get_event_class(name: str) -> Type[MinestomEvent] | None:
    return _REGISTRY.get(name)

def deserialize(name: str, data: dict) -> MinestomEvent | None:
    cls = get_event_class(name)
    if cls is None:
        return None
    # Flatten nested dicts into dataclass
    flat = _flatten(data)
    try:
        return cls(**flat)
    except TypeError:
        return None

def _flatten(data: dict) -> dict:
    """Flatten nested player/location dicts into typed objects."""
    from ..types.player import Player, Location
    result = {}
    for k, v in data.items():
        if isinstance(v, dict) and {"username", "uuid"} <= v.keys():
            result[k] = Player(**{kk: v[kk] for kk in ("username", "uuid")})
        elif isinstance(v, dict) and {"x", "y", "z", "world"} <= v.keys():
            result[k] = Location(**{kk: v[kk] for kk in ("x", "y", "z", "world")})
        else:
            result[k] = v
    return result