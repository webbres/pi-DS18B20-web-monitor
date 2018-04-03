# Pi DS18B20 Web Monitor

Currently provides a simple spring boot implementation of a logger for temperature data from 1 or more
DB18B20 sensors connected to a Pi or Pi Zero. 

This application will only succesfully run on a Rasperry Pi with 1 or more sensors connected.

## Database

The data is stored in an H2 database ~/.temperature/data

## Access

See [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) for documentation of the 
resources.