@echo off
title Velalar Cinemas - Java Ticket Booking System (Scenario 30)
cd /d "%~dp0"

echo ===============================================================================
echo   VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY (AUTONOMOUS), ERODE - 12
echo          DEPARTMENT OF INFORMATION TECHNOLOGY - ACADEMIC YEAR 2026-27
echo   MOVIE THEATRE TICKET BOOKING & BOX OFFICE ANALYSIS SYSTEM (SCENARIO 30)
echo ===============================================================================
echo.
echo [1/2] Checking Java Bytecode...
if not exist "bin\com\theatre\app\MovieTheatreApp.class" (
    echo Compiling Java source files...
    if not exist "bin" mkdir bin
    javac -d bin -sourcepath src src\com\theatre\app\MovieTheatreApp.java
)

echo [2/2] Launching Interactive Java Application with Manual Input Support...
echo.
java -cp bin com.theatre.app.MovieTheatreApp

echo.
echo ===============================================================================
echo Program execution completed.
echo ===============================================================================
pause
