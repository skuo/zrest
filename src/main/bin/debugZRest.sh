JVM_MEM=1700M
JAVA_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,address=19999,server=y,suspend=y "

echo "Debugging zrest-web (MEM = $JVM_MEM $JAVA_OPTS )"
$JAVA_HOME/bin/java -server -Xmx$JVM_MEM $JAVA_OPTS -cp  "$CP" $MAIN_CLASS $@ > console.log 2>&1 &
echo $! >royalty.pid
