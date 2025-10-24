# PowerShell helper to build the plugin locally on Windows.
$ErrorActionPreference = "Stop"

function Ensure-App($id, $check) {
  try { & $check; return }
  catch {
    Write-Host "Installing $id..." -ForegroundColor Cyan
    winget install --id $id -e --silent | Out-Null
  }
}

# 1) Ensure Java 11
Ensure-App "EclipseAdoptium.Temurin.11.JDK" { & java -version | Select-String '\"11\.' | Out-Null }

# 2) Ensure Gradle
Ensure-App "Gradle.Gradle" { & gradle -v | Out-Null }

# 3) Build
Write-Host "Building plugin..." -ForegroundColor Cyan
gradle clean build

# 4) Copy JAR to RuneLite plugins folder
$jar = Get-ChildItem -Recurse -Filter "fairy-ring-hotkeys-*.jar" -Path ".\build\libs" | Select-Object -First 1
if (-not $jar) { throw "Build JAR not found in .\build\libs" }

$plugins = Join-Path $env:USERPROFILE ".runelite\plugins"
if (-not (Test-Path $plugins)) { New-Item -ItemType Directory -Path $plugins | Out-Null }
Copy-Item $jar.FullName -Destination $plugins -Force

Write-Host "Copied $($jar.Name) to $plugins" -ForegroundColor Green
ii $plugins