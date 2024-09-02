# Spring Security WebFlux with R2DBC

## Roadmap

- [ ] RBAC (WIP)
- [ ] Pagination
- [ ] CI/CD
- [x] Containerize
- [ ] Unit Test
- [ ] Global REST Response
- [ ] Persistent Entity
- [ ] Customized Exception
- [ ] Exception Handler (WIP)
- [ ] Logger
- [ ] Monitor & Trace

## docker

```shell
# for mysql data in container
mkdir -p /var/tmp/mysql_data/
# build jar
mvn clean package -Dmaven.test.skip=true
# set up database
docker-compose up -d --build
```