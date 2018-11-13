#!/bin/bash
./check-dirs.sh

for i in "$@"
do
case $i in
    --fromVersion=*)
    FROM_VERSION="${i#*=}"
    shift
    ;;
    *)
            # unknown option
    ;;
esac
done

if [[ -z "${FROM_VERSION// }" ]]; then
    echo "--fromVersion parameter is invalid or unspecified!"
    echo "Usage: docker-upgrade-tb.sh --fromVersion={VERSION}"
    exit 1
else
    fromVersion="${FROM_VERSION// }"
fi

set -e

source compose-utils.sh

ADDITIONAL_COMPOSE_ARGS=$(additionalComposeArgs) || exit $?

ADDITIONAL_STARTUP_SERVICES=$(additionalStartupServices) || exit $?

docker-compose -f docker-compose.yml $ADDITIONAL_COMPOSE_ARGS pull tb1

if [ ! -z "${ADDITIONAL_STARTUP_SERVICES// }" ]; then
    docker-compose -f docker-compose.yml $ADDITIONAL_COMPOSE_ARGS up -d redis $ADDITIONAL_STARTUP_SERVICES
fi

docker-compose -f docker-compose.yml $ADDITIONAL_COMPOSE_ARGS run --no-deps --rm -e UPGRADE_TB=true -e FROM_VERSION=${fromVersion} tb1
