#!/bin/sh

OPENQUOTE_HOME=`dirname $PWD/$0`/..
JBOSS_HOME=$OPENQUOTE_HOME/jboss

cd $JBOSS_HOME/bin
. ./shutdown.sh
