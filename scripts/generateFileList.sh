if [ $# -ne 1 ]
then
echo "Usage: $0 [Directory]"
exit
fi

BASE_DIR=$1
TMP_DIR=/tmp

find $BASE_DIR -regex ".*\.\(JPG\|jpg\|gif\|PNG\|png\|jpeg\)">$TMP_DIR/Images.txt
while read line
do
OP=`identify -format %[EXIF:DateTimeOriginal] "$line"|sed 's/://g;s/ //g'`
FILNAME=`basename "$line"`
aPATH=`dirname "$line"` 
ALBUM=`basename "$aPATH"`
echo :$ALBUM::$FILNAME:$OP
done<$TMP_DIR/Images.txt
#find $BASE_DIR -type d>$TMP_DIR/dirs.txt
#while read line
#do
#count=`ls "$line"/*JPG "$line"/*jpg|wc -l 2>/dev/null`
#if [ "$count" != 0 ]
#then
#export IFS="="
#identify -format %f:%[EXIF:DateTimeOriginal]\\n $line/*jpg $line/*.JPG|xargs -n 1  echo $line
#fi
#done <$TMP_DIR/dirs.txt
