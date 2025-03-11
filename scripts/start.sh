#!/bin/bash

ROOT_PATH="/home/ubuntu/spring-github-action"
JAR="$ROOT_PATH/application.jar"

APP_LOG="$ROOT_PATH/application.log"
ERROR_LOG="$ROOT_PATH/error.log"
START_LOG="$ROOT_PATH/start.log"

NOW=$(date +%c)

echo "[$NOW] 실행 중인 애플리케이션 종료" >> $START_LOG
# 실행 중인 프로세스 종료
if pgrep -f $JAR > /dev/null
then
    echo "[$NOW] 기존 애플리케이션 종료" >> $START_LOG
    pkill -f $JAR
    sleep 5
fi

echo "[$NOW] 최신 JAR 복사" >> $START_LOG
cp $ROOT_PATH/build/libs/capstone-0.0.1-SNAPSHOT.jar $JAR

echo "[$NOW] 애플리케이션 실행" >> $START_LOG
nohup java -jar $JAR > $APP_LOG 2> $ERROR_LOG &

SERVICE_PID=$(pgrep -f $JAR)
echo "[$NOW] 서비스 PID: $SERVICE_PID" >> $START_LOG
