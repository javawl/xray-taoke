#!/bin/bash
cd `dirname $0`
USERDIR=`pwd`
USERDIR=`dirname ${USERDIR}`
USERDIR=${USERDIR}/webroot
CLASSPATH="${USERDIR}/WEB-INF/classes:${USERDIR}/WEB-INF/lib/*"
java -classpath ${CLASSPATH} com.elc.core.test.TestTask $1
