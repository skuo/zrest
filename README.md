zrest
=====

-----
Jetty
-----

Documentation http://www.eclipse.org/jetty/documentation/current/

Build
-----

mvn clean install -P local

Running Jetty using jetty-runner
-------------------------------

java -jar ~/.m2/repository/org/eclipse/jetty/jetty-runner/9.0.0.v20130308/jetty-runner-9.0.0.v20130308.jar --path /zrest-web target/zrest-web-${project.version}.war

Running Jetty using shell script
-------------------------------

cd target; tar -zxvf zrest-${project.version}.tar.gz

cd zrest-${project.version}

./bin/startZRest.sh # to start

./bin/stopZRest.sh  # to stop

Development
-----------
./runJetty # debug port at 4000, jetty listens at 8080

Fabric
-----------
fab build_and_debug # debug port at 4000, jetty listens at 8080

Postman
-----------
http://localhost:8080/zrest-web/uploadFile #params: file and name

Swagger-SpringMvc
-----------
http://localhost:8080/zrest-web/api-docs
