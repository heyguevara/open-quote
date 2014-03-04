@echo off

@rem Copyright Applied Industrial Logic Limited 2014. All rights Reserved
@rem
@rem This program is free software; you can redistribute it and/or modify it under
@rem the terms of the GNU General Public License as published by the Free Software
@rem Foundation; either version 2 of the License, or (at your option) any later 
@rem version.
@rem
@rem This program is distributed in the hope that it will be useful, but WITHOUT
@rem ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
@rem FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
@rem more details.
@rem
@rem You should have received a copy of the GNU General Public License along with
@rem this program; if not, write to the Free Software Foundation, Inc., 51 
@rem Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

set OPENUNDERWRITER_HOME=%CD%\..
set TMP=%OPENUNDERWRITER_HOME%\tmp
set LIB=%OPENUNDERWRITER_HOME%\lib
set BIN=%OPENUNDERWRITER_HOME%\bin
set JBOSS_HOME=%OPENUNDERWRITER_HOME%\liferay-portal-@liferay.version@/jboss-@jboss.version@
set PROGNAME=run.bat
set DB_USERNAME=
set DB_PASSWORD=

if exist "%TMP%\setup" goto setupdone

echo.
echo OpenUnderwriter Database Setup
echo ==============================
echo The first time that you run OpenUnderwriter, this script will create databases in 
echo MySQL and populate them with content, it will also create an OpenUnderwriter database
echo user. To do this successfully the script will need the username and password 
echo of a user who has the necessary permissions to create database and users.
echo This user will only be used to run the scripts. The OpenUnderwriter server itself
echo will use the less privileged user created by the scripts.
echo. 
echo You will not be prompted for these user details again.
echo. 

set /p DB_USERNAME="Please enter your MySQL username (default: root):"
set /p DB_PASSWORD="Please enter your MySQL password (leave blank for no password):"
    
if "x%DB_USERNAME%" == "x" (set DB_USERNAME="root") 
if "x%DB_PASSWORD%" == "x" (set PW_OPTION="") else (set PW_OPTION=--password=%DB_PASSWORD%) 

echo.
echo Running database scripts...

cd %LIB%
mysql -u %DB_USERNAME% %PW_OPTION% < "%LIB%\Master-Setup.sql"
if "%ERRORLEVEL%" == "1" (cd %BIN% & echo "Failed to execute the MySQL database setup script." & goto:eof)
    
cd %BIN%
set GEN_CLASSPATH=%GEN_CLASSPATH%;%JBOSS_HOME%\modules\com\ail\insurance\main\insurance.jar
set GEN_CLASSPATH=%GEN_CLASSPATH%;%JBOSS_HOME%\modules\com\ail\core\main\core.jar
set GEN_CLASSPATH=%GEN_CLASSPATH%;%JBOSS_HOME%\modules\com\liferay\portal\main\mysql.jar

java -cp %GEN_CLASSPATH% com.ail.insurance.actuarial.DataGenerator ^
	com.mysql.jdbc.Driver ^
	jdbc:mysql://localhost/openunderwriter_2_0_AIL_Base_DataSource_Master_Motor ^
	"%DB_USERNAME%" ^
	"%DB_PASSWORD%"

mkdir %TMP%
echo > %TMP%\setup
    
echo OpenUnderwriter setup complete. Starting JBoss...

:setupdone

cd "%JBOSS_HOME%\bin"
.\standalone.bat
