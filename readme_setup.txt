Prerequisites for running app.
1. Java(JDK) 7+
2. Maven 3.*
3. Apache Tomcat 7
Create folder with Git clone of the project
Place folder("com.kts.unity.bfs.service_settings") with settings files in the same folder where cloned project is located




Configuration.

1.1 Tomcat Authentication
Add an user with roles manager-gui and manager-script.

	%TOMCAT7_PATH%/conf/tomcat-users.xml
	Markup
	<?xml version='1.0' encoding='utf-8'?>
	<tomcat-users>

		<role rolename="manager-gui"/>
		<role rolename="manager-script"/>
		<user username="admin" password="password" roles="manager-gui,manager-script" />

	</tomcat-users>
	
	
1.2 Maven Authentication
Add above Tomcat’s user in the Maven setting file, later Maven will use this user to login Tomcat server.

	%MAVEN_PATH%/conf/settings.xml
	Bash
	<?xml version="1.0" encoding="UTF-8"?>
	<settings ...>
		<servers>
		   
			<server>
				<id>TomcatServer</id>
				<username>admin</username>
				<password>password</password>
			</server>

		</servers>
	</settings>
	
	
1.3 Tomcat7 Maven Plugin
Declares a Maven Tomcat plugin.

	pom.xml
	Markup
		<plugin>
			<groupId>org.apache.tomcat.maven</groupId>
			<artifactId>tomcat7-maven-plugin</artifactId>
			<version>2.2</version>
			<configuration>
				<url>http://localhost:8080/manager/text</url>
				<server>TomcatServer</server>
				<path>/mkyongWebApp</path>
			</configuration>
		</plugin>
How it works?
During deployment, it tells Maven to deploy the WAR file to Tomcat server via “http://localhost:8080/manager/text” , on path “/mkyongWebApp“, 
using “TomcatServer” (in settings.xml) username and password for authentication.

1.4 Deploy to Tomcat
Commands to manipulate WAR file on Tomcat.

	Bash
	mvn tomcat7:deploy 
	mvn tomcat7:undeploy 
	mvn tomcat7:redeploy 
