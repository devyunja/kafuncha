#!/usr/bin/env bash

CONTAINER_NAME="kafuncha-postgres"
USERNAME="postgres"
DATABASE_NAME="postgres"

docker exec -it $CONTAINER_NAME psql -U $USERNAME $DATABASE_NAME
