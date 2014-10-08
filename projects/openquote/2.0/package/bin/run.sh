#!/bin/sh

OPENQUOTE_HOME=`dirname $PWD/$0`/..
TMP=$OPENQUOTE_HOME/tmp
LIB=$OPENQUOTE_HOME/lib
JBOSS_HOME=$OPENQUOTE_HOME/liferay-portal-@liferay.version@/jboss-@jboss.version@
PROGNAME=`basename $0`

if [ ! -f "$TMP/setup" ]; then
	
    echo ""
    echo "OpenQuote Database Setup"
    echo "========================"
    echo "The first time that you run OpenQuote, this script will create a database in" 
    echo "MySQL and populate it with content, it will also create an openquote database"
    echo "user. To do this successfully the script will need the username and password" 
    echo "of a user who has the necessary permissions to create database and users."
    echo "This user will only be used to run the scripts. The OpenQuote server itself"
    echo "will use the less privileged user created by the scripts."
    echo ""
    echo "You will not be prompted for these user details again."
    echo ""
	
    read -p "Please enter you MySQL username (default: root): " DB_USERNAME
	read -p "Please enter your MySQL password (leave blank for no password): " DB_PASSWORD

    [ "x$DB_USERNAME" = "x" ] && DB_USERNAME="root"
    [ "x$DB_PASSWORD" != "x" ] && PW_OPTION="--password=$DB_PASSWORD"
    
    echo
    echo "Running database script..."

   	mysql -u $DB_USERNAME $PW_OPTION < $LIB/OpenQuote-MySql-Setup.sql
    [ "$?" = "1" ] && echo "Failed to execute the MySQL database setup script."  && exit 1
    mysql -u $DB_USERNAME $PW_OPTION < $LIB/OpenQuote-Table-Setup.sql
    [ "$?" = "1" ] && echo "Failed to execute the MySQL OpenQuote table setup script." && exit 1 

	mkdir $TMP 2>/dev/null
	touch $TMP/setup

	echo "OpenQuote setup complete. Starting JBoss..."
fi

cd $JBOSS_HOME/bin
./standalone.sh
