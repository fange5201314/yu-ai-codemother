# Claude Code Audio Notification Hook
# Usage: Called automatically by Claude Code hooks
# Audio file: Mission Completed.mp3 (place in same directory)

# Get script directory
$HooksDir = $PSScriptRoot
$AudioPath = Join-Path $HooksDir "Mission Completed.mp3"

# Check if audio file exists
if (-not (Test-Path $AudioPath)) {
    # Play system beep as fallback
    [System.Console]::Beep(800, 300)
    exit 0
}

try {
    # Use .NET SoundPlayer for reliable audio playback
    Add-Type -AssemblyName System.Windows.Forms
    $player = New-Object System.Media.SoundPlayer $AudioPath
    $player.PlaySync()
} catch {
    # Fallback to system beep if audio playback fails
    [System.Console]::Beep(800, 300)
}