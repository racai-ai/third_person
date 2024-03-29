#!/bin/sh

docker build --tag thirdperson .


docker rmi -f $(docker images -q --filter label=stage=intermediate)
