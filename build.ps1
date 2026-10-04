$ErrorActionPreference = 'Stop'
Set-StrictMode -Version Latest

# --release checks both bytecode and platform APIs against Java 8.
$jdk = $env:JAVA_HOME
if (-not $jdk) {
    throw 'Set JAVA_HOME to a JDK 17 or newer installation, then run build.ps1 again.'
}
$javac = Join-Path $jdk 'bin/javac.exe'
$jar = Join-Path $jdk 'bin/jar.exe'
if (-not (Test-Path $javac) -or -not (Test-Path $jar)) {
    throw 'JAVA_HOME must point to a JDK, not a JRE.'
}

Push-Location $PSScriptRoot
try {
    # A fresh directory prevents stale classes from entering a release.
    $build = Join-Path $PSScriptRoot ('build/' + [guid]::NewGuid().ToString('N'))
    $classes = Join-Path $build 'classes'
    $package = Join-Path $build 'MAC-Multi-Audio-Converter'
    New-Item -ItemType Directory -Force $classes, "$package/lib", 'dist' | Out-Null
    $dependency = 'lib/flatlaf-3.7.2.jar'
    & $javac --release 8 -encoding UTF-8 -cp $dependency -d $classes MainUI.java FileHandler.java ProcessWorker.java FFmpegEngine.java MacRootPaneUI.java
    if ($LASTEXITCODE -ne 0) { throw 'Java compilation failed.' }
    & $jar cfm "$package/MAC.jar" META-INF/MANIFEST.MF -C $classes .
    if ($LASTEXITCODE -ne 0) { throw 'JAR packaging failed.' }
    Copy-Item $dependency "$package/lib/"
    Copy-Item Start-MAC.bat, README.md $package
    Compress-Archive -Path $package -DestinationPath 'dist/MAC-Multi-Audio-Converter-java8.zip' -Force
    Write-Host "Built $package/MAC.jar"
    Write-Host 'Release: dist/MAC-Multi-Audio-Converter-java8.zip'
} finally {
    Pop-Location
}
