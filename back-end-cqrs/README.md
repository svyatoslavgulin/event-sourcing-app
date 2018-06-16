1. запускаем zookeeper-server-start.sh ../config/zookeeper.properties
2. запускаем kafka-server-start.sh ../config/server.properties
3. запускаем mysql
4. создаем БД с именем event_sourcing
5. создаем пользователя с именем event_sourcing и паролем event_sourcing
6. запускаем CqrsApplication.main