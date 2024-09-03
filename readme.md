# Spring Security WebFlux with R2DBC

## Roadmap

- [x] RBAC
- [x] Containerize
- [x] Global REST Response
- [x] Pagination
- [x] Customized Exception and Handler
- [x] Logger
- [ ] CI/CD
- [x] Monitor & Trace
- [ ] Unit Test
- [ ] Profile Configuration

## docker

```shell
# for mysql data in container
mkdir -p /var/tmp/mysql_data/
# build jar
mvn clean package -Dmaven.test.skip=true
# set up database
docker-compose up -d --build
```

## netty access log

```text
-Dreactor.netty.http.server.accessLogEnabled=true
```
