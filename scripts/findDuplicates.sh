BASE_DIR=..
INPUT=$BASE_DIR/output/dumpText.txt
OUTPUT=$BASE_DIR/output/DuplicateFiles.txt

while read -r line
do
#set -x
photo=`echo $line|awk -F: '{print $4}'`
timestamp=`echo $line|awk -F: '{print $5}'`
count=`grep -c "$photo:$timestamp" $INPUT`  
if [ $count -gt 1 ]
then
export OIFS=$IFS
export IFS=:
printf "%s\n" "$line"
export IFS=$OIFS
fi
#set +x
done<$INPUT>tmp_duplicates.txt
cat tmp_duplicates.txt |sort |uniq >$OUTPUT
