#!/bin/sh

rm -rf build
mkdir build
cd build
cmake -DOpenCVDIR=../../../build -DCMAKE_TOOLCHAIN_FILE=../../../android.toolchain.cmake ..
make -j6

