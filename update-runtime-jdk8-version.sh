#!/bin/bash

DEST_VERSION=$1

# 如果 DEST_VERSION 为空，则报错
if [ -z "$DEST_VERSION" ]; then
  echo "Error: DEST_VERSION must be provided."
  exit 1
fi

if [[ "$(uname)" == "Darwin" ]]; then
  grep -rn --include="*.xml" "<koupleless.runtime.version>1\..*</koupleless.runtime.version>" | awk -F: '{print $1}' | xargs -I {} sed -i '' "s/\<koupleless.runtime.version\>1\..*\<\/koupleless.runtime.version\>/\<koupleless.runtime.version\>$1\<\/koupleless.runtime.version\>/g" {}
else
  grep -rn --include="*.xml" "<koupleless.runtime.version>1\..*</koupleless.runtime.version>" | awk -F: '{print $1}' | xargs -I {} sed -i "s/\<koupleless.runtime.version\>1\..*\<\/koupleless.runtime.version\>/\<koupleless.runtime.version\>$1\<\/koupleless.runtime.version\>/g" {}
fi
