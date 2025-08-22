# CommuteTimely - Hybrid Android + React Native/Expo App

This is a hybrid application that combines native Android development with React Native/Expo capabilities.

## 🏗️ Project Structure

```
CommuteTimely/
├── app/                    # Native Android app (Kotlin + Compose)
├── core/                   # Core modules (data, domain, ui)
├── feature/                # Feature modules (home, map, weather, etc.)
├── assets/                 # Expo assets (icons, splash screens)
├── App.js                  # React Native main component
├── index.js                # React Native entry point
├── app.json                # Expo configuration
├── eas.json                # EAS Build configuration
├── metro.config.js         # Metro bundler configuration
├── babel.config.js         # Babel configuration
├── package.json            # Node.js dependencies
└── build.gradle            # Android build configuration
```

## 🚀 Getting Started

### Prerequisites
- Node.js 16+ and npm
- Android Studio with Android SDK
- EAS CLI (`npm install -g eas-cli`)

### Development

#### Native Android Development
```bash
# Open in Android Studio
# Build and run the native Android app
./gradlew assembleDebug
```

#### React Native/Expo Development
```bash
# Install dependencies
npm install

# Start Expo development server
npm start

# Run on Android
npm run android

# Run on iOS
npm run ios

# Run on web
npm run web
```

## 📱 Building with EAS

### Development Build
```bash
# Create development build for Android
npm run build:android

# Create development build for iOS
npm run build:ios
```

### Production Build
```bash
# Production build for Android
eas build --platform android --profile production

# Production build for iOS
eas build --platform ios --profile production
```

### Submitting to Stores
```bash
# Submit Android build to Play Store
npm run submit:android

# Submit iOS build to App Store
npm run submit:ios
```

## 🔧 Configuration

### Expo Configuration (app.json)
- App name, version, and bundle identifiers
- Platform-specific settings
- Required plugins (location, dev-client)

### EAS Configuration (eas.json)
- Build profiles (development, preview, production)
- Platform-specific build settings
- Submit configurations

## 📦 Available Scripts

- `npm start` - Start Expo development server
- `npm run android` - Run on Android device/emulator
- `npm run ios` - Run on iOS device/simulator
- `npm run web` - Run in web browser
- `npm run build:android` - Build Android app with EAS
- `npm run build:ios` - Build iOS app with EAS
- `npm run submit:android` - Submit Android build to Play Store
- `npm run submit:ios` - Submit iOS build to App Store

## 🌟 Features

### Native Android Features
- Location services with proper permissions
- Mapbox integration for mapping
- Weather data integration
- Compose UI components
- MVVM architecture with Hilt DI

### React Native/Expo Features
- Cross-platform development
- Hot reloading
- EAS Build for cloud builds
- Expo plugins and services
- Web support

## 🔄 Hybrid Development Workflow

1. **Native Features**: Use existing Kotlin code for platform-specific features
2. **Cross-platform Features**: Use React Native for shared business logic
3. **Build & Deploy**: Use EAS Build for both platforms
4. **Testing**: Test native features in Android Studio, React Native features with Expo

## 📚 Documentation

- [Expo Documentation](https://docs.expo.dev/)
- [React Native Documentation](https://reactnative.dev/)
- [EAS Build Documentation](https://docs.expo.dev/build/introduction/)
- [Android Development](https://developer.android.com/)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test on both platforms
5. Submit a pull request

## 📄 License

This project is licensed under the ISC License.
