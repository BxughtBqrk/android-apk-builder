# EyeRIS Clock Overlay

An always-on-top clock app designed for the EyeRIS Board (Android 14) by CybernetyX.

## Features

✨ **Dual Display**: Shows both analog and digital clocks
📅 **Date Display**: Displays current date with day of week
⏱️ **Live Seconds**: Updates every second for precise timekeeping
🎯 **Draggable**: Move the clock anywhere on your screen
🔝 **Always On Top**: Stays above all other apps
🎨 **Clean Design**: Semi-transparent background with clear, readable display

## Installation

### Prerequisites
- Android Studio (Arctic Fox or later)
- Android SDK 24 or higher
- EyeRIS Board device running Android 14

### Building the APK

1. **Open the project in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the `ClockOverlay` folder

2. **Connect your EyeRIS Board**
   - Enable Developer Options on your device
   - Enable USB Debugging
   - Connect via USB cable

3. **Build and Install**
   - Click on "Build" → "Build Bundle(s) / APK(s)" → "Build APK(s)"
   - Once built, click "Run" (green play button) to install on your device
   - Or locate the APK in `app/build/outputs/apk/debug/` and install manually

### Alternative: Build via Command Line

```bash
cd ClockOverlay
./gradlew assembleDebug
```

The APK will be in `app/build/outputs/apk/debug/app-debug.apk`

## Usage

1. **Launch the app** - Open "EyeRIS Clock" from your app drawer

2. **Grant overlay permission**
   - On first launch, you'll be prompted to grant "Display over other apps" permission
   - Tap "Start Clock Overlay"
   - You'll be redirected to Settings
   - Enable the permission for EyeRIS Clock
   - Return to the app

3. **Start the clock overlay**
   - Tap the "Start Clock Overlay" button
   - The clock will appear on your screen

4. **Move the clock**
   - Tap and drag the clock to position it anywhere on screen
   - The position will be maintained even when switching apps

5. **Stop the overlay**
   - Return to the EyeRIS Clock app
   - Tap "Stop Clock Overlay"

## Technical Details

### Permissions Required
- `SYSTEM_ALERT_WINDOW` - Allows the app to draw over other apps
- `FOREGROUND_SERVICE` - Keeps the clock running in the background
- `FOREGROUND_SERVICE_SPECIAL_USE` - Required for Android 14 foreground services

### Components

**MainActivity.java**
- Handles overlay permission requests
- Controls the overlay service start/stop

**ClockOverlayService.java**
- Foreground service that maintains the overlay
- Manages the window manager and view lifecycle
- Updates the clock every second

**ClockView.java**
- Custom view that draws the analog clock
- Renders hour markers, hands, and digital time/date display
- Updates in real-time

### Compatibility
- Minimum SDK: Android 7.0 (API 24)
- Target SDK: Android 14 (API 34)
- Optimized for: EyeRIS Board by CybernetyX

## Customization

You can customize the clock appearance by modifying `ClockView.java`:

- **Clock size**: Change the `size` variable in `onMeasure()`
- **Colors**: Modify the Paint object colors in `init()`
- **Text size**: Adjust `textSize` values for digital display
- **Transparency**: Change the alpha value in `circlePaint.setColor()`

## Troubleshooting

**Clock doesn't appear after starting**
- Make sure overlay permission is granted
- Check if the service is running in Settings → Apps → EyeRIS Clock

**Clock disappears on reboot**
- The clock must be manually started after each reboot
- This is by design to prevent unnecessary battery drain

**Can't move the clock**
- Make sure you're tapping directly on the clock face
- The touch area is limited to the clock view itself

## License

This app is created for the EyeRIS Board device by CybernetyX.

## Version History

**v1.0** (2024)
- Initial release
- Analog + Digital clock display
- Draggable overlay
- Date and seconds display
- Android 14 support
