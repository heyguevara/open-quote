@echo off

set OPENQUOTE_HOME=%CD%\..
set JBOSS_HOME=%OPENQUOTE_HOME%\jboss

cd %JBOSS_HOME%\bin
.\shutdown.bat
