for line in `egrep ":2015_|:2016_" safe/dumpText.txt |awk -F: '{print $4":"$5}'`; 
do 
count=`grep -c $line safe/dumpText.txt` ; 
if [ $count -gt 1 ]
then
echo $line
fi
done>duplicates.txt

cat duplicates.txt |sort |uniq >duplicates_1.txt
