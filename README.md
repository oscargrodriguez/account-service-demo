# account-service-demo
POC app to verify CQRS event sourcing approach for foobar editions

## Event analysis
https://miro.com/app/board/o9J_kimHzKs=/

## Kafka up and running

```bash
zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties & kafka-server-start /usr/local/etc/kafka/server.properties
```

## Install 🔧

```bash
$ mvn spring-boot:run
```

## Test ⚙️

http://localhost:8081/swagger-ui.html