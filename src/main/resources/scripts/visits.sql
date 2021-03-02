CREATE TYPE Animal_type AS ENUM('OTHER','DOG','CAT','HAMSTER','RAT','GUINEA_PIG');
CREATE TYPE VisitStatus_type AS ENUM('PLANNED','FINISHED','NOT_APPEARED');
--auto generated???????
CREATE TABLE visits (
  id int primary key,
  startTime TIMESTAMP,
  duration INTERVAL,
  animal int,
  status int,
  price NUMERIC
);

insert into visits (id,startTime,duration,animal,status,price) values (1,'2016-06-22T19:10:25','2:30:50',1,2,123.321);

drop table visits;

--lączenie do psql przez ubuntu:
-- psql -h ec2-54-73-68-39.eu-west-1.compute.amazonaws.com -p 5432 -U ixsonxtwrwbavc -d dfhoaf8t88lc2m
-- 5fe0670eabde6132e56882f95b059a4f2ec2d2fd7f7f68e916892cf8f93444cb

--przetestowac czy dziala to co jest
--pozmieniac jakies glupoty - Either Java np --https://www.baeldung.com/vavr-either
--sprawdzic czy dziala
--pomyslec co mozna lepszego zmienic
--upeszyc i napisac
--sprawdzic czy dziala
--junit testy
--na podstawie testow zrobic jakies updaty