#!/bin/sh

mkdir -p "results"

for t in 10 30
do
	for i in $(seq 1 8)
	do
		for fn in files/*.xml
		do
			instance=$(basename "$fn" .xml)
			java -jar code/nurses.jar "$instance" "$t" >> "results/${instance}-${t}.txt"
		done
	done
done

for t in 90 270 810
do
	for i in $(seq 1 8)
	do
		for fn in files/long*.xml
		do
			instance=$(basename "$fn" .xml)
			java -jar code/nurses.jar "$instance" "$t" >> "results/${instance}-${t}.txt"
		done
	done
done
