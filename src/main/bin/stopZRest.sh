echo "Stoppting zrest-Web "

$JAVA_HOME/bin/java -jar lib/start-${jetty.version}.jar -DSTOP.PORT=8181 -DSTOP.KEY=zrestStop --stop
