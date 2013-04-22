#!/bin/sh

OPENQUOTE_HOME=`dirname $PWD/$0`/..
TMP=$OPENQUOTE_HOME/tmp
LIB=$OPENQUOTE_HOME/lib
JBOSS_HOME=$OPENQUOTE_HOME/jboss
PROGNAME=`basename $0`

log() {
    echo "${PROGNAME}: $*"
}

if [ ! -f "$TMP/setup" ]; then
	log "Database OpenQuote database setup..."

	read -p "Please enter your MySQL password: " pw

	log "\nRunning database script..."

	mysql -u root --password=$pw < $LIB/MySql-Dump.sql

	touch $TMP/setup

	log "OpenQuote setup complete. Starting JBoss..."
fi

cd $JBOSS_HOME/bin
./run.sh
