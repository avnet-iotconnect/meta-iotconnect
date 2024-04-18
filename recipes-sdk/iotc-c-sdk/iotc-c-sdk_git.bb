LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "gitsm://github.com/avnet-iotconnect/iotc-generic-c-sdk.git;protocol=https;branch=main \
           file://0001-curl-fix.patch;patchdir=.. \
           "



# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "701906f8f70a25273830e7c19cb631c09da1a2f1"

S = "${WORKDIR}/git/iotc-generic-c-sdk"
C = "${WORKDIR}/git"

DEPENDS += " curl"
DEPENDS += " pkgconfig"
DEPENDS += " openssl"
DEPENDS += " util-linux"

inherit cmake
EXTRA_OECMAKE += "-DCMAKE_SKIP_RPATH=TRUE"

cmake_do_generate_toolchain_file_append() {
	cat >> ${WORKDIR}/toolchain.cmake <<EOF
$cmake_crosscompiling

set( PC_CURL_LIBRARY_DIRS "${STAGING_LIBDIR}")

EOF
}

PACKAGES = "${PN} ${PN}-dev ${PN}-dbg ${PN}-staticdev"

FILES_${PN} += "/lib/* \
  /iotc-generic-c-sdk/* \
"

RDEPENDS_${PN} = " \
	bash \
	perl \
	make \
	ruby \
"

RDEPENDS_${PN}-staticdev = ""
RDEPENDS_${PN}-dev = ""
RDEPENDS_${PN}-dbg = ""

do_populate_sysroot() {
    mkdir -p ${SYSROOT_DESTDIR}/lib
    cp -r --no-preserve=ownership ${C}/lib/* ${SYSROOT_DESTDIR}/lib/
    rm -r ${SYSROOT_DESTDIR}/lib/paho.mqtt.c/test/python
    cp -r --no-preserve=ownership ${C}/iotc-generic-c-sdk ${SYSROOT_DESTDIR}/
}

# Create /usr/bin in rootfs and copy program to it
do_install() {
    mkdir -p ${D}/lib
    cp -r --no-preserve=ownership ${C}/lib/* ${D}/lib/
    rm -r ${D}/lib/paho.mqtt.c/test/python
    cp -r --no-preserve=ownership ${C}/iotc-generic-c-sdk ${D}/
}

