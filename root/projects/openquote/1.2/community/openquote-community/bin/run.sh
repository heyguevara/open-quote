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
	log "Running OpenQuote setup..."

    if [ "x$JAVA_HOME" != "x" ]; then
        JAR="$JAVA_HOME/bin/jar"
    else
        JAR="jar"
    fi

	log "Extracting jars for product development..."

	rm -rf $TMP/*
	cd $TMP
	$JAR -xf $JBOSS_HOME/server/default/deploy/openquote.ear lib
	mv $TMP/lib/*.jar $LIB
	cp $JBOSS_HOME/server/default/deploy/jboss-portal.sar/lib/portal-portlet-jsr168api-lib.jar $LIB
	cp $JBOSS_HOME/server/default/deploy/jboss-portal.sar/lib/portal-identity-lib.jar $LIB
	
	log "Database setup..."

	read -s -p "Please enter your MySQL password:" pw
	mysql -u root --password=$pw < $LIB/MySql-Dump.sql

	touch $TMP/setup

	log "OpenQuote setup complete. Starting JBoss..."
fi

cd $JBOSS_HOME/bin
./run.sh
