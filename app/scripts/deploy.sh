#!/usr/bin/env bash

APP_NAME=app

# 배포 파일 경로 이동
REPOSITORY=/home/ubuntu
cd $REPOSITORY

# 1. 압축 해제
echo "압축 해제 시작..."
unzip -o $REPOSITORY/duri-aws-build.zip -d $REPOSITORY/
echo "압축 해제 완료!"

# 2. JAR 파일 찾기
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

# 3. 현재 실행 중인 프로세스 찾아서 종료
CURRENT_PID=$(pgrep -f $APP_NAME)
if [ -z $CURRENT_PID ]
then
  echo "> 종료할 애플리케이션이 없습니다."
else
  echo "> kill -9 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

# 4. 새로운 JAR 파일 실행
echo "> Deploy - $JAR_PATH "
nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &