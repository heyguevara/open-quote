#!/bin/sh

OPENUNDERWRITER_HOME=`dirname $PWD/$0`/..
JBOSS_HOME=$OPENUNDERWRITER_HOME/jboss

cd $JBOSS_HOME/bin
. ./shutdown.sh
