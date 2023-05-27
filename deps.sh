#!/bin/sh

cd dep || exit 1
git clone https://github.com/DennisDuysak/nrp
cd ..
cp -r -t code dep/nrp/src/Helper dep/nrp/src/Attributes
