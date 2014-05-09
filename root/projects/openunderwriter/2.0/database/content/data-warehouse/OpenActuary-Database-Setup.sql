-- Create example actuarial databases for use by OpenActuary users
create database if not exists ##dbname##_AIL_Base_DataSource_Master_Motor character set utf8;
grant insert,select,update,delete on ##dbname##_AIL_Base_DataSource_Master_Motor.* to 'adam'@'localhost' identified by 'Dhj0TB/sA+xAg0IstYumoJrE+yo=';
grant select on ##dbname##_AIL_Base_DataSource_Master_Motor.* to 'charlie'@'localhost' identified by '2M0QuSDcvbUWPKAYXkAjV7wnwmU=';
grant select on ##dbname##_AIL_Base_DataSource_Master_Motor.* to 'lily'@'localhost' identified by '/dvgGFfi06tQW/wdrDs4osi4zWU=';
grant select on ##dbname##_AIL_Base_DataSource_Master_Motor.* to 'oliver'@'localhost' identified by 'xTkVO6H5R71Lb5ECY7lnxKCmI1c=';
grant select on ##dbname##_AIL_Base_DataSource_Master_Motor.* to 'ethan'@'localhost' identified by 'CifhLQYq1xZz1X+cJ5myB68xaIU=';
grant select on ##dbname##_AIL_Base_DataSource_Master_Motor.* to 'sophie'@'localhost' identified by 'X1BEO/529yeajg8vCpiXXNv/OOk=';
grant select on ##dbname##_AIL_Base_DataSource_Master_Motor.* to 'thomas'@'localhost' identified by 'X1CoTB+jvP8UZAUBfzauwaEKnjg=';
grant select on ##dbname##_AIL_Base_DataSource_Master_Motor.* to 'report'@'localhost' identified by 'e98d2f001da5678b39482efbdf5770dc';
use ##dbname##_AIL_Base_DataSource_Master_Motor;
source ./content/data-warehouse/OpenActuary-Table-Setup.sql
source ./content/data-warehouse/OpenActuary-Standing-Data-Setup.sql
source ./content/data-warehouse/OpenActuary-Trigger-Setup.sql

create database if not exists ##dbname##_MetropolitanInsurance_Motor_Staging character set utf8;
grant insert,select,update,delete on ##dbname##_MetropolitanInsurance_Motor_Staging.* to 'charlie'@'localhost' identified by '2M0QuSDcvbUWPKAYXkAjV7wnwmU=';
grant select on ##dbname##_MetropolitanInsurance_Motor_Staging.* to 'lily'@'localhost' identified by '/dvgGFfi06tQW/wdrDs4osi4zWU=';
grant select on ##dbname##_MetropolitanInsurance_Motor_Staging.* to 'oliver'@'localhost' identified by 'xTkVO6H5R71Lb5ECY7lnxKCmI1c=';
grant select on ##dbname##_MetropolitanInsurance_Motor_Staging.* to 'report'@'localhost' identified by 'e98d2f001da5678b39482efbdf5770dc';
use ##dbname##_MetropolitanInsurance_Motor_Staging;
source ./content/data-warehouse/OpenActuary-Table-Setup.sql
source ./content/data-warehouse/OpenActuary-Standing-Data-Setup.sql
source ./content/data-warehouse/OpenActuary-Trigger-Setup.sql

create database if not exists ##dbname##_StarInsurance_Motor_Staging  character set utf8;
grant insert,select,update,delete on ##dbname##_StarInsurance_Motor_Staging.* to 'ethan'@'localhost' identified by 'CifhLQYq1xZz1X+cJ5myB68xaIU=';
grant select on ##dbname##_StarInsurance_Motor_Staging.* to 'sophie'@'localhost' identified by 'X1BEO/529yeajg8vCpiXXNv/OOk=';
grant select on ##dbname##_StarInsurance_Motor_Staging.* to 'thomas'@'localhost' identified by 'X1CoTB+jvP8UZAUBfzauwaEKnjg=';
grant select on ##dbname##_StarInsurance_Motor_Staging.* to 'report'@'localhost' identified by 'e98d2f001da5678b39482efbdf5770dc';
use ##dbname##_StarInsurance_Motor_Staging;
source ./content/data-warehouse/OpenActuary-Table-Setup.sql
source ./content/data-warehouse/OpenActuary-Standing-Data-Setup.sql
source ./content/data-warehouse/OpenActuary-Trigger-Setup.sql

create database if not exists ##dbname##_AIL_Base_DataSource_Test character set utf8;
grant insert,select,update,delete on ##dbname##_AIL_Base_DataSource_Test.* to 'adam'@'localhost' identified by 'Dhj0TB/sA+xAg0IstYumoJrE+yo=';
grant select on ##dbname##_AIL_Base_DataSource_Test.* to 'charlie'@'localhost' identified by '2M0QuSDcvbUWPKAYXkAjV7wnwmU=';
grant select on ##dbname##_AIL_Base_DataSource_Test.* to 'lily'@'localhost' identified by '/dvgGFfi06tQW/wdrDs4osi4zWU=';
grant select on ##dbname##_AIL_Base_DataSource_Test.* to 'oliver'@'localhost' identified by 'xTkVO6H5R71Lb5ECY7lnxKCmI1c=';
grant select on ##dbname##_AIL_Base_DataSource_Test.* to 'ethan'@'localhost' identified by 'CifhLQYq1xZz1X+cJ5myB68xaIU=';
grant select on ##dbname##_AIL_Base_DataSource_Test.* to 'sophie'@'localhost' identified by 'X1BEO/529yeajg8vCpiXXNv/OOk=';
grant select on ##dbname##_AIL_Base_DataSource_Test.* to 'thomas'@'localhost' identified by 'X1CoTB+jvP8UZAUBfzauwaEKnjg=';
grant select on ##dbname##_AIL_Base_DataSource_Test.* to 'report'@'localhost' identified by 'e98d2f001da5678b39482efbdf5770dc';

