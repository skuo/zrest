JVM_MEM=1700M
JAVA_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,address=4000,server=y,suspend=y "

echo "Debugging zrest-web (MEM = $JVM_MEM $JAVA_OPTS )"
$JAVA_HOME/bin/java -server -Xms$JVM_MEM -Xmx$JVM_MEM $JAVA_OPTS -jar lib/jetty-runner-${jetty.version}.jar --path /zrest-web zrest-web-${project.version}.war

