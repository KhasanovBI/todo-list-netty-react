#!/usr/bin/env bash
mvn clean compile assembly:single
cd public_html && npm i
