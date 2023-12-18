FROM postgres:16.1

COPY ./init.sql /docker-entrypoint-initdb.d/init.sql

CMD ["docker-entrypoint.sh", "postgres"]