#!/bin/env bash
# Copyright 2019 (c) all rights reserved by S D Rausty; see LICENSE  
# https://sdrausty.github.io hosted courtesy https://pages.github.com
# To create checksum files and commit use; ./do.sums.bash
# To see file tree use; awk '{print $2}' sha512.sum
# To check the files use; sha512sum -c sha512.sum
#####################################################################
set -eu
git pull
rm -f *.sum
FILELIST=( $(find . -type f | grep -v .git | sort) )
CHECKLIST=(sha512sum) # md5sum sha1sum sha224sum sha256sum sha384sum
for SCHECK in ${CHECKLIST[@]}
do
	printf "\\e[1;38;5;118m%s\\e[0m\\n" "Creating $SCHECK file..."
	for FILE in "${FILELIST[@]}"
	do
		$SCHECK "$FILE" >> ${SCHECK::-3}.sum
	done
done
chmod 400 ${SCHECK::-3}.sum 
for SCHECK in  ${CHECKLIST[@]}
do
	printf "\\e[1;38;5;119m%s\\e[0m\\n" "Checking $SCHECK..."
	$SCHECK -c ${SCHECK::-3}.sum
done
git add .
git commit
git push
ls
printf "\\e[1;38;5;120m%s\\e[0m\\n" "$PWD"
# do.sums.sh EOF
