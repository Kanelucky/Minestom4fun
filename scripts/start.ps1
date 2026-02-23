$host.UI.RawUI.WindowTitle = "Minestom4fun - A Minecraft Java Edition server software built on top of Minestom"

# Check if Java is installed
if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    Write-Host "Java is not installed. Please install Java 25 or higher."
    pause
    exit 1
}

# Check if server jar exists
$JAR_FILE = Get-Item minestom4fun-server-*-shaded.jar 2>$null | Select-Object -First 1

if (-not $JAR_FILE) {
    Write-Host "Server jar not found."
    Write-Host "You can download the file from https://github.com/Kanelucky/Minestom4fun/releases"
    pause
    exit 1
}

java --enable-native-access=ALL-UNNAMED `
    -XX:+UseZGC `
    -XX:+ZGenerational `
    -XX:+UseStringDeduplication `
    -jar $JAR_FILE.Name nogui

pause