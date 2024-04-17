# IoT-Connect Yocto Integration
This repository aims to cover all Yocto builds for the C and Python SDK.

### `meta-myExampleIotconnectLayer`
This layer provides an example of how a user might write a recipe suitable for their application. It contains a simple application that demonstrates telemetry and commands. Once installed on the image it can be started by logging in & executing:

`/usr/iotc-c/app/iotc/iotc-c-telemetry-demo /path/to/config.json`
or
`/usr/bin/local/iotc/iotc-demo.py /path/to/config.json`

where `config.json` is a file that contains device authentication information and paths to where telemetry-demo will read data from on the host device. It's expected that in the 1st instance a user would run this demo on their hardware after editing a sample `config.json` to reflect a device they've defined on iotconnect.io and sensor data particular to their hardware.

By adding the recipe to your image (e.g. `IMAGE_INSTALL += "telemetry-demo"` in `conf/local.conf`)

```
iotc-yocto-c-sdk$ tree meta-myExampleIotconnectLayer/
meta-myExampleIotconnectLayer/
├── conf
│   └── layer.conf
└── recipes-apps
│   └── telemetry-demo <-------------------- Recipe directory
│       ├── files
│       │   ├── cmke-src <------------------ A small CMake project
│       │   │   ├── CMakeLists.txt
│       │   │   ├── json_parser.c
│       │   │   ├── json_parser.h
|       │   │   └── main.c
|       │   └── eg-private-repo-data <----- Location for certificates/keys & other config data for development purposes.
|       │       ├── config.json
|       │       ├── config-symmtrcKy.json
|       │       └── config-x509.json
|       └── telemetry-demo_0.1.bb <-------- Recipe
├── conf
│   └── layer.conf
├── recipes-apps
│   └── telemetry-demo <-------------------- Recipe directory
│      ├── files
│       │   ├── eg-private-repo-data <----- Location for provisioning configuration files.
│       │   │   ├── certs <----- Location for certificates/keys.
│       │   │   ├── configSymmetricKey.json
│       │   │   └── configX509.json
│       │   ├── scripts <----- Location for executable scripts that run as commands.
│       │   │   └── control_led.sh
│       │   └── src <------------------ Demo CMake project directory
│       │       ├── CMakeLists.txt
│       │       ├── iotcl_config.h
│       │       └── main.c
│       └── iotc-c-telemetry-demo_0.1.bb <----- Main recipe
└── recipes-systemd
    └── iotc-c-telemetry-demo-service
        ├── files
        │   └── iotc-c-telemetry-demo.service
        └── iotc-c-telemetry-demo-service_0.1.bb
```

As developing a iotc application involves the use of private/secure data like keys/certificates and the user is expected to develop same application using SCM like git, it's worth taking a moment to be aware of risks of accidentlally uploading private data to places it shouldnt belong. The directory `eg-priviate-repo-data` seeks to provide a safe space to place sensitive data like device keys etc for development purposes only. When the user installs the _development_ version of the recipe (`telemetry-demo-dev`) any files within `eg-private-repo-data` will be installed in the rootfs of the image. The `.gitignore` settings for this repo are also configured to prevent accidental upload of *.pem or *.crt files.

This approach allows the user to develop their solution conveniently, then when it's time to provide production builds, the result would be a clean installation awaiting first time configuration post image flash.

## How to include layers
To include the layers within a yocto enviroment:

1. check them out to the `sources` directory in your yocto enviroment.

1. add them to `conf/bblayers` file in your build directory

1. add recipes as a part of your image (for example in `<meta-my-layer>/recipes-core/images/<image-name.bb>` file) or to your local build configuration (in `local.conf` for example) - `IMAGE_INSTALL += " telemetry-demo-dev"`

1. using the config.json files in `eg-private-repo-data` as a template, create your own config.json with details of the device you have setup on iotconnect.io.

1. editing the same json as in the last step, edit the `attributes` section of the JSON so the `name` of the attritube maps to a path on your system where the relevant data can be found e.g. the path to the position data of an I2C accelerometer might be: `/sys/bus/i2c/devices/1-0053/position`.

1. build with a bitbake call e.g. `./bitbake <image-name>`

1. Flash the resultant image to the device.

2. Login into the device & run the command `/usr/iotc-c/app/iotc/telemetry-demo /usr/iotc-c/local/config.json`

***Note***: you might need adding lines below to your image
```
inherit core-image
inherit module
inherit extrausers
```

## Board specific examples can be found [here](board_specific_readmes/README.md)
