#!/bin/bash

TITLE="Minestom4fun - A Minecraft Java Edition server software built on top of Minestom"

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Java is not installed. Please install Java 25 or higher."
    exit 1
fi

# Check if server jar exists
JAR_FILE=$(ls minestom4fun-server-*-shaded.jar 2>/dev/null | head -n 1)

if [ -z "$JAR_FILE" ]; then
    echo "Server jar not found."
    echo "You can download the file from https://github.com/Kanelucky/Minestom4fun/releases"
    exit 1
fi

java --enable-native-access=ALL-UNNAMED \
    -XX:+UseZGC \
    -XX:+ZGenerational \
    -XX:+UseStringDeduplication \
    -jar "$JAR_FILE" nogui