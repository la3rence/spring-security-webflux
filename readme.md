# Spring Security WebFlux with R2DBC

## docker

```shell
# for mysql data in container
mkdir -p /var/tmp/mysql_data/
# build jar
mvn clean package -Dmaven.test.skip=true
# set up database
docker-compose up -d --build
```