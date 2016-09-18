set +x
for line in `cat duplicates_1.txt`; do 
ENTRIES=`grep $line safe/dumpText.txt`; 
for row in $ENTRIES; do 
album=`echo $row|awk -F: '{print $2}'`; 
printf "$album " 
done; 
echo $line; 
done
