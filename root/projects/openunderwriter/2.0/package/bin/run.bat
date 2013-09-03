@echo off

set OPENUNDERWRITER_HOME=%CD%\..
set TMP=%OPENUNDERWRITER_HOME%\tmp
set LIB=%OPENUNDERWRITER_HOME%\lib
set JBOSS_HOME=%OPENUNDERWRITER_HOME%\liferay-portal-@liferay.version@/jboss-@jboss.version@
set PROGNAME=run.bat
set DB_USERNAME=
set DB_PASSWORD=

if exist "%TMP%\setup" goto setupdone

echo.
echo OpenUnderwriter Database Setup
echo ==============================
echo The first time that you run OpenUnderwriter, this script will create a database in 
echo MySQL and populate it with content, it will also create an openquote database
echo user. To do this successfully the script will need the username and password 
echo of a user who has the necessary permissions to create database and users.
echo This user will only be used to run the scripts. The OpenUnderwriter server itself
echo will use the less privileged user created by the scripts.
echo. 
echo You will not be prompted for these user details again.
echo. 

set /p DB_USERNAME="Please enter you MySQL username (default: root):"
set /p DB_PASSWORD="Please enter your MySQL password (leave blank for no password):"
    
if "x%DB_USERNAME%" == "x" (set DB_USERNAME="root") 
if "x%DB_PASSWORD%" == "x" (set PW_OPTION="") else (set PW_OPTION=--password=%DB_PASSWORD%) 

echo.
echo Running database scripts...

mysql -u %DB_USERNAME% %PW_OPTION% < "%LIB%\OpenUnderwriter-MySql-Setup.sql"
if "%ERRORLEVEL%" == "1" (echo "Failed to execute the MySQL database setup script." & goto:eof)
mysql -u %DB_USERNAME% %PW_OPTION% < "%LIB%\OpenUnderwriter-Table-Setup.sql"
if "%ERRORLEVEL%" == "1" (echo "Failed to execute the MySQL OpenUnderwriter table setup script." & goto:eof)
    
mkdir %TMP%
echo > %TMP%\setup
    
echo OpenUnderwriter setup complete. Starting JBoss...

:setupdone

cd "%JBOSS_HOME%\bin"
.\standalone.bat
