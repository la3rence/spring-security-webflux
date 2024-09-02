# Spring Security WebFlux with R2DBC

## Roadmap

- [x] RBAC
- [x] Containerize
- [x] Global REST Response
- [ ] Pagination
- [ ] Persistent Entity
- [x] Customized Exception and Handler
- [ ] Logger
- [ ] CI/CD
- [ ] Monitor & Trace
- [ ] Unit Test

## docker

```shell
# for mysql data in container
mkdir -p /var/tmp/mysql_data/
# build jar
mvn clean package -Dmaven.test.skip=true
# set up database
docker-compose up -d --build
```