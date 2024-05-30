FROM crops/poky:ubuntu-22.04

USER root
RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y \
    rsync \
    repo

RUN groupadd pokyuser -g1000 && useradd pokyuser -u 1000 -g 1000 -m -s /bin/bash && chown -R 1000:1000 /home/pokyuser && mkdir -m 1777 /__w
USER pokyuser
WORKDIR /home/pokyuser
RUN mkdir MaaXBoard_IoTC_kirkstone 
WORKDIR MaaXBoard_IoTC_kirkstone 
RUN git config --global user.email "avnet-ci@example.com" && git config --global user.name "Avnet Iotc CI" && git config --global color.ui false
RUN repo init -u https://github.com/nxp-imx/imx-manifest  -b imx-linux-kirkstone -m imx-5.15.71-2.2.2.xml && repo sync && git clone https://github.com/Avnet/meta-maaxboard.git -b kirkstone sources/meta-maaxboard    
