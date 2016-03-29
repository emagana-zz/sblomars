#!/bin/sh
# Copying Graphs to SBLOMARS Web Server
while :
do
sleep 60
scp -r /path_to_Sblomars/Sblomars/database/*.png user@host:/var/www/database/
done
