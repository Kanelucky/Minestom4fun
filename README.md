<!-- PROJECT LOGO -->
<div align="center">
  <a href="https://github.com/Kanelucky/Minestom4fun">
    <img src="minestom4fun-server/src/main/resources/icon.png" alt="Logo" width="200" height="200">
  </a>
<h1>Minestom4fun</h1>

<h3>Just 4 fun xd</h3>
</div>

---

> [!IMPORTANT]
> This project is in **0.x** stage and is under active development. Breaking changes may occur at any time.

## Goals

- Make Minestom do what it was never designed for: **Survival**
- Pure experimentation, no limits, just 4 fun

---

## Features

- **Player**
    - [x] Combat
    - [ ] Skins
- **Commands**
    - [x] `/gamemode`
    - [x] `/version`
    - [x] `/tps`
    - [x] `/kill`
    - [x] `/op`
    - [x] `/help`
    - [x] `/status`
- **World**
    - [x] Basic superflat generator and preset
    - [x] World generation *(UNSTABLE)*
    - [x] Liquid Physics
    - [ ] Entity Spawning
- **Events**
    - Player
        - [x] PlayerPickupItemEvent
        - [x] PlayerDropItemEvent
        - [x] PlayerDeathEvent
    - Server
      - [x] PlayerJoinEvent
      - [x] PlayerQuitEvent
      - [x] PlayerSpawnEvent
    - Block
        - [x] BlockBreakEvent
    
- **Network**
    - [x] ServerListPing
    - [x] Server Brand
- **Terminal**
    - [x] ServerTerminalConsole

---

## Usage

### Requirements

- Java 25 JRE/JDK
- Windows / macOS / Linux / FreeBSD

### Steps

1. Download the latest release from [Releases](https://github.com/Kanelucky/Minestom4fun/releases) or build from source
2. Get your start script [here](https://github.com/Kanelucky/Minestom4fun/tree/master/scripts)
3. Run the server with that start script

### Building from source
```bash
git clone https://github.com/Kanelucky/Minestom4fun.git
cd Minestom4fun
./gradlew shadowJar
```

- The output jar will be located at `minestom4fun-server/build/libs/`

---

## Credits

- [<img src="https://raw.githubusercontent.com/XDPXI/SwiftMC-Website/refs/heads/main/favicon.png" width="16"/>](https://github.com/XDPXI/SwiftMC) [SwiftMC](https://github.com/XDPXI/SwiftMC)