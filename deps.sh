#!/bin/sh

cd dep
git clone https://github.com/DennisDuysak/nrp
cd ..
cp -r -t code dep/nrp/src/Helper dep/nrp/src/Attributes
