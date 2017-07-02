@echo off 
call setenv.bat

echo Running from %CLASSPATH%


setlocal enableDelayedExpansion 

set MYDIR=C:\something
for /F %%x in ('dir /B/D x*') do (
  set FILENAME=%%x
START "!FILENAME!" java -classpath %JARS% com.mangalraj.Flicker.UploadPics E:\bk\Pictures !FILENAME!
  echo ===========================  Search in !FILENAME! ===========================
)