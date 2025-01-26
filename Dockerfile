FROM ubuntu

RUN /bin/bash -c 'echo Hello this cmd is utilizing docker'

ENV myEnv="myEnv 1" \
    myEnv2="myEnv 2"