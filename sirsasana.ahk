#NoEnv  ; Recommended for performance and compatibility with future AutoHotkey releases.

; #Warn  ; Enable warnings to assist with detecting common errors.

SendMode Input  ; Recommended for new scripts due to its superior speed and reliability.

SetWorkingDir %A_ScriptDir%  ; Ensures a consistent starting directory.

; SetTimer, CheckForExe, 5000 ;frequency

Loop
{
    ;Process, Exist, Sirsasana
    ;IfWinExist Sirsasana
    Sleep 30000  ; Sleep first to avoid machine startup timing issues.

    Process, Exist, java.exe
    
    if not Errorlevel {    
	Run, C:\Users\remot\Documents\GitHub\Sirsasana\sirsasana.bat, C:\Users\remot\Documents\GitHub\Sirsasana
	  Sleep 30000
	}
}
