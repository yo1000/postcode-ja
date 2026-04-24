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

2. Check the log and wait until the message `Ending table updates.` is displayed.

```
... Node=55c3aa5c-8ea4-456f-a9d5-928d1c85c8d1 Time=1777056793106 | Starting table updates.
... Node=55c3aa5c-8ea4-456f-a9d5-928d1c85c8d1 Time=1777056793106 | Sleep 0-millis.
... Node=55c3aa5c-8ea4-456f-a9d5-928d1c85c8d1 Time=1777056793106 | Ending table updates.
```

3. Access to API

- http://localhost:8080/posts/1000001
- http://localhost:8080/posts/100--
- http://localhost:8080/posts?prefectureName=%E6%9D%B1%E4%BA%AC%E9%83%BD&municipalityName=%E5%8D%83%E4%BB%A3%E7%94%B0%E5%8C%BA&townAreaName=%E5%8D%83%E4%BB%A3%E7%94%B0


How to retrieve resource files from the internet
----------------------------------------------------------------------------------------------------

```bash
export APP_CSV_RESOURCE="https://www.post.japanpost.jp/zipcode/dl/utf/zip/utf_ken_all.zip"
```


Actuator endpoints
----------------------------------------------------------------------------------------------------

- http://localhost:8090/actuator
- http://localhost:8090/actuator/health
- http://localhost:8090/actuator/metrics/jvm.memory.used
