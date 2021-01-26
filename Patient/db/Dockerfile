FROM mysql

ENV MYSQL_ROOT_PASSWORD poiuyTREZA123
ENV MYSQL_DATABASE patient_db
ENV MYSQL_USER PatientAdmUser
ENV MSQL_PASSWORD 123

ADD shema.sql /docker-entrypoint-initdb.d
EXPOSE 3306
