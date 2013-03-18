zrest
=====

-----
Jetty
-----

Documentation http://www.eclipse.org/jetty/documentation/current/

Build

mvn clean install -P local

Running Jetty using jetty-runner

java -jar ~/.m2/repository/org/eclipse/jetty/jetty-runner/9.0.0.v20130308/jetty-runner-9.0.0.v20130308.jar --path /zrest-web target/zrest-web.war