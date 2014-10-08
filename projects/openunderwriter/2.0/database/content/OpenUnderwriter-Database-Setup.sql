--
-- Create the OpenQuote database, users and rights.
--
CREATE DATABASE IF NOT EXISTS ##dbname## character set utf8;
GRANT ALL ON ##dbname##.* TO '##dbusername##'@'localhost' IDENTIFIED BY '##dbpassword##' WITH GRANT OPTION;
GRANT ALL ON ##dbname##.* TO '##dbusername##'@'localhost.localdomain' IDENTIFIED BY '##dbpassword##' WITH GRANT OPTION;

SET FOREIGN_KEY_CHECKS=0;

USE ##dbname##;