-- Make sure the database and users are there before we try to delete them!
create database if not exists ##dbname## character set utf8;
grant all on ##dbname##.* to '##dbusername##'@'localhost' identified by '##dbpassword##' with grant option;
grant all on ##dbname##.* to '##dbusername##'@'localhost.localdomain' identified by '##dbpassword##' with grant option;
  
-- Now do the actual tidy up 
drop database if exists ##dbname##;
revoke all on ##dbname##.* from '##dbusername##'@'localhost';
revoke all on ##dbname##.* from '##dbusername##'@'localhost.localdomain';
