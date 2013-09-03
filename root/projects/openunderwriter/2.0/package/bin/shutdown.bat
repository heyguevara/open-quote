@echo off

set OPENUNDERWRITER_HOME=%CD%\..
set JBOSS_HOME=%OPENUNDERWRITER_HOME%\jboss

cd %JBOSS_HOME%\bin
.\shutdown.bat
