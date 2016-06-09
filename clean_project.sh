#!/bin/bash
T_NAME_AT_TOMCAT="rct_serv"

UI_SEPARATOR="____________________________________________"

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
	echo "Info - $SETTINGS_FOLDER_PATH not found !!! - OK"
fi


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
echo CLEAN - completed - OK








