#!/bin/bash

cd ..
git pull up master
./bin/activator clean compile docker:stage
cp -rvf target/docker/stage deployment/app

