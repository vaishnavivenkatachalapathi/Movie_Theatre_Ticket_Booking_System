@echo off
title Velalar Cinemas - Foundations of Data Science Analytics (Scenario 30)
cd /d "%~dp0"

echo ===============================================================================
echo   VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY (AUTONOMOUS), ERODE - 12
echo          DEPARTMENT OF INFORMATION TECHNOLOGY - ACADEMIC YEAR 2026-27
echo      FOUNDATIONS OF DATA SCIENCE (25ITC31) - BOX OFFICE DATA ANALYTICS
echo ===============================================================================
echo.
echo [1/2] Verifying Analytics Engine...
if not exist "InteractiveAnalytics.class" (
    echo Compiling Analytics Engine...
    javac InteractiveAnalytics.java
)

echo [2/2] Launching Interactive Analytics Engine with Manual Input Support...
echo.
java InteractiveAnalytics

echo.
echo ===============================================================================
echo Analytics session completed.
echo ===============================================================================
pause
