@echo off
:: Cấu hình đường dẫn ADB trên máy tính của bạn
set ADB_PATH=C:\Users\Admin\AppData\Local\Android\Sdk\platform-tools\adb.exe

:menu
cls
echo ====================================================================
echo    HUST MED DEMO - VOICE ACTION INTENT SIMULATOR (GOOGLE ASSISTANT)
echo ====================================================================
echo.
echo  [1] Gia lap giong noi: "play baby" (Justin Bieber)
echo  [2] Gia lap giong noi: "play hoa hong" (Ha Anh Tuan)
echo  [3] Gia lap giong noi: "play con mua tinh yeu" (Ha Anh Tuan)
echo  [4] Tu nhap cau lenh giong noi tuy chon (Custom Voice Query)
echo  [5] Thoat
echo.
echo ====================================================================
set /p choice="Chon tuy chon (1-5): "

if "%choice%"=="1" (
    echo.
    echo [+] Dang gia lap cau lenh: "play baby"...
    "%ADB_PATH%" shell am start -n com.example.hust_med_demo/.VoiceTestActivity -a android.media.action.MEDIA_PLAY_FROM_SEARCH -e query "baby"
    timeout /t 3
    goto menu
)

if "%choice%"=="2" (
    echo.
    echo [+] Dang gia lap cau lenh: "play hoa hong"...
    "%ADB_PATH%" shell am start -n com.example.hust_med_demo/.VoiceTestActivity -a android.media.action.MEDIA_PLAY_FROM_SEARCH -e query "hoa hong"
    timeout /t 3
    goto menu
)



if "%choice%"=="3" (
    echo.
    echo [+] Dang gia lap cau lenh: "play con mua tinh yeu"...
    "%ADB_PATH%" shell am start -n com.example.hust_med_demo/.VoiceTestActivity -a android.media.action.MEDIA_PLAY_FROM_SEARCH -e query "con mua tinh yeu"
    timeout /t 3
    goto menu
)



if "%choice%"=="4" (
    echo.
    set /p custom_query="Nhap cau lenh giong noi ban muon test: "
    echo [+] Dang gia lap cau lenh: "%custom_query%"...
    "%ADB_PATH%" shell am start -n com.example.hust_med_demo/.VoiceTestActivity -a android.media.action.MEDIA_PLAY_FROM_SEARCH -e query "%custom_query%"
    timeout /t 3
    goto menu
)

if "%choice%"=="5" (
    exit
)

echo Lua chon khong hop le, vui long chon lai!
timeout /t 2
goto menu
