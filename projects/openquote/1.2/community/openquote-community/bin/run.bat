@echo off

set OPENQUOTE_HOME=%CD%\..
set TMP=%OPENQUOTE_HOME%\tmp
set LIB=%OPENQUOTE_HOME%\lib
set JBOSS_HOME=%OPENQUOTE_HOME%\jboss
set PROGNAME=run.bat

if exist "%TMP%\setup" goto SETUP_DONE

echo Running OpenQuote setup...

if "%JAVA_HOME%" == "" set JAR="jar"
if not "%JAVA_HOME%" == "" set JAR=%JAVA_HOME%\bin\jar

echo Extracting jars for product development...

if exist "%TMP%" rmdir /S /Q "%TMP%"
mkdir "%TMP%"
mkdir "%TMP%\lib"
cd "%TMP%\lib"
"%JAR%" -xf "%JBOSS_HOME%\server\default\deploy\openquote.ear"
move "lib\*.jar" "%LIB%"
copy "%JBOSS_HOME%\server\default\deploy\jboss-portal.sar\lib\portal-portlet-jsr168api-lib.jar" "%LIB%"
copy "%JBOSS_HOME%\server\default\deploy\jboss-portal.sar\lib\portal-identity-lib.jar" "%LIB%"
	
echo Database setup...

mysql -u root -p < "%LIB%\MySql-Dump.sql"

echo > "%TMP%\setup"

echo OpenQuote setup complete. Starting JBoss...

:SETUP_DONE

cd "%JBOSS_HOME%\bin"
.\run.bat
