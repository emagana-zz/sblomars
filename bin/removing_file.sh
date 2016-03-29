for node in `cat nodes.txt | uniq` ; do

 ssh user@$node "cd /path_to_Sblomars/Sblomars && rm -rf $1 > /dev/null" &

done
