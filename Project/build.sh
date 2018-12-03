#!/bin/bash
cd `dirname $0`

LIBS=`find lib -name '*.jar' -print`

#
# Compile the program:
#
rm -rf out
mkdir -p out
JAVA_SRC=`find src -name '*.java' -print`
javac -cp out:out/kotlin.jar:$LIBS -d out -Xlint:unchecked -Xmaxerrs 5  \
    -sourcepath src $JAVA_SRC
if [ $? != 0 ] ; then
    exit 1
fi

java -ea -cp $LIBS:out InnReservations "$@"
