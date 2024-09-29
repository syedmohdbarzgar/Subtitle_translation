# Subtitle_translation

**Subtitle_translation** is an Android project that allows users to translate subtitle files using **ML Kit** libraries. This project is designed to support multiple languages and helps you translate subtitles into other languages and save them.

##Screenshots

<div style="display: flex; justify-content: space-around;">
    <img src="https://github.com/syedmohdbarzgar/Subtitle_translation/blob/master/app/src/main/assets/screenshots/main.jpg" alt="Image 1" width="300"/>
    <img src="https://github.com/syedmohdbarzgar/Subtitle_translation/blob/master/app/src/main/assets/screenshots/translator.jpg" alt="Image 2" width="300"/>
</div>


## Features

- Automatic subtitle translation using Google ML Kit
- Subtitle file (SRT) selection capability
- Translate SRT files and save the translated version on the device
- Utilizes RxJava3 for background processing
- Multi-language support
- Android permission management using Dexter
- WorkManager for managing background translation tasks

## Requirements

- Android SDK 21 and higher
- Google ML Kit
- Internet access to download language models

## How to Use

1. Clone the project:
   ```bash
   git clone https://github.com/syedmohdbarzgar/Subtitle_translation.git
## How to Use

1. Open the project in Android Studio.
2. Fetch the required libraries from Gradle.
3. Run the app on your Android device.

## Libraries Used

- Google ML Kit
- RxJava3
- WorkManager
- Dexter

## Known Issues

- **Screen Rotation**: During device rotation, the translation operation halts and the process restarts. Use `ViewModel` to manage lifecycle and save state.
- **Temporary Language Model Storage**: Occasionally, language models may not download correctly or may be temporarily lost. A better memory management system for handling models needs to be implemented.
- **Subtitle File Errors**: Some SRT subtitle files do not load correctly. More rigorous validation should be performed on the file before processing.

## Contribution

Please report any bugs or suggestions via the Issues section on GitHub.

## License

This project is licensed under the MIT License, so you can use, modify, and distribute it.

> MIT License  
> Copyright (c) 2024 syedmohdbarzgar  
> 
> Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
> 
> The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
> 
> THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
