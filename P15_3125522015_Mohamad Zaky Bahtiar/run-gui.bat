@echo off
title Sistem Manajemen MBG - Desktop GUI (P15_3125522015_MohamadZaky)
echo ====================================================================
echo   KOMPILASI DAN PELUNCURAN DASHBOARD JAVAFX GUI SISTEM MBG
echo ====================================================================

if not exist "bin" mkdir "bin"
if not exist "bin\P15_3125522015_MohamadZaky\view" mkdir "bin\P15_3125522015_MohamadZaky\view"
copy /Y "src\P15_3125522015_MohamadZaky\view\*" "bin\P15_3125522015_MohamadZaky\view\" >nul

javac --module-path "lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base -cp "lib\mysql-connector-j-8.3.0.jar" -d "bin" src\P15_3125522015_MohamadZaky\*.java src\P15_3125522015_MohamadZaky\model\*.java src\P15_3125522015_MohamadZaky\utils\*.java src\P15_3125522015_MohamadZaky\controller\*.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Kompilasi JavaFX gagal.
    pause
    exit /b %ERRORLEVEL%
)

echo [INFO] Kompilasi berhasil. Meluncurkan antarmuka Desktop JavaFX...
java --enable-native-access=javafx.graphics --module-path "lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base -cp "bin;lib\mysql-connector-j-8.3.0.jar" P15_3125522015_MohamadZaky.AppLauncher %*

