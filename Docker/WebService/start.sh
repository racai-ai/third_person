#!/bin/sh

mkdir -p /data/tmp/thirdperson-run/logs/apache2
mkdir -p /data/tmp/thirdperson-run/data
mkdir -p /data/tmp/thirdperson-run/data/tasks
mkdir -p /data/tmp/thirdperson-run/data/tasks/new
mkdir -p /data/tmp/thirdperson-run/data/tasks/done
mkdir -p /data/tmp/thirdperson-run/data/tasks/run
mkdir -p /data/tmp/thirdperson-run/data/cases

if [ ! -f /data/tmp/thirdperson-run/data/config.json ]; then
    cp test/config.json /data/tmp/thirdperson-run/data/config.json
fi

if [ ! -f /data/tmp/thirdperson-run/data/udpipe.model.ud ]; then
    cp test/udpipe.model.ud /data/tmp/thirdperson-run/data/udpipe.model.ud
fi

if [ ! -f /data/tmp/thirdperson-run/data/rolex.txt ]; then
    cp test/rolex.zip /data/tmp/thirdperson-run/data/rolex.zip
    current=$(pwd)
    cd /data/tmp/thirdperson-run/data ; unzip rolex.zip
    cd $current
fi

docker run --name "thirdperson-run" -d -p=8111:80 \
    -v /data/tmp/thirdperson-run/logs:/var/log \
    -v /data/tmp/thirdperson-run/data:/data \
    thirdperson

docker ps

