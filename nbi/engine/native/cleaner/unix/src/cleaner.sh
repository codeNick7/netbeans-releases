#!/bin/sh
# 
# The contents of this file are subject to the terms of the Common Development and
# Distribution License (the License). You may not use this file except in compliance
# with the License.
# 
# You can obtain a copy of the License at http://www.netbeans.org/cddl.html or
# http://www.netbeans.org/cddl.txt.
# 
# When distributing Covered Code, include this CDDL Header Notice in each file and
# include the License file at http://www.netbeans.org/cddl.txt. If applicable, add
# the following below the CDDL Header, with the fields enclosed by brackets []
# replaced by your own identifying information:
# 
#     "Portions Copyrighted [year] [name of copyright owner]"
# 
# The Original Software is NetBeans. The Initial Developer of the Original Software
# is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun Microsystems, Inc. All
# Rights Reserved.

deleteFiles() {
	waitOnError=1
	tryTimes=2
	list="$1"
	if [ -n "$list" ] && [ -f "$list" ] ; then
		#echo "Using list file : $list"
		itemsNumber=`wc -l $list | sed "s/^\ *//;s/\ .*//" 2>/dev/null`
		#echo "Total items : $itemsNumber"
		counter=1
		try=$tryTimes
		while [ $counter -le $itemsNumber ] ; do			
			file=`sed -n "${counter}p" "$list"`
			#echo "entry : $file"
			result=1
			if [ -n "$file" ] ; then
				#echo "... file not zero"
				if [ -L "$file" ] || [ -f "$file" ] ; then
					# file or symlink
					#echo "deleting [F] $file"
					rm -f "$file" 2>/dev/null 1>&2
					if [ $? -ne 0 ] ; then
						#echo "... can't delete $file"
						result=0
					fi 
				elif [ -d "$file" ] ; then
					# directory
					#echo "deleting [D] $file"
					rmdir "$file" 2>/dev/null 1>&2
					if [ $? -ne 0 ] ; then 
						result=0
						#echo "... can't delete $file"
					fi
				fi
			fi
			if [ 0 -eq $result ] ; then
				# try to remove it again after a short wait
				if [ $try -gt 0 ] ; then	
					try=`expr "$try" - 1`
					sleep $waitOnError
				else
					#can`t delete.. skip it
					result=1
				fi
				
			fi
			if [ 1 -eq $result ] ; then
				counter=`expr "$counter" + 1`
				try=$tryTimes
			fi		
		done
		if [ -f "$list" ] ; then 
			#echo "... remove cleaner list $list"
			rm -f "$list"
		fi		
	fi
	if [ -f "$0" ] ; then 
		#echo "... remove cleaner itself $0"
		rm -f "$0"
	fi
}


deleteFiles "$@"