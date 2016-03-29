#!/bin/sh
javac -classpath /path_to_Sblomars/Sblomars/lib/AdventNetSnmp.jar:/path_to_Sblomars/Sblomars/lib/rrd4j-2.0.1.jar:. source/*.java
java -classpath /path_to_Sblomars/Sblomars/lib/AdventNetSnmp.jar:/path_to_Sblomars/Sblomars/lib/rrd4j-2.0.1.jar:. source/PrincipalAgentDeployer snmpd.conf 20 3 60 5 60 5 60 5 3600000 0 0 604800000 >/dev/null &
./images.sh >/dev/null & 
