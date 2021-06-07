#!/bin/sh

cd ~/drc-offline-service/

if [ ! -f ./log ]; then
    mkdir -p log
fi

DATE_BEGIN=`date -d "yesterday 13:00 " '+%Y-%m-%d-00.00.00.000000'`
DATE_FINISH=`date -d "yesterday 13:00 " '+%Y-%m-%d-23.59.59.999999'`
DRC_OFFLINE_LOG=./log/drcoffline_$DATE_BEGIN.log

touch $DRC_OFFLINE_LOG

java -classpath '*' drcoffline.Main >$DRC_OFFLINE_LOG



