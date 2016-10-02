#!/bin/bash
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
if [ "x${OP:0:1}" != "x2" ]
then
OP=`identify -format %[EXIF:DateTime] "$line"|sed 's/://g;s/ //g'`
fi
if [ "x${OP:0:1}" != "x2" ]
then
OP=`identify -format %[EXIF:DateTimeDigitized] "$line"|sed 's/://g;s/ //g'`
fi
FILNAME=`basename "$line"`
aPATH=`dirname "$line"` 
ALBUM=`basename "$aPATH"`
echo :$ALBUM::$FILNAME:$OP
done<$TMP_DIR/Images.txt

