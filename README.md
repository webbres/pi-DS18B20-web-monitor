# Pi DS18B20 Web Monitor

Currently provides a simple spring boot implementation of a logger for temperature data from 1 or more
DB18B20 sensors connected to a Pi or Pi Zero. 

This application will only succesfully run on a Rasperry Pi with 1 or more sensors connected.

# Database

The data is stored in an H2 database ~/.temperature/data

# Access

See [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) for documentation of the 
resources.

## Resources

These resources are all GET requests and the URL is relative to the base. If running as a spring boot standalone application this will
be http://localhost:8080 and will vary if deployed in tomcat.

### /sensors/{id}/

Get the current temperature recorded by the sensor with the specified ID as a url path parameter.

### /sensors/ids/

Get all available IDs

#### Example response
```
	19.625
```

#### Example response

```json
	[
	  "28-0517602a31ff",
	  "28-051760a9aaff"
	]
```

### /sensors/all/

Get the temperature readings for all connected sensors.

#### Example response:

```json
	{
	  "28-0517602a31ff": 19.625,
	  "28-051760a9aaff": 18.562
	}
```



# Building and Deploying

To generate an executable jar run (this should be machine agnostic, build your your PC and run on the pi).

```shell
$ gradle build
```

Then to run the application:

```shell
$ java -jar pi-temp-sensor-rest-{version}.jar
```

