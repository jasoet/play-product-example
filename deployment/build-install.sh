#!/bin/bash

cd ..
git pull up master
./bin/activator clean compile docker:stage
rm -rvf deployment/app/opt deployment/app/Dockerfile
cp -rvf target/docker/stage/* deployment/app/

