#!/bin/sh

mkdir -p "results"

for t in 10 30 90 270
do
	for fn in files/*.xml
	do
		instance=$(basename "$fn" .xml)
		java -jar code/nurses.jar "$instance" "$t" >> "results/${instance}-${t}.txt"
	done
done
