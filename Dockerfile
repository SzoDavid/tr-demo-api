FROM ubuntu:latest
LABEL authors="szobonya"

ENTRYPOINT ["top", "-b"]