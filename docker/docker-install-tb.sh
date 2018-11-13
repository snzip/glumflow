#!/bin/bash
./check-dirs.sh

while [[ $# -gt 0 ]]
do
key="$1"

case $key in
    --loadDemo)
    LOAD_DEMO=true
    shift # past argument
    ;;
    *)
            # unknown option
    ;;
esac
shift # past argument or value
done

if [ "$LOAD_DEMO" == "true" ]; then
    loadDemo=true
else
    loadDemo=false
fi

set -e

source compose-utils.sh

ADDITIONAL_COMPOSE_ARGS=$(additionalComposeArgs) || exit $?

ADDITIONAL_STARTUP_SERVICES=$(additionalStartupServices) || exit $?

if [ ! -z "${ADDITIONAL_STARTUP_SERVICES// }" ]; then
    docker-compose -f docker-compose.yml $ADDITIONAL_COMPOSE_ARGS up -d redis $ADDITIONAL_STARTUP_SERVICES
fi

docker-compose -f docker-compose.yml $ADDITIONAL_COMPOSE_ARGS run --no-deps --rm -e INSTALL_TB=true -e LOAD_DEMO=${loadDemo} tb1


