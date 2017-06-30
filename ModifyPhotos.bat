::@echo off
call setenv.bat

echo Running from %CLASSPATH%

java -classpath %JARS% com.mangalraj.Flicker.ModifyPhotos %*
