@echo off
title Sistem Manajemen MBG - P12_3125522015_MohamadZaky
echo ====================================================================
echo   MENYIAPKAN KOMPILASI DAN MENJALANKAN APLIKASI DESKTOP JAVAFX MBG (P12)
echo ====================================================================

if not exist "bin\P12_3125522015_MohamadZaky\view" mkdir "bin\P12_3125522015_MohamadZaky\view"
copy /Y "src\P12_3125522015_MohamadZaky\view\*" "bin\P12_3125522015_MohamadZaky\view\" >nul

javac --module-path "lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base -cp "lib\mysql-connector-j-8.3.0.jar" -d "bin" src\P12_3125522015_MohamadZaky\*.java src\P12_3125522015_MohamadZaky\model\*.java src\P12_3125522015_MohamadZaky\utils\*.java src\P12_3125522015_MohamadZaky\controller\*.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Kompilasi gagal.
    pause
    exit /b %ERRORLEVEL%
)

echo [INFO] Kompilasi berhasil. Menjalankan aplikasi MBG...
java --enable-native-access=javafx.graphics --module-path "lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base -cp "bin;lib\mysql-connector-j-8.3.0.jar" P12_3125522015_MohamadZaky.Main %*

