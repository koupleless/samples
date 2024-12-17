#!/bin/sh

DEST_VERSION=$1

# 如果 DEST_VERSION 为空，则报错
if [ -z "$DEST_VERSION" ]; then
  echo "Error: SOURCE_VERSION and DEST_VERSION must be provided."
  exit 1
fi

# git grep -rn '2.2.12' | cat | awk -F: '{print $1}' | xargs -I {} sed -i '' 's/2.2.12/2.2.13/g' {}
git grep -rn "<sofa.ark.version>3\..*</sofa.ark.version>" | cat | awk -F: '{print $1}' | xargs -I {} sed -i '' "s/\<sofa.ark.version\>3\..*\<\/sofa.ark.version\>/\<sofa.ark.version\>$1\<\/sofa.ark.version\>/g" {}