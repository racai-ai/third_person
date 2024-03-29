#!/bin/sh

cd /site/startup ; php start.php 2>&1 | /usr/sbin/rotatelogs -l /var/log/runner-%Y-%m-%d.log 86400 &

cd /

mkdir -p /run/php-fpm
php-fpm &

/usr/sbin/apachectl -D FOREGROUND


sleep 9000
