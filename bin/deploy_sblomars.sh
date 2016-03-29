#!/bin/sh
# Copying SBLOMARS in Emanisc-Lab Nodes 
cat nodes.txt | while read node
do
ssh user@$node "cd /path_to_Sblomars/Sblomars/ && nohup sh final_sblomars.sh > /dev/null" &
done
