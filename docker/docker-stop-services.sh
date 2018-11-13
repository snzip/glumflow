#!/bin/bash
set -e

source compose-utils.sh

ADDITIONAL_COMPOSE_ARGS=$(additionalComposeArgs) || exit $?

docker-compose -f docker-compose.yml $ADDITIONAL_COMPOSE_ARGS stop
