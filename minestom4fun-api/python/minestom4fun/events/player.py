from dataclasses import dataclass
from .base import MinestomEvent
from ..types.player import Player

@dataclass(frozen=True)
class PlayerJoinEvent(MinestomEvent):
    player: Player

@dataclass(frozen=True)
class PlayerLeaveEvent(MinestomEvent):
    player: Player

@dataclass(frozen=True)
class PlayerChatEvent(MinestomEvent):
    player: Player
    message: str