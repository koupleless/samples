#!/bin/bash

DEST_VERSION=$1

# 如果 DEST_VERSION 为空，则报错
if [ -z "$DEST_VERSION" ]; then
  echo "Error: DEST_VERSION must be provided."
  exit 1
fi

if [[ "$(uname)" == "Darwin" ]]; then
  grep -rn --include="*.xml" "<sofa.ark.version>2\..*</sofa.ark.version>" | awk -F: '{print $1}' | xargs -I {} sed -i '' "s/\<sofa.ark.version\>2\..*\<\/sofa.ark.version\>/\<sofa.ark.version\>$1\<\/sofa.ark.version\>/g" {}
else
  grep -rn --include="*.xml" "<sofa.ark.version>2\..*</sofa.ark.version>" | awk -F: '{print $1}' | xargs -I {} sed -i "s/\<sofa.ark.version\>2\..*\<\/sofa.ark.version\>/\<sofa.ark.version\>$1\<\/sofa.ark.version\>/g" {}
fi
