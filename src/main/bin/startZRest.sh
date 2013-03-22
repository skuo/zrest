JVM_MEM=1700M

echo "Starting zrest-Web (MEM = -server -Xms$JVM_MEM -Xmx$JVM_MEM)"

$JAVA_HOME/bin/java -server -Xms$JVM_MEM -Xmx$JVM_MEM -jar lib/jetty-runner-${jetty.version}.jar --config conf/zrest-jetty.xml --stop-port 8181 --stop-key zrestStop --path /zrest-web zrest-web-${project.version}.war > console.log 2>&1 &
