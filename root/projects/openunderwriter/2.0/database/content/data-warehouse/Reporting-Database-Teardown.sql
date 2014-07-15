drop database if exists ##dbname##_AIL_Base_Report;

revoke all on ##dbname##_AIL_Base_Report.* from 'charlie'@'localhost';
revoke all on ##dbname##_AIL_Base_Report.* from 'lily'@'localhost';
revoke all on ##dbname##_AIL_Base_Report.* from 'oliver'@'localhost';
revoke all on ##dbname##_AIL_Base_Report.* from 'ethan'@'localhost';
revoke all on ##dbname##_AIL_Base_Report.* from 'sophie'@'localhost';
revoke all on ##dbname##_AIL_Base_Report.* from 'thomas'@'localhost';
revoke all on ##dbname##_AIL_Base_Report.* from 'report'@'localhost';
