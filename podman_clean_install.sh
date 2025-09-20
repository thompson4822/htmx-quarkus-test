#!/bin/bash
# This script runs a clean Maven install, configuring DOCKER_HOST to point to the Podman socket.
# This allows Testcontainers to use Podman for running dev services during the build.

export DOCKER_HOST=unix:///run/user/1000/podman/podman.sock

./mvnw clean install
