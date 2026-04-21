Postcode
====================================================================================================

Load Japanese postcodes from a file and transform it into an API.


How to run
----------------------------------------------------------------------------------------------------

1. Run containers

```bash
./mvnw clean package && \
docker compose down && docker compose up --build
 ```


Actuator endpoints
----------------------------------------------------------------------------------------------------

- http://localhost:8090/actuator
- http://localhost:8090/actuator/health
- http://localhost:8090/actuator/metrics/jvm.memory.used
