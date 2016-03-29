#!/bin/sh
# Copying SBLOMARS in Emanisc-Lab Nodes 
cat nodes.txt | while read node
do
ssh user@$node "killall -9 java" >/dev/null &
ssh user@$node "killall -9 images.sh" >/dev/null &
ssh user@$node "killall -9 sleep" >/dev/null &
done
