#!/bin/bash
#cat PictureList.Flickr.txt |grep -i Cochin-2015|sed 's/Cochin-2015/Bangalore Days\\Cochin-2015/'
set -x
cat PictureList.Flickr.txt |grep -i ":$1:"|sed "s/:$1:/:$2\\\\$1:/"