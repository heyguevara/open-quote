-- Create example actuarial databases for use by OpenActuary users
create database if not exists AIL_Base_DataSource_Master_Motor character set utf8;

grant insert,select,update,delete on AIL_Base_DataSource_Master_Motor.* to 'adam'@'localhost' identified by 'Dhj0TB/sA+xAg0IstYumoJrE+yo=';
grant select on AIL_Base_DataSource_Master_Motor.* to 'charlie'@'localhost' identified by '2M0QuSDcvbUWPKAYXkAjV7wnwmU=';
grant select on AIL_Base_DataSource_Master_Motor.* to 'lily'@'localhost' identified by '/dvgGFfi06tQW/wdrDs4osi4zWU=';
grant select on AIL_Base_DataSource_Master_Motor.* to 'oliver'@'localhost' identified by 'xTkVO6H5R71Lb5ECY7lnxKCmI1c=';
grant select on AIL_Base_DataSource_Master_Motor.* to 'ethan'@'localhost' identified by 'CifhLQYq1xZz1X+cJ5myB68xaIU=';
grant select on AIL_Base_DataSource_Master_Motor.* to 'sophie'@'localhost' identified by 'X1BEO/529yeajg8vCpiXXNv/OOk=';
grant select on AIL_Base_DataSource_Master_Motor.* to 'thomas'@'localhost' identified by 'X1CoTB+jvP8UZAUBfzauwaEKnjg=';
use AIL_Base_DataSource_Master_Motor;
source ./content/OpenActuary-Table-Setup.sql

create database if not exists AIL_Base_DataSource_Test character set utf8;
grant insert,select,update,delete on AIL_Base_DataSource_Test.* to 'adam'@'localhost' identified by 'Dhj0TB/sA+xAg0IstYumoJrE+yo=';
grant select on AIL_Base_DataSource_Test.* to 'charlie'@'localhost' identified by '2M0QuSDcvbUWPKAYXkAjV7wnwmU=';
grant select on AIL_Base_DataSource_Test.* to 'lily'@'localhost' identified by '/dvgGFfi06tQW/wdrDs4osi4zWU=';
grant select on AIL_Base_DataSource_Test.* to 'oliver'@'localhost' identified by 'xTkVO6H5R71Lb5ECY7lnxKCmI1c=';
grant select on AIL_Base_DataSource_Test.* to 'ethan'@'localhost' identified by 'CifhLQYq1xZz1X+cJ5myB68xaIU=';
grant select on AIL_Base_DataSource_Test.* to 'sophie'@'localhost' identified by 'X1BEO/529yeajg8vCpiXXNv/OOk=';
grant select on AIL_Base_DataSource_Test.* to 'thomas'@'localhost' identified by 'X1CoTB+jvP8UZAUBfzauwaEKnjg=';

create database if not exists MetropolitanInsurance_Motor_Staging character set utf8;
grant insert,select,update,delete on MetropolitanInsurance_Motor_Staging.* to 'charlie'@'localhost' identified by '2M0QuSDcvbUWPKAYXkAjV7wnwmU=';
grant select on MetropolitanInsurance_Motor_Staging.* to 'lily'@'localhost' identified by '/dvgGFfi06tQW/wdrDs4osi4zWU=';
grant select on MetropolitanInsurance_Motor_Staging.* to 'oliver'@'localhost' identified by 'xTkVO6H5R71Lb5ECY7lnxKCmI1c=';
use MetropolitanInsurance_Motor_Staging;
source ./content/OpenActuary-Table-Setup.sql

create database if not exists StarInsurance_Motor_Staging  character set utf8;
grant insert,select,update,delete on StarInsurance_Motor_Staging.* to 'ethan'@'localhost' identified by 'CifhLQYq1xZz1X+cJ5myB68xaIU=';
grant select on StarInsurance_Motor_Staging.* to 'sophie'@'localhost' identified by 'X1BEO/529yeajg8vCpiXXNv/OOk=';
grant select on StarInsurance_Motor_Staging.* to 'thomas'@'localhost' identified by 'X1CoTB+jvP8UZAUBfzauwaEKnjg=';
use StarInsurance_Motor_Staging;
source ./content/OpenActuary-Table-Setup.sql

