#!/bin/sh
# Copying SBLOMARS in Emanisc-Lab Nodes 
cat nodes.txt | while read node
do
scp -r /path_to_sblomars/Sblomars user@$node:/home/upc_sblomars/

done
