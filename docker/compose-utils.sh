#!/bin/bash
function additionalComposeArgs() {
    source .env
    ADDITIONAL_COMPOSE_ARGS=""
    case $DATABASE in
        postgres)
        ADDITIONAL_COMPOSE_ARGS="-f docker-compose.postgres.yml"
        ;;
        cassandra)
        ADDITIONAL_COMPOSE_ARGS="-f docker-compose.cassandra.yml"
        ;;
        *)
        echo "Unknown DATABASE value specified: '${DATABASE}'. Should be either postgres or cassandra." >&2
        exit 1
    esac
    echo $ADDITIONAL_COMPOSE_ARGS
}

function additionalStartupServices() {
    source .env
    ADDITIONAL_STARTUP_SERVICES=""
    case $DATABASE in
        postgres)
        ADDITIONAL_STARTUP_SERVICES=postgres
        ;;
        cassandra)
        ADDITIONAL_STARTUP_SERVICES=cassandra
        ;;
        *)
        echo "Unknown DATABASE value specified: '${DATABASE}'. Should be either postgres or cassandra." >&2
        exit 1
    esac
    echo $ADDITIONAL_STARTUP_SERVICES
}
