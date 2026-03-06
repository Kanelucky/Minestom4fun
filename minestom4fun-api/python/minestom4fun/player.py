from dataclasses import dataclass

@dataclass(frozen=True)
class Location:
    x: float
    y: float
    z: float
    world: str

@dataclass(frozen=True)
class Player:
    username: str
    uuid: str