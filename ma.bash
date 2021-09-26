#!/bin/env bash
# Copyright 2019 (c) all rights reserved 
# by BuildAPKs https://buildapks.github.io
#####################################################################
set -Eeuo pipefail
shopt -s nullglob globstar
. "$RDR"/scripts/bash/shlibs/trap.bash 210 211 212
cd "$JDR"
# _AT_ username/repository commit
_AT_ aporter/coursera-android 157373885fbfa18b83fa97cd46f6a003905970ea
_AT_ BullShark/AndroidTutorial f5b3ea08ab9dce1b78a67b71490dc45afe275199
# ma.bash OEF 
