CREATE TABLE repository (name varchar(50) PRIMARY KEY NOT NULL,
                        owner varchar(50) NOT NULL,
                        issues int,
                        fork int,
                        ts timestamp default (now() at time zone 'utc-1')
                        );

INSERT INTO repository values ('TPSpringTestForkIssue', 'abkhemissi',0,0);

create table test(
    id int,
    ts timestamp default (now() at time zone 'utc-1')
);


insert into test values (1);

insert into test values(2,'2019-12-10 12:21:27');