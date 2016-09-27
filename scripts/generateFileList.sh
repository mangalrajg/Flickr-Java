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
#OP=`identify -format %[EXIF:DateTimeOriginal] "$line"|sed 's/://g;s/ //g'`
OP=`identify -format %[EXIF:DateTime] "$line"|sed 's/://g;s/ //g'`
FILNAME=`basename "$line"`
aPATH=`dirname "$line"` 
ALBUM=`basename "$aPATH"`
echo :$ALBUM::$FILNAME:$OP
done<$TMP_DIR/Images.txt

