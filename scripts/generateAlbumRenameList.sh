getMonthName()
{
case $1 in
01)
echo "Jan";;
02)
echo "Feb";;
03)
echo "Mar";;
04)
echo "Apr";;
05)
echo "May";;
06)
echo "Jun";;
07)
echo "Jul";;
08)
echo "Aug";;
09)
echo "Sep";;
10)
echo "Oct";;
11)
echo "Nov";;
12)
echo "Dec";;
*)
echo "NONE: $1";;
esac
}
getProposedAlbum()
{
YEAR=`echo $1|cut -b1-4`
MONTH=`echo $1|cut -b5-6`
MONTHNAME=`getMonthName $MONTH`
echo ${YEAR}_${MONTHNAME}
set +x
}
rm RenameAlbums.txt
#set -x
for line in `cat safe/dumpText.txt|egrep ":2015_|:2016_"|awk -F: '{print $4":"$5}'`; do 
	photo=`echo $line|awk -F: '{print $2}'`
	proposedAlbum=`getProposedAlbum $photo`
#	echo Proposed: $proposedAlbum
	FOUND=0
	ENTRIES=`grep $line safe/dumpText.txt`; 
	for row in $ENTRIES; do 
		album=`echo $row|awk -F: '{print $2}'`; 
		if [ $album != $proposedAlbum ]
		then
			echo $row
		else
			(( FOUND += 1 ))
			if [ $FOUND -gt 1 ]
			then
				echo $row
			fi
		fi
	done; 
	if [ $FOUND -eq 0 ]
	then
	    echo "WARNING: PHOTO $line NOT INSIDE $proposedAlbum. Run RenameAlbums first "
	    echo $row|sed "s/$album/$proposedAlbum/g">>RenameAlbums.txt
	fi
done
set +x
