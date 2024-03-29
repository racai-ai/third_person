#!/bin/sh

echo "Testing the WebService Docker with CURL"

echo "schedule"
curl -X POST  -F "input={}" http://127.0.0.1:8111/schedule.php
echo ""

echo "getResult"
curl -X POST  -F "input={\"id\":\"JOBID\"}" http://127.0.0.1:8111/getResult.php
echo ""

echo "getResult"
curl -X GET  "http://127.0.0.1:8111/getResult.php?input=\{\"id\":\"JOBID\"\}"
echo ""

echo "schedule"
curl -X POST  -F "input={}" http://127.0.0.1:8111/schedule.php
echo ""

echo "checkHealth"
curl -X GET  http://127.0.0.1:8111/checkHealth.php
echo ""

echo "getVersion"
curl -X GET http://127.0.0.1:8111/getVersion.php
echo ""


