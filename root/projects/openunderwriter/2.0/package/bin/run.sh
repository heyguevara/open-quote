#!/bin/sh

# Copyright Applied Industrial Logic Limited 2014. All rights Reserved
#
# This program is free software; you can redistribute it and/or modify it under
# the terms of the GNU General Public License as published by the Free Software
# Foundation; either version 2 of the License, or (at your option) any later 
# version.
#
# This program is distributed in the hope that it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
# FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
# more details.
#
# You should have received a copy of the GNU General Public License along with
# this program; if not, write to the Free Software Foundation, Inc., 51 
# Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

OPENUNDERWRITER_HOME=`dirname $PWD/$0`/..
BIN=$OPENUNDERWRITER_HOME/bin
TMP=$OPENUNDERWRITER_HOME/tmp
LIB=$OPENUNDERWRITER_HOME/lib
JBOSS_HOME=$OPENUNDERWRITER_HOME/liferay-portal-@liferay.version@/jboss-@jboss.version@
PROGNAME=`basename $0`

if [ ! -f "$TMP/setup" ]; then
	
    echo ""
    echo "OpenUnderwriter Database Setup"
    echo "=============================="
    echo "The first time that you run OpenUnderwriter, this script will create databases in" 
    echo "MySQL and populate them with content, it will also create an OpenUnderwriter database"
    echo "user. To do this successfully the script will need the username and password" 
    echo "of a user who has the necessary permissions to create database and users."
    echo "This user will only be used to run the scripts. The OpenUnderwriter server itself"
    echo "will use the less privileged user created by the scripts."
    echo ""
    echo "You will not be prompted for these user details again."
    echo ""
	
    read -p "Please enter your MySQL username (default: root): " DB_USERNAME
	read -p "Please enter your MySQL password (leave blank for no password): " DB_PASSWORD

    [ "x$DB_USERNAME" = "x" ] && DB_USERNAME="root"
    [ "x$DB_PASSWORD" != "x" ] && PW_OPTION="--password=$DB_PASSWORD"
    
	SETUP_CLASSPATH=$SETUP_CLASSPATH:$JBOSS_HOME/modules/com/ail/insurance/main/insurance.jar
	SETUP_CLASSPATH=$SETUP_CLASSPATH:$JBOSS_HOME/modules/com/ail/core/main/core.jar
	SETUP_CLASSPATH=$SETUP_CLASSPATH:$JBOSS_HOME/modules/com/liferay/portal/main/mysql.jar
	SETUP_CLASSPATH=$SETUP_CLASSPATH:$JBOSS_HOME/modules/com/ail/core/main/bsh-2.0b5.jar
	
	echo
	echo "Checking environment..."
	
	java -cp $SETUP_CLASSPATH bsh.Interpreter $BIN/port-checker.bsh "$DB_USERNAME" "$DB_PASSWORD"
	[ "$?" = "1" ] && exit 1

    echo "Running database scripts..."

	cd $LIB
   	mysql -u $DB_USERNAME $PW_OPTION < $LIB/Master-Setup.sql
    [ "$?" = "1" ] && cd $BIN && echo "Failed to execute the MySQL database setup script." && exit 1

	cd $BIN
	java -cp $SETUP_CLASSPATH com.ail.insurance.actuarial.DataGenerator \
		com.mysql.jdbc.Driver \
		jdbc:mysql://localhost/openunderwriter_2_0_AIL_Base_DataSource_Master_Motor \
		"$DB_USERNAME" \
		"$DB_PASSWORD"

	mkdir $TMP 2>/dev/null
	touch $TMP/setup
	
	echo "OpenUnderwriter setup complete. Starting JBoss..."
fi

cd $JBOSS_HOME/bin
./standalone.sh
