-- Drop the example actuarial databases and remove all access rights associated with them
drop database if exists ##dbname##_AIL_Base_DataSource_Master_Motor;
drop database if exists ##dbname##_AIL_Base_DataSource_Test;
drop database if exists ##dbname##_MetropolitanInsurance_Motor_Staging;
drop database if exists ##dbname##_StarInsurance_Motor_Staging;

revoke all privileges, grant option from 'charlie'@'localhost';
revoke all privileges, grant option from 'lily'@'localhost';
revoke all privileges, grant option from 'oliver'@'localhost'; 
revoke all privileges, grant option from 'ethan'@'localhost';
revoke all privileges, grant option from 'sophie'@'localhost';
revoke all privileges, grant option from 'thomas'@'localhost';
revoke all privileges, grant option from 'report'@'localhost';

