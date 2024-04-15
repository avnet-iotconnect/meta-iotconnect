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

### JSON Configs
Also in the `eg-private-repo-data` are sample JSON files, these are cross compatible with AWS and Azure. This aims to be the main configuration file for IoTC. This can be edited live on the device and the changes immediately reflected after restarting of the service.

The config json provides a quick and easy way to provide a user's executable with the requisite device credentials for any connection and a convenient method of mapping sensors to iotc device attributes. The demo source provided will match an `attribute.name` to a path on the user's host where the relevant sensor data resides. It also indicates to the demo what format to expect the data at the path to be in.

```json
{
    "sdk_ver": "2.1",
    
    // Unique ID of the device as used in the web console. This MUST match otherwise the handshake will fail.
    "duid": "",

    // Your unique CPID exactly as it shows in the Key Vault, this will authorize your devices to connect.
    "cpid": "",

    // Your environment exactly as it shows in the Key Vault.
    "env": "",

    // This is the root CA cert used for connecting your device, it is likely to be AWS Root CA 1 or a custom CA you have registered on the web console.
    "iotc_server_cert": "/etc/ssl/certs/...",

    // SDK Identities -> Language: Python **, Version: 1.0' from portal's Key Vault, needed for Python SDK
    "sdk_id": "",

    // The discovery URL for starting the IoTC login process. It WILL be different depending on if you are using AWS or Azure.
    "discovery_url": "",

    // Use IOTC_CT_AZURE or IOTC_CT_AWS to select connection type, only used for the C SDK
    "connection_type": "IOTC_CT_AZURE",
    
    // This is the auth method used to connect the device.
    "auth": {
        "auth_type": "IOTC_AT_X509",
            "params": {
                // This is the path on the device AFTER it's built and running on the device. Ensure that these files exist after building and are valid certs.
                // If you are using generated keys goto: https://awspoc.iotconnect.io/device/1/{THING_NAME_HERE} > Connection Info > Click on the icon with a certificate and download arrow.
                // Check the types of the certs you have just download and ensure they are in the correct format, as well as if their locations are as specified below.
                "client_key": "/usr/iotc-c/local/certs/device.key",
                "client_cert": "/usr/iotc-c/local/certs/DeviceCertificate.pem"
        }
    },
    // This contains specific command and telemetry to be send to IoTC.
    "device": {
        "commands_list_path": "/usr/iotc-c/app/scripts",
        "attributes": [
            {
                "name": "example",
                "private_data": "/tmp/example",
                "private_data_type": "ascii"
            }
        ]
    }
}
```

The sample JSON contains key value pairs where the value contains directions to what your individual value will be. E.g:
```json
{
    "sdk_ver": "2.1",
    "duid": "Your Device's name in https://avnet.iotconnect.io/device/1",
...
}
```
Would become: 
```json
{
    "sdk_ver": "2.1",
    "duid": "myDemoDevice",
...
}
```

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
