@echo off
title Sistem Informasi Klinik Pratama PENS - Mohamad Zaky Bahtiar Arifianto
echo ===================================================================
echo   MEMBUKA SISTEM INFORMASI KLINIK (JAVA SWING GUI DESKTOP)
echo   Pengembang : Mohamad Zaky Bahtiar Arifianto (3125522015)
echo   Kelas      : 3D3PSDKU-SM-IT A
echo ===================================================================
echo Memeriksa kompilasi bytecode...
if not exist "bin" mkdir bin
javac -cp "lib/mysql-connector-j-8.3.0.jar" -d bin src/*.java
echo Meluncurkan Antarmuka Pengguna GUI...
start javaw -cp "bin;lib/mysql-connector-j-8.3.0.jar" KlinikMainFrame
exit
