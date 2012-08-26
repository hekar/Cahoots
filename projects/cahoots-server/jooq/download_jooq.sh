#!/bin/sh

DIR=`dirname $(readlink -f $0)`

wget -P $DIR -c http://repo1.maven.org/maven2/org/jooq/jooq/2.4.0/jooq-2.4.0.jar
wget -P $DIR -c http://repo1.maven.org/maven2/org/jooq/jooq-meta/2.4.0/jooq-meta-2.4.0.jar
wget -P $DIR -c http://repo1.maven.org/maven2/org/jooq/jooq-codegen/2.4.0/jooq-codegen-2.4.0.jar
wget -P $DIR -c http://repo1.maven.org/maven2/postgresql/postgresql/9.1-901.jdbc4/postgresql-9.1-901.jdbc4.jar

