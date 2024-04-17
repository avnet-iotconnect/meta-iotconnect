LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://git@github.com/avnet-iotconnect/iotc-python-sdk.git;protocol=ssh;branch=master-std-21"

# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "f37dcdb3f8af6a29b2e251e3eeec1f95bf516988"

S = "${WORKDIR}/git/iotconnect-sdk-1.0"
DISTUTILS_SETUP_PATH = "${WORKDIR}/git/iotconnect-sdk-1.0"

inherit setuptools3


RDEPENDS:${PN} += " python3"
RDEPENDS:${PN} += " python3-pip"

RDEPENDS:${PN} += " python3-ntplib"
RDEPENDS:${PN} += " python3-paho-mqtt"
RDEPENDS:${PN} += " python3-wheel"

distutils_do_configure () {
	# Specify any needed configure commands here
	:
}

distutils_do_compile () {
	# Specify compilation commands here
	:
}

distutils_do_install () {
	# Specify install commands here
	:
}