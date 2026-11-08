@echo off
title Sistem Manajemen MBG - P15_3125522015_MohamadZaky
echo ====================================================================
echo   MENYIAPKAN KOMPILASI DAN MENJALANKAN APLIKASI KONSOL MBG (P15)
echo ====================================================================

if not exist "bin" mkdir "bin"

javac -cp "lib\mysql-connector-j-8.3.0.jar" -d "bin" src\P15_3125522015_MohamadZaky\*.java src\P15_3125522015_MohamadZaky\model\*.java src\P15_3125522015_MohamadZaky\utils\*.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Kompilasi gagal.
    pause
    exit /b %ERRORLEVEL%
)

echo [INFO] Kompilasi berhasil. Menjalankan aplikasi MBG...
java -cp "bin;lib\mysql-connector-j-8.3.0.jar" P15_3125522015_MohamadZaky.Main %*

