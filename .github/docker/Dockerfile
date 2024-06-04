FROM crops/poky:ubuntu-22.04

USER root
RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y \
    bsdmainutils \
    libgmp-dev \
    libmpc-dev \
    libssl-dev \
    python3-pip \
    zip \
    rsync \
    repo

RUN groupadd pokyuser -g1000 && useradd pokyuser -u 1000 -g 1000 -m -s /bin/bash && chown -R 1000:1000 /home/pokyuser && mkdir -m 1777 /__w
USER pokyuser
WORKDIR /home/pokyuser
RUN mkdir MaaXBoard_IoTC
RUN mkdir STM32MP1_IoTC
