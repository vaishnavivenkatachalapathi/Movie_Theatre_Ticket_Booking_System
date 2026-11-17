@echo off
title Velalar Cinemas - DBMS SQL Query & Transaction Simulator (Scenario 30)
cd /d "%~dp0"

echo ===============================================================================
echo   VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY (AUTONOMOUS), ERODE - 12
echo          DEPARTMENT OF INFORMATION TECHNOLOGY - ACADEMIC YEAR 2026-27
echo   DATABASE MANAGEMENT SYSTEMS (25ITT31) - SQL QUERY & TRANSACTION ENGINE
echo ===============================================================================
echo.
echo [1/2] Verifying Database Simulator...
if not exist "InteractiveDBSimulator.class" (
    echo Compiling Database Engine...
    javac InteractiveDBSimulator.java
)

echo [2/2] Launching Interactive Database Engine with Manual Input Support...
echo.
java InteractiveDBSimulator

echo.
echo ===============================================================================
echo Database session completed.
echo ===============================================================================
pause
