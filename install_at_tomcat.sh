#!/bin/bash

MIN_JAVA_VERSION=1.7
TOMCAT_VERSION=1.7
TOMCAT_HOME_DIRECTORY="C:/dev_tools/apache-tomcat-7.0.69"
APP_CONTEXT_NAME_AT_TOMCAT="rct_serv"

UI_SEPARATOR="____________________________________________"

# declare intro text variable
INTRO_TEXT="Launching script for BFS app installation"
echo $INTRO_TEXT

echo $UI_SEPARATOR

# Check if Java installed
echo Minimum Java version required - $MIN_JAVA_VERSION
if type -p java; then
    echo found java executable in PATH
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    echo found java executable in JAVA_HOME     
    _java="$JAVA_HOME/bin/java"
else
    echo "ERROR - no Java installed"
	exit
fi

if [[ "$_java" ]]; then
    version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    echo Installed Java version : "$version"
    if [[ "$version" > "$MIN_JAVA_VERSION" ]]; then
        echo Installed Java version - OK !!!
    else         
        echo ERROR - version is less than $MIN_JAVA_VERSION - Please update your Java
		exit
    fi
fi
# Finished checking Java version

echo $UI_SEPARATOR

#Check if TOMCAT is available
echo Checking TOMCAT
if [ -d "$TOMCAT_HOME_DIRECTORY" ]
then
	echo "Tomcat home directory found - OK !!!"
else
	echo "ERROR !!! - Tomcat home directory $TOMCAT_HOME_DIRECTORY not found !!! - Define folder location of your Tomcat installation at TOMCAT_HOME_DIRECTORY variable of this script"
	exit
fi

echo $UI_SEPARATOR


echo Stopping Tomcat
STOP_TOMCAT_ACTION="$TOMCAT_HOME_DIRECTORY/bin/shutdown.sh"
echo $STOP_TOMCAT_ACTION
$STOP_TOMCAT_ACTION
echo "Tomcat Stopped. If Tomcat is not started, there are errors above. No worries ... Tomcat will be started automatically once solution is built."


#Tomcat stopped

echo $UI_SEPARATOR




SETTINGS_PROJECT_PATH="../com.kts.unity.bfs.service_settings"
if [ -d "$SETTINGS_PROJECT_PATH" ]
then
	echo "Settings project found - OK !!!"
else
	echo "ERROR !!! - $SETTINGS_PROJECT_PATH_ not found !!! - this project/folder and its content is mandatory for successful build"
	exit
fi

echo Checking "Settings" folder of the project
echo INFO - "settings" folder in this project copied from another project - If you need to make any changes, make them in root project !!!
SETTINGS_FOLDER_PATH="src/main/resources/settings"
if [ -d "$SETTINGS_FOLDER_PATH" ]
then
	echo "settings folder found - to be removed"
	REMOVE_SETTINGS_FOLDER_COMMAND="rm -r -f $SETTINGS_FOLDER_PATH"
	echo $REMOVE_SETTINGS_FOLDER_COMMAND
	$REMOVE_SETTINGS_FOLDER_COMMAND
else
	echo "Warning !!! - $SETTINGS_FOLDER_PATH not found !!! - to be created"
fi
MAKE_SETTINGS_FOLDER_COMMAND="mkdir $SETTINGS_FOLDER_PATH"
echo $MAKE_SETTINGS_FOLDER_COMMAND
$MAKE_SETTINGS_FOLDER_COMMAND


echo Copying settings files
COPY_SETTINGS_FILES_COMMAND="cp -a $SETTINGS_PROJECT_PATH/. $SETTINGS_FOLDER_PATH"
echo $COPY_SETTINGS_FILES_COMMAND
$COPY_SETTINGS_FILES_COMMAND


echo $UI_SEPARATOR


echo Maven configuration file
maven_project_config_file="pom.xml"
if [ -f "$maven_project_config_file" ]
then
	echo "$maven_project_config_file found - OK !!!"
else
	echo "ERROR !!! - $maven_project_config_file not found !!! - This script must be run in the same directory where pom.xml file placed"
	exit
fi

echo $UI_SEPARATOR

echo Maven commands

MAVEN_CLEAN_COMMAND="mvn clean"
echo $MAVEN_CLEAN_COMMAND
$MAVEN_CLEAN_COMMAND
echo $UI_SEPARATOR
MAVEN_PACKAGE_COMMAND="mvn package"
echo $MAVEN_PACKAGE_COMMAND
$MAVEN_PACKAGE_COMMAND

echo $UI_SEPARATOR


#Removing context/app artifacts from Tomcat filesystem
echo Removing app context $APP_CONTEXT_NAME_AT_TOMCAT from webapps

REMOVE_APP_FOLDER_FILE_COMMAND="rm -r -f $TOMCAT_HOME_DIRECTORY/webapps/$APP_CONTEXT_NAME_AT_TOMCAT"
echo $REMOVE_APP_FOLDER_FILE_COMMAND
$REMOVE_APP_FOLDER_FILE_COMMAND
REMOVE_APP_WAR_FILE_COMMAND="rm $TOMCAT_HOME_DIRECTORY/webapps/$APP_CONTEXT_NAME_AT_TOMCAT.war"
echo $REMOVE_APP_WAR_FILE_COMMAND
$REMOVE_APP_WAR_FILE_COMMAND

echo $UI_SEPARATOR

echo Starting Tomcat
$TOMCAT_HOME_DIRECTORY/bin/startup.sh

echo $UI_SEPARATOR

echo Deploying app at Tomcat by means of Maven plugin
MAVEN_PACKAGE_COMMAND="mvn tomcat7:redeploy"
echo $MAVEN_PACKAGE_COMMAND
$MAVEN_PACKAGE_COMMAND

echo $UI_SEPARATOR









