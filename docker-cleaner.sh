#!/usr/bin/env bash

docker container prune --force
docker image prune --all --force
docker volume prune --force
