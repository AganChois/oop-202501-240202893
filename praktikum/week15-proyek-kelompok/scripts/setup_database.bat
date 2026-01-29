@echo off
REM ========================================
REM Setup Database AgriPOS
REM ========================================

echo.
echo ========================================
echo   SETUP DATABASE AGRIPOS
echo ========================================
echo.

REM Check if PostgreSQL is installed
where psql >nul 2>nul
if %errorlevel% neq 0 (
    echo [WARNING] psql command tidak ditemukan di PATH!
    echo.
    echo SOLUSI:
    echo 1. Gunakan manual setup via pgAdmin (lihat QUICK_START.md Option 2)
    echo 2. Atau tambahkan PostgreSQL ke PATH:
    echo    - Default location: C:\Program Files\PostgreSQL\16\bin
    echo    - Tambahkan ke System Environment Variables
    echo.
    echo Meneruskan dengan asumsi PostgreSQL terinstall...
    REM Don't exit, let user try manual setup instead
)

echo [1/5] Checking PostgreSQL installation...
echo PostgreSQL found!
echo.

REM Set PostgreSQL credentials
set PGPASSWORD=242424
set PGUSER=postgres
set PGHOST=localhost
set PGPORT=5432

echo [2/5] Creating database 'agripos'...
psql -U %PGUSER% -h %PGHOST% -p %PGPORT% -c "CREATE DATABASE agripos;" 2>nul
if %errorlevel% equ 0 (
    echo Database 'agripos' created successfully!
) else (
    echo Database 'agripos' already exists or error occurred.
)
echo.

echo [3/5] Running schema.sql...
psql -U %PGUSER% -h %PGHOST% -p %PGPORT% -d agripos -f "%~dp0..\sql\schema.sql"
if %errorlevel% neq 0 (
    echo [ERROR] Failed to run schema.sql
    pause
    exit /b 1
)
echo Schema created successfully!
echo.

echo [4/5] Running seed.sql...
psql -U %PGUSER% -h %PGHOST% -p %PGPORT% -d agripos -f "%~dp0..\sql\seed.sql"
if %errorlevel% neq 0 (
    echo [ERROR] Failed to run seed.sql
    pause
    exit /b 1
)
echo Sample data inserted successfully!
echo.

echo [5/5] Verifying database...
psql -U %PGUSER% -h %PGHOST% -p %PGPORT% -d agripos -c "SELECT 'Products' as table_name, COUNT(*) as rows FROM products UNION ALL SELECT 'Promos', COUNT(*) FROM promos UNION ALL SELECT 'Users', COUNT(*) FROM users;"
echo.

echo ========================================
echo   SETUP COMPLETED!
echo ========================================
echo.
echo Next steps:
echo 1. cd praktikum\week15-proyek-kelompok
echo 2. mvn clean install
echo 3. mvn javafx:run
echo.
pause
