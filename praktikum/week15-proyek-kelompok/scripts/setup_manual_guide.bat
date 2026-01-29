@echo off
REM ========================================
REM Setup Database AgriPOS - Manual Guide
REM ========================================

echo.
echo ========================================
echo   SETUP DATABASE AGRIPOS - MANUAL
echo ========================================
echo.
echo Script otomatis mungkin tidak berfungsi jika PostgreSQL
echo tidak ada di PATH. Berikut langkah manual:
echo.
echo ========================================
echo LANGKAH 1: Buka pgAdmin
echo ========================================
echo 1. Cari "pgAdmin" di Start Menu dan buka
echo 2. Login dengan master password Anda
echo 3. Connect ke PostgreSQL Server (localhost)
echo.
pause
echo.
echo ========================================
echo LANGKAH 2: Buat Database
echo ========================================
echo 1. Di sidebar kiri, klik kanan "Databases"
echo 2. Pilih: Create -^> Database...
echo 3. Database name: agripos
echo 4. Owner: postgres
echo 5. Klik "Save"
echo.
pause
echo.
echo ========================================
echo LANGKAH 3: Jalankan SQL Script
echo ========================================
echo 1. Klik kanan database "agripos"
echo 2. Pilih: Query Tool
echo 3. Buka File Explorer dan navigate ke:
echo    %~dp0..\sql\setup_complete.sql
echo 4. Buka file tersebut dengan Notepad
echo 5. Copy SEMUA isi file
echo 6. Paste ke Query Tool di pgAdmin
echo 7. Klik tombol Execute (atau tekan F5)
echo.
echo File location:
echo %~dp0..\sql\setup_complete.sql
echo.
pause
echo.
echo ========================================
echo LANGKAH 4: Verifikasi
echo ========================================
echo Di Query Tool, jalankan query berikut:
echo.
echo SELECT COUNT(*) as total FROM products;
echo.
echo Jika hasilnya 5, maka setup berhasil!
echo.
pause
echo.
echo ========================================
echo LANGKAH 5: Test dari Aplikasi
echo ========================================
echo 1. Buka terminal/command prompt
echo 2. cd praktikum\week15-proyek-kelompok
echo 3. mvn clean install
echo 4. mvn exec:java -Dexec.mainClass="com.upb.agripos.dao.DatabaseTest"
echo.
echo Jika muncul "Koneksi database BERHASIL!" maka selesai!
echo.
echo ========================================
echo SELESAI!
echo ========================================
echo.
echo Selanjutnya jalankan aplikasi dengan:
echo mvn javafx:run
echo.
pause
