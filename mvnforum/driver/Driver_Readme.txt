The content of this directory:

NOTE: If this folder does not contain the driver for your database, please 
      look at the short guide in the header of sql/mvnForum_<yourdatabase>.sql
      Ex: The driver for Oracle is in sql/mvnForum_oracle9.sql


mysql-connector-java-5.1.12-bin.jar
    Description : driver for Mysql (Connector/J)
    Driver      : com.mysql.jdbc.Driver
    Url (3.x 4.0.x) : jdbc:mysql://<host>/<database>?useUnicode=true&characterEncoding=utf-8
    Url (4.1.x)     : jdbc:mysql://localhost/mvnforum?useServerPrepStmts=false
    Version     : 5.1.12



mysql-connector-java-5.0.8-bin.jar
    Description : driver for Mysql (Connector/J)
    Driver      : com.mysql.jdbc.Driver
    Url (3.x 4.0.x) : jdbc:mysql://<host>/<database>?useUnicode=true&characterEncoding=utf-8
    Url (4.1.x)     : jdbc:mysql://localhost/mvnforum?useServerPrepStmts=false
    Version     : 5.0.8



mysql-connector-java-3.0.17-ga-bin.jar
    Description : driver for Mysql (Connector/J)
    Driver      : com.mysql.jdbc.Driver
    Url (3.x 4.0.x) : jdbc:mysql://<host>/<database>?useUnicode=true&characterEncoding=utf-8
    Url (4.1.x)     : jdbc:mysql://localhost/mvnforum?useServerPrepStmts=false
    Version     : 3.0.17-ga



mysql-connector-java-3.1.14-bin.jar (for MySql 4.1.x and later)
    Description : driver for Mysql (Connector/J)
    Driver      : com.mysql.jdbc.Driver
    Url (4.1.x) : jdbc:mysql://localhost/mvnforum?useServerPrepStmts=false
    Version     : 3.1.14
    Read more   : http://dev.mysql.com/doc/refman/5.0/en/cj-upgrading.html



hsqldb.jar 
    Description : driver and database engine for hsqldb
    Driver      : org.hsqldb.jdbcDriver
    Url         : jdbc:hsqldb:file:<database>
    Version     : 1.8.1.2
    Note        : latest driver is here http://hsqldb.sourceforge.net



postgresql-8.4-701.jdbc3.jar 
    Description : driver for PostgreSql 
    Driver      : org.postgresql.Driver
    Url         : jdbc:postgresql://host:port/database
    Version     : 8.4-701.jdbc3
    Note        : latest driver is here http://jdbc.postgresql.org/download.html
                  JDBC driver for postgreSQL usually bundled with the Linux distro



jtds-1.2.5.jar 
    Description : driver for MS Sql Server or Sybase
    Driver      : net.sourceforge.jtds.jdbc.Driver
    Url         : jdbc:jtds:sqlserver://<hostname>[:<port>][/<dbname>][;<property>=<value>[;...]]
                  jdbc:jtds:sybase://<hostname>[:<port>][/<dbname>][;<property>=<value>[;...]]
    Version     : 1.2.5
    Note        : latest driver is here http://jtds.sourceforge.net/
    Note        : <port> is the port the server is listening to (with a default of 1433 for SQL Server 
                  and 4000 for Sybase) and <database> is the database name -- or catalog 
                  in the JDBC terminology -- (default is 'master').

