JVM_MEM=1700M
PROJECT_VERSION=0.0.1-SNAPSHOT
JETTY_VER="9.0.0.v20130308"

echo "Starting zrest-Web (MEM = -server -Xms$JVM_MEM -Xmx$JVM_MEM)"
#$JAVA_HOME/bin/java -server -Xms$JVM_MEM -Xmx$JVM_MEM -cp  "$CP" $MAIN_CLASS $@ > console.log 2>&1 &
#echo $! >royalty.pid

java -jar lib/jetty-runner-$JETTY_VER.jar --path /zrest-web zrest-web-0.0.1-SNAPSHOT.war


