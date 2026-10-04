# MAC — Multi Audio Converter

**Batch audio conversion with a macOS-inspired dark interface.**
Created by Karin.

MAC is a small desktop app for working on several audio files at once. Choose a folder, select the files you want, adjust your export settings, and let it process the batch. It is useful for preparing sample packs, converting music files, organizing stems, or making osu! hitsounds without opening every file in an audio editor.

The interface is built with Java Swing and FlatLaf. FFmpeg does the audio processing in the background.

Despite the name and appearance, the current setup is aimed at **Windows**. “MAC” stands for **Multi Audio Converter**; it does not mean the app is only for Apple computers.

## What you can do

- Convert files between WAV, MP3, OGG, FLAC, and M4A, or keep each file's existing format.
- Process multiple files in parallel using the available CPU cores.
- Adjust the requested audio bitrate and manually raise or lower the volume.
- Apply automatic loudness normalization or remove silence.
- Keep, remove, or replace metadata such as artist, title, album, and track number.
- Rename output files using the original filename and an automatic counter.
- Search and sort the file list, choose individual files, and open a file in your default audio player.
- View a spectrogram comparison and an estimated export size before converting.

MAC is meant for straightforward batch jobs. It does not have a timeline, multitrack editing, or the detailed editing tools of a full audio workstation.

## Before you start

| Requirement | What it is needed for |
| --- | --- |
| Java 8 or newer | Runs the application. Java 7 and earlier are not supported. |
| FFmpeg | Reads, converts, and processes audio. The `ffmpeg` command must be available on your system's `PATH`. |
| The complete application ZIP | Contains the app, launcher, and required FlatLaf theme library. |

You do **not** need a Java development kit to use a packaged release. A compatible Java runtime is enough. Building the app from source requires a JDK.

The ZIP does not include Java or FFmpeg. FlatLaf is already included, so you do not need to download the theme separately.

## Installation

1. Get `MAC-Multi-Audio-Converter-java8.zip` from the release package. If you are browsing the repository, check the [Releases page](https://github.com/KarinistakenXD/MAC-Multi-Audio-Converter/releases) for available downloads. GitHub's **Source code** ZIP is not a ready-to-run application.
2. Extract the entire ZIP into a folder. Do not run the app from inside the archive.
3. Keep the extracted files together:

   ```text
   MAC-Multi-Audio-Converter/
   ├── Start-MAC.bat
   ├── MAC.jar
   ├── README.md
   └── lib/
       └── flatlaf-3.7.2.jar
   ```

4. Make sure Java 8 or newer is installed.
5. Double-click **Start-MAC.bat**.

If FFmpeg is missing, MAC offers to install it through Windows Package Manager (`winget`). Complete the installation in the terminal window. If the app still cannot find it afterward, close and reopen MAC so it can pick up the updated environment.

If you prefer a terminal, open one in the extracted application folder and run:

```shell
java -jar MAC.jar
```

## Your first conversion

1. **Choose an input folder.** MAC reads supported audio files directly inside that folder. It does not search subfolders.
2. **Choose an input filter.** Keep **All Audio Files** selected to include every supported format, or select one extension to narrow the list.
3. **Review your selection.** Open the file-selection window and choose the files you want to process. Click **Done** to keep your selection.
4. **Choose an output format.** Select a format or use **Same as original** to keep each file's extension. Keeping the format still processes and re-encodes the audio; it is not a simple file copy.
5. **Adjust the settings.** Choose a bitrate, change the volume if needed, and enable normalization or silence trimming only when you want those changes.
6. **Review metadata settings.** Leave **Keep Original** selected unless you want to remove or replace tags.
7. **Start the conversion.** With **Overwrite original files** unchecked, the app asks you to choose an export folder. Use a new, empty folder for your first batch.
8. **Check the results.** Listen to a few exported files and confirm their names, formats, and tags before processing a larger collection.

If you dismiss the export-folder picker, MAC asks whether you want to cancel. Choosing to continue creates a folder such as `MAC_Export_mp3` inside the input folder.

## Understanding the settings

### Format and bitrate

The input filter controls which files are selected from the folder. The output format controls what those files become.

The quality slider selects a requested bitrate from **64 to 500 kbps**. The actual result depends on the output format and FFmpeg encoder. Some encoders may reject particular values, and bitrate does not control WAV or FLAC quality in the same way it controls lossy formats.

Increasing the bitrate cannot restore detail that was already lost in a compressed source. The displayed output size is an estimate, not a promised file size.

### Volume and automatic normalization

Manual volume adjustment ranges from **−10 dB to +10 dB**. A value of **0 dB** leaves the manual gain unchanged.

**Auto-Balance Volume** uses FFmpeg's `loudnorm` filter with targets of **−14 LUFS integrated loudness**, **−1 dBTP true peak**, and **11 LU loudness range**. In everyday terms, it aims to make files more consistent in perceived loudness. It does not guarantee that every sound will feel equally loud.

When automatic normalization is enabled, MAC uses it instead of the manual volume adjustment.

### Silence trimming

**Clean up dead air** uses a silence threshold of **−50 dB**. Quiet material below that threshold may be treated as silence.

The current filter can remove silent sections within a file as well as silence around its edges. Try it on a copy first if pauses, quiet fades, or timing are important to the audio.

### Metadata and filenames

The metadata editor offers three modes:

| Mode | What it does |
| --- | --- |
| Keep Original | Requests that FFmpeg copy the source metadata into the output. Supported tags depend on the destination format. |
| Strip All | Disables automatic copying of source metadata. |
| Overwrite All | Uses the values you enter for the exported files. |

Editable fields include artist, track title, album, track number, year, genre, comments, copyright, and output filename.

Use these placeholders when entering your own values:

| Placeholder | Where it works | Example |
| --- | --- | --- |
| `{original}` | Output Filename | `{original}_converted` keeps the original base name and adds a suffix. |
| `{count}` | Output Filename and metadata values | `Hit {count}` produces names such as `Hit 1`, `Hit 2`, and `Hit 3`. |

The output extension is added automatically. The counter starts at 1 for each batch, but its order is based on the input file enumeration and is not guaranteed to follow the visible table sorting.

Tag support varies between formats. Embedded artwork preservation is not guaranteed, so inspect the results if your files contain cover art you want to keep.

## File selection and previews

The selection window has a search box and sortable name, type, and size columns. Use **Ctrl+Click** to toggle individual files or **Shift+Click** to select a range. **Select All** applies to the currently visible rows. Filtering the list does not automatically deselect files hidden by the filter.

Double-clicking a file opens the original in your system's default audio player. Playback is handled by that player, not by an embedded player in MAC.

The spectrogram compares a sample input file with a temporary processed preview. It reflects the selected output format, bitrate, and manual volume adjustment. It does **not** include automatic normalization, silence trimming, or metadata changes, and it does not represent every file in the batch.

## About the Mac-style window controls

On Windows, this version adds traffic-light buttons to the left side of the title bar:

- **Red:** close the window.
- **Yellow:** minimize the window.
- **Green:** maximize the window or restore its previous size.

Hover over a button to reveal its symbol. The controls turn gray when the window is inactive. The title is centered, and the custom title bar uses FlatLaf's existing window handling for dragging and resizing.

This styling applies to the app's Swing windows. The Windows taskbar and operating-system dialogs keep their normal appearance. On macOS, the app leaves the native window controls in place; the Windows launcher and automatic FFmpeg installation are Windows-specific.

## Current limitations

This is an early version, and a few reliability issues still need attention:

- **The completion message does not report individual conversion failures.** A batch can finish with missing or failed outputs. Check the exported files rather than relying only on the message.
- **Overwrite mode needs safer file replacement.** It can delete an original before confirming that the replacement file was successfully renamed. Keep backups and use a separate export folder for important audio.
- **Existing output files can be replaced.** FFmpeg is run with overwrite enabled. Even with the app's overwrite checkbox off, choosing a folder with matching filenames can replace files there. Reusing the input folder as the destination is especially risky.
- **Filename collisions are not resolved automatically.** Files with the same base name, or custom naming patterns that produce duplicates, can target the same output path.
- **Platform testing is limited.** The setup and launcher target Windows. A similar appearance on another platform does not mean the full workflow has been tested there.

The Java compatibility and title-bar changes do not resolve these audio-processing limitations.

## Troubleshooting

### The app closes immediately or nothing happens

Use `Start-MAC.bat` instead of double-clicking the JAR. The launcher keeps startup errors visible so you can read or copy them.

You can also check the Java selected by your terminal:

```shell
java -version
```

Java 8 may display a version beginning with `1.8.0`; that is normal.

### I get an “UnsupportedClassVersionError”

This usually means the application was compiled for a newer Java version than the runtime trying to open it. Use the Java 8 compatible package and confirm that the selected runtime is Java 8 or newer.

The Windows launcher looks for Java in this order:

1. `runtime/bin/java.exe` inside the extracted app folder, if you have supplied a compatible portable runtime.
2. `JAVA_HOME/bin/java.exe`, if it exists.
3. `java.exe` on `PATH`.

If you have several Java installations, an outdated `JAVA_HOME` can cause the launcher to use a different version from the one you expected. The package does not download or bundle a portable runtime automatically.

### The error mentions FlatLaf or a missing class

Extract the complete ZIP again. Check that `lib/flatlaf-3.7.2.jar` is next to `MAC.jar` in the folder structure shown above. Moving only the JAR to another folder leaves its theme dependency behind.

### FFmpeg is installed, but MAC cannot find it

Open a new terminal and run:

```shell
ffmpeg -version
```

If the command is not recognized, FFmpeg is not available on `PATH` in that terminal. Finish configuring it, then restart MAC. If `winget` is unavailable, install FFmpeg separately and make its executable available on `PATH`.

### No files appear

Check the input folder and format filter. MAC currently lists WAV, MP3, OGG, FLAC, and M4A files directly in the selected folder; files in nested folders will not appear.

### The batch finishes, but an output is missing

Try a single file with a different bitrate or output format, and use an empty export folder. The current interface does not show a per-file FFmpeg error report, so the completion message alone cannot confirm that every conversion succeeded.

## Building from source

The current build script runs on Windows with PowerShell. Install a **JDK 17 or newer** and set `JAVA_HOME` to that JDK's installation folder. A runtime-only Java installation cannot compile the app.

From the repository folder, run:

```powershell
./build.ps1
```

The distributable archive is written to:

```text
dist/MAC-Multi-Audio-Converter-java8.zip
```

The build uses `javac --release 8`, which checks both the generated bytecode and the Java APIs against Java 8. This helps prevent a build made with a newer JDK from accidentally requiring that newer version on users' computers.

FlatLaf is copied into the package, and the JAR manifest points to it. Distribute the complete ZIP rather than individual compiled classes or a JAR copied from an IDE's output directory.

The GitHub Actions workflow is configured to build the package and run compatibility checks on Java 8, 17, and 21. Those checks load the application classes and theme and verify title-bar button placement and action wiring. They do not test full audio conversion, interactive dragging, or resizing.

## Reporting a problem

Open an [issue](https://github.com/KarinistakenXD/MAC-Multi-Audio-Converter/issues) and include:

- Your Windows version and the output of `java -version`.
- Which application package you are using.
- The input and output formats, bitrate, and enabled processing options.
- Whether you used an export folder or overwrite mode.
- What you expected, what happened, and any error shown by the launcher.

A small example file that you are comfortable sharing can help reproduce an audio issue.

## Built with

- **Java Swing** for the desktop interface.
- **[FlatLaf](https://www.formdev.com/flatlaf/)** for the dark theme and window decorations.
- **[FFmpeg](https://ffmpeg.org/)** for audio conversion, filtering, metadata handling, and spectrogram generation.
