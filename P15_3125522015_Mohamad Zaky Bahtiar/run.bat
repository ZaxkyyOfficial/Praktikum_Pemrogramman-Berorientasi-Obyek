@echo off
title Sistem Manajemen MBG - P15_3125522015_MohamadZaky
echo ====================================================================
echo   KOMPILASI DAN EKSEKUSI SISTEM MANAJEMEN MBG (P15)
echo ====================================================================

if not exist "bin" mkdir "bin"
if not exist "bin\P15_3125522015_MohamadZaky\view" mkdir "bin\P15_3125522015_MohamadZaky\view"
copy /Y "src\P15_3125522015_MohamadZaky\view\*" "bin\P15_3125522015_MohamadZaky\view\" >nul

javac --module-path "lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base -cp "lib\mysql-connector-j-8.3.0.jar" -d "bin" src\P15_3125522015_MohamadZaky\*.java src\P15_3125522015_MohamadZaky\model\*.java src\P15_3125522015_MohamadZaky\utils\*.java src\P15_3125522015_MohamadZaky\controller\*.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Kompilasi gagal.
    pause
    exit /b %ERRORLEVEL%
)

if "%1"=="--gui" (
    echo [INFO] Meluncurkan antarmuka Desktop GUI JavaFX...
    java --enable-native-access=javafx.graphics --module-path "lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base -cp "bin;lib\mysql-connector-j-8.3.0.jar" P15_3125522015_MohamadZaky.AppLauncher
) else (
    echo [INFO] Menjalankan aplikasi Konsol CLI MBG...
    java -cp "bin;lib\mysql-connector-j-8.3.0.jar" P15_3125522015_MohamadZaky.Main %*
)
