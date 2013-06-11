== Spring Sample Application using Redis and PostgreSQL Services


Just follow these steps to deploy the appliation:

1)> cf target
https://api.de.a9s.eu
>> Setting target to https://api.de.a9s.eu... OK

2)> cf login
>> target: https://api.de.a9s.eu

Email> user@example.com
Password> **********
>> Authenticating... OK


3)> mvn package

>> Maven Build Output

4)> cf push --path target/web-1.0.0.war

Name> springmvc

Instances> 1

Custom startup command> none

1: 64M
2: 128M
3: 256M
4: 512M
5: 1G
Memory Limit> 4

Creating springmvc... OK

1: springmvc
2: none
Subdomain> springmvc

1: de.a9sapp.eu
2: none
Domain> de.a9sapp.eu

Binding springmvc.de.a9sapp.eu to springmvc... OK

Create services for application?> y

1: mongodb 2.0
2: mysql 5.5
3: postgresql 9.0
4: rabbitmq 2.8
5: redis 2.2
What kind?> 5

Name?> redis-springmvc

1: 100: Shared server, shared VM, 1MB memory, 10MB storage, 10
connections
Which plan?> 1

Creating service redis-springmvc... OK
Binding redis-springmvc to springmvc... OK
Create another service?> y

1: mongodb 2.0
2: mysql 5.5
3: postgresql 9.0
4: rabbitmq 2.8
5: redis 2.2
What kind?> 3

Name?> postgresql-springmvc

1: 100: Shared server, shared VM, 1MB memory, 10MB storage, 10
connections
Which plan?> 1

Creating service postgresql-springmvc... OK
Binding postgresql-springmvc to springmvc... OK
Create another service?> n

Bind other services to application?> n

Save configuration?> y

Saving to manifest.yml... OK
Uploading springmvc... OK
Starting springmvc... OK
-----> Downloaded app package (19M)
Installing java.
Downloading JDK...
Copying openjdk-1.7.0_21.tar.gz from the buildpack cache ...
Unpacking JDK to .jdk
Downloading Tomcat: apache-tomcat-7.0.40.tar.gz
Downloading apache-tomcat-7.0.40.tar.gz from
http://archive.apache.org/dist/tomcat/tomcat-7/v7.0.40/bin/ ...
Unpacking Tomcat to .tomcat
Copying mysql-connector-java-5.1.12.jar from the buildpack cache ...
Copying postgresql-9.0-801.jdbc4.jar from the buildpack cache ...
Copying auto-reconfiguration-0.6.6.jar from the buildpack cache ...
-----> Uploading staged droplet (126M)
-----> Uploaded droplet
Checking springmvc...
Staging in progress...
Staging in progress...
  0/1 instances: 1 starting
  0/1 instances: 1 starting
  0/1 instances: 1 starting
  0/1 instances: 1 starting
  0/1 instances: 1 starting
  0/1 instances: 1 starting
  1/1 instances: 1 running
OK

5)> cf apps
Getting applications in development... OK

name        status    usage      url
caldecott   running   1 x 128M   caldecott-4dc80.de.a9sapp.eu
notes       running   1 x 256M   notes.de.a9sapp.eu
springmvc   running   1 x 512M   springmvc.de.a9sapp.eu

6) Open the url specified for the application in your web browser.
