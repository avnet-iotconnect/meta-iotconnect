# IoT-Connect Yocto Integration meta-layer
This repository is a Yocto meta layer for providing recipes for the IoT-Connect [C](https://github.com/avnet-iotconnect/iotc-generic-c-sdk/tree/main/) and [Python](https://github.com/avnet-iotconnect/iotc-python-sdk/tree/master-std-21) SDKs.

It provides a recipe for the C SDK which you can add to your own recipes through `RDEPENDS_${PN} += " iotc-c-sdk"` as well as the Python SDK through `RDEPENDS_${PN} = "python3-iotconnect-sdk`.