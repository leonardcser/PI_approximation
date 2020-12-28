#! /bin/sh

stty raw -echo

cd dev/Java/PI_approximation/

javac -encoding utf8 $(find . -name "*.java") -d bin

java -cp bin com.leo.application.Launch