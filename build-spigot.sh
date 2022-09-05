
#!/bin/bash

# This script does the build process to download the latest git sources
# and compile them as described at
# http://www.spigotmc.org/threads/bukkit-craftbukkit-spigot-1-8.36598/
#
# Written by Oliver Pientka ( o dot $mySecondName at googlemail dot com )
# Distributed under "creative-common 3.0 by-sa"
# Version 0.2.1 - 2014-12-21
# Written and tested with funtoo linux stage3-core2_64-funtoo-stable-2014-09-27
#
# History:
# v0.1   initial version --> http://pastebin.com/uiQh27Hi
# v0.2   added check for wget and git, expanded this header --> http://pastebin.com/z1PqUQ8q
# v0.2.1 eliminated some typos, added comments


echo "================================================================================"
echo "Checking for dependencies"
echo "================================================================================"
if [ $(which wget) ];then
        echo "found wget ..."
else
        echo "Can't find wget"
        echo "Please make sure wget is installed on your system!"
        exit 1
fi
if [ $(which git) ]; then
        echo "found git ..."
else
        echo "Can't find git"
        echo "Please make sure git is installed on your system!"
        exit 1
fi

if [ $(which javac) ]; then
        echo "found javac ..."
else
        echo "Can't find javac"
        echo "Please make sure you're using a JDK (Java development Kit)"
        echo "instead of an JRE (Java Runntime Enviroment)!"
        echo "Also check your enviroment settings! Your default JVM needs to be the JDK."
        exit 1
fi

# Sorry dudes, can't go into detail within these error messages.
# There are way to many different linux distributions out there, which do this in different ways.
# In doubt, please read your distributions documentation!


if [ -f BuildTools.jar ]; then
        echo "================================================================================"
        echo "Deleting old BuildTools"
        echo "================================================================================"
        rm BuildTools.jar
fi


echo "================================================================================"
echo "Downloading latest BuildTools"
echo "================================================================================"
wget https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar


echo "================================================================================"
echo "Building latest spigot release"
echo "================================================================================"
java -jar BuildTools.jar --rev 1.18.2

echo "================================================================================"
echo "done"
echo "================================================================================"