Creating the Dagnabit database.
=================================
The Dagnabit database may be created in either PostgreSQL or Oracle.

PREREQUISTS
-------------
Use one of the sample jdbc.properties as a starting point.
The tablespace needs to be set in the gradle.properties as well

#Create database from scratch.
gradle deployNew

#Drop all elements in the schema
gradle dropAll

#Apply changes from changes directory
gradle deployChanges
