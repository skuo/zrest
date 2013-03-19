JVM_MEM=1700M

echo "Starting zrest-Web (MEM = -server -Xms$JVM_MEM -Xmx$JVM_MEM)"
#$JAVA_HOME/bin/java -server -Xms$JVM_MEM -Xmx$JVM_MEM -cp  "$CP" $MAIN_CLASS $@ > console.log 2>&1 &
#echo $! >royalty.pid

$JAVA_HOME/bin/java -server -Xms$JVM_MEM -Xmx$JVM_MEM -jar lib/jetty-runner-${jetty.version}.jar --path /zrest-web zrest-web-${project.version}.war
