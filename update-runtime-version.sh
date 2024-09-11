#!/bin/sh

SOURCE_VERSION=$1
DEST_VERSION=$2

# 如何 SOURCE_VERSION 或 DEST_VERSION 为空，则报错
if [ -z "$SOURCE_VERSION" ] || [ -z "$DEST_VERSION" ]; then
  echo "Error: SOURCE_VERSION and DEST_VERSION must be provided."
  exit 1
fi

# git grep -rn '2.2.12' | cat | awk -F: '{print $1}' | xargs -I {} sed -i '' 's/2.2.12/2.2.13/g' {}
git grep -rn "<koupleless.runtime.version>$1</koupleless.runtime.version>" | cat | awk -F: '{print $1}' | xargs -I {} sed -i '' "s/\<koupleless.runtime.version\>$1\<\/koupleless.runtime.version\>/\<koupleless.runtime.version\>$2\<\/koupleless.runtime.version\>/g" {}
