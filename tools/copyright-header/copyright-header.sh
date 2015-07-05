BASEDIR=`dirname $0`
SEPARATOR=" -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+"
for filetype in "xml" "java" "properties"; do
	COPYRIGHTFILE=$BASEDIR/COPYRIGHT.$filetype
	echo "Searching for files .$filetype to apply $COPYRIGHTFILE"
	for each in `find . -name "*.$filetype"`; do
	    # Need to determine if any license text exists before we try to change it
	    EXISTS=`grep -- "$SEPARATOR" $each`
	    if [ "$EXISTS" = "" ]; then
	      # No license exists, just add it to the top, along with an extra blank line
	      (cat $COPYRIGHTFILE; echo; cat $each) > $each.temp
		  echo " Added licence info to file $each"
	    else
	      # License text exists, use sed to remove it
	      (cat $COPYRIGHTFILE; cat $each | sed "1,/$SEPARATOR/d") > $each.temp
	    fi
	    mv $each.temp $each
	done
done



