Fairy Ring Hotkeys — All-in-One

This folder contains:
- Full RuneLite plugin source
- Gradle build files (for local builds)
- GitHub Actions workflow (for one-click cloud build)
- build-plugin.ps1 PowerShell script (automates local build on Windows)

=== OPTION A: Build Online with GitHub (simplest) ===
1) Create an empty GitHub repository named 'fairy-ring-hotkeys' (Public).
2) Click 'Upload files' and upload the CONTENTS of this folder (not the folder itself).
   That means: build.gradle, settings.gradle, gradle.properties, runelite-plugin.properties, src/, .github/, README.txt, build-plugin.ps1
3) Go to the Actions tab -> 'Build Fairy Ring Hotkeys' -> Run workflow.
4) When it finishes, open the run -> download the 'fairy-ring-hotkeys-jar' artifact.
   Inside you'll find fairy-ring-hotkeys-1.0.0.jar. Drop that into %USERPROFILE%\.runelite\plugins and restart RuneLite.

=== OPTION B: Build Locally on Windows (no GitHub) ===
1) Ensure you have Java 11 installed (Temurin 11). In PowerShell, 'java -version' should show version 11.
2) Install Gradle via Winget: 'winget install Gradle.Gradle' (or install manually).
3) Right-click build-plugin.ps1 -> Run with PowerShell (or run: Set-ExecutionPolicy -Scope CurrentUser RemoteSigned; .\build-plugin.ps1).
4) The script builds the JAR and opens your RuneLite plugins folder.

After installing the JAR, enable 'Fairy Ring Hotkeys' in RuneLite and configure your 1–4 favorite codes.