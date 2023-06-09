#!/bin/sh

mkdir -p "results"
mkdir -p "diagnostics"

for t in 10 30 90 270
do
	for fn in files/*.xml
	do
		instance=$(basename "$fn" .xml)
		java -jar code/nurses.jar "$instance" "$t" >> "results/${instance}-${t}.txt" 2> "diagnostics/${instance}-${t}.tsv"
	done
done
