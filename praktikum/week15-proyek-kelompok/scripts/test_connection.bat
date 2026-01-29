@echo off
REM ========================================
REM Test Database Connection
REM ========================================

echo.
echo ========================================
echo   TESTING DATABASE CONNECTION
echo ========================================
echo.

cd /d "%~dp0.."

echo Running DatabaseTest.java...
echo.

mvn exec:java -Dexec.mainClass="com.upb.agripos.dao.DatabaseTest" -q

echo.
echo ========================================
echo   TEST COMPLETED
echo ========================================
echo.
pause
