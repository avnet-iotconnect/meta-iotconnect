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
    repo

USER usersetup
WORKDIR /home/pokyuser
RUN mkdir STM32MP157_IoTC_kirkstone 
WORKDIR STM32MP157_IoTC_kirkstone 
RUN git config --global user.email "avnet-ci@example.com" && git config --global user.name "Avnet Iotc CI" && git config --global color.ui false
RUN repo init -u https://github.com/STMicroelectronics/oe-manifest.git -b refs/tags/openstlinux-5.15-yocto-kirkstone-mp1-v23.07.26 && repo sync    
USER root
RUN groupadd pokyuser -g1000 && useradd pokyuser -u 1000 -g 1000 -m -s /bin/bash 
RUN chown -R 1000:1000 /home/pokyuser
RUN mkdir -m 1777 /__w
USER pokyuser
WORKDIR /home/pokyuser
