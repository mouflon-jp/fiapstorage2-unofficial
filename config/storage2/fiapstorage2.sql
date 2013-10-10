/*
 * FIAP Data Storage PostgreSQL Implementation Data Schema                
 * Hideya Ochiai 
 * create: 2009-11-15
 * update: 2013-03-08
 */

CREATE TABLE pointSetTree (
id varchar PRIMARY KEY,
parent varchar,
ispoint boolean NOT NULL
);

CREATE TABLE pointValue (
id varchar REFERENCES pointSetTree,
time timestamptz NOT NULL,
attrString varchar,
value varchar,
PRIMARY KEY(id, time)
);

CREATE INDEX pointvalue_time_index ON pointValue(time);
