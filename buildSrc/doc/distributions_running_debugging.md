# Creating packages

To create all distributions (including docker) without running the tests, simply run the
following:

```
./gradlew assemble --parallel
```

To create a platform-specific build including the x-pack modules, use the
following depending on your operating system:

-----------------------------
./gradlew :distribution:archives:linux-tar:assemble --parallel
./gradlew :distribution:archives:darwin-tar:assemble --parallel
./gradlew :distribution:archives:windows-zip:assemble --parallel
-----------------------------

# Running Elasticsearch from a checkout

In order to run Elasticsearch from source without building a package, you can
run it using Gradle:

```
./gradlew --parallel run
```

This will run the default distribution with a basic license, and thus security enabled  with
username `elastic-admin` and password `elastic-password`.

This cna be checked with:
```
curl -u elastic-admin:elastic-password 127.0.0.1:9200
```

## Launching and debugging from an IDE

If you want to run Elasticsearch from your IDE, this method
supports a remote debugging option:

```
./gradlew --parallel run --debug-jvm
```
This will block and wait for debugging connections on port `8000`.


## Running with a different distribution

By default a node is started with the default distribution.
In order to start with a different distribution use the `-Drun.distribution` argument.

To for example start the open source distribution:

```
./gradlew run -Drun.distribution=oss
```

## License type

By default a node is started with the `basic` license type.
In order to start with a different license type use the `-Drun.license_type` argument.

In order to start a node with a trial license execute the following command:

-------------------------------------
./gradlew run -Drun.license_type=trial
-------------------------------------

This enables security and paid features.
It runs with the same credentials.

## Other useful arguments

In order to start a node with a different max heap space add: `-Dtests.heap.size=4G`
In order to disable assertions add: `-Dtests.asserts=false`
In order to set an Elasticsearch setting, provide a setting with the following prefix: `-Dtests.es.`
