#!/bin/bash
mkdir old
for f in $(ls *.tex); do awk '{gsub("\\","\n"); print $0}' $f > "$f.new" && mv $f old/ && [[ $f =~ ^(.*)\.tex$ ]] && mv "$f.new" "${BASH_REMATCH[1]}.tex"; done
