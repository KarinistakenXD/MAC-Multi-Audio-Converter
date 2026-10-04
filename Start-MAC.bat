@echo off
setlocal
cd /d "%~dp0"
rem A portable runtime can be supplied without changing the system Java.
set "MAC_JAVA=java.exe"
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" set "MAC_JAVA=%JAVA_HOME%\bin\java.exe"
if exist "%~dp0runtime\bin\java.exe" set "MAC_JAVA=%~dp0runtime\bin\java.exe"
"%MAC_JAVA%" -jar "%~dp0MAC.jar"
if not errorlevel 1 exit /b 0
echo.
echo MAC could not start or exited with an error. See the details above.
echo MAC requires Java 8 or newer. Extract the entire ZIP, including lib.
echo You can also place a compatible Java runtime in the runtime folder.
pause
exit /b 1
