@echo off
TITLE Minestom4fun - A Minecraft Java Edition server software built on top of Minestom

REM Check if Java is installed
java -version >nul 2>&1
IF %ERRORLEVEL% NEQ 0 (
    echo Java is not installed. Please install Java 25 or higher.
    pause
    exit /b 1
)

REM Check if server jar exists
set JAR_FILE=
for %%f in (minestom4fun-server-*-shaded.jar) do set JAR_FILE=%%f

IF "%JAR_FILE%"=="" (
    echo Server jar not found.
    echo You can download the file from https://github.com/Kanelucky/Minestom4fun/releases
    pause
    exit /b 1
)

set JAVA_CMD=java

%JAVA_CMD% --enable-native-access=ALL-UNNAMED ^
    -XX:+UseZGC ^
    -XX:+ZGenerational ^
    -XX:+UseStringDeduplication ^
    -jar "%JAR_FILE%" nogui

pause