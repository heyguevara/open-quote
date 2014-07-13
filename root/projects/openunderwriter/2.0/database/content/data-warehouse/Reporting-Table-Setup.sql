-- Create example reporting database for use by OpenQuote users
create database if not exists ##dbname##_AIL_Base_Report character set utf8;
grant insert,select,update,delete on ##dbname##_AIL_Base_Report.* to 'adam'@'localhost' identified by 'Dhj0TB/sA+xAg0IstYumoJrE+yo=';
grant select on ##dbname##_AIL_Base_Report.* to 'charlie'@'localhost' identified by '2M0QuSDcvbUWPKAYXkAjV7wnwmU=';
grant select on ##dbname##_AIL_Base_Report.* to 'lily'@'localhost' identified by '/dvgGFfi06tQW/wdrDs4osi4zWU=';
grant select on ##dbname##_AIL_Base_Report.* to 'oliver'@'localhost' identified by 'xTkVO6H5R71Lb5ECY7lnxKCmI1c=';
grant select on ##dbname##_AIL_Base_Report.* to 'ethan'@'localhost' identified by 'CifhLQYq1xZz1X+cJ5myB68xaIU=';
grant select on ##dbname##_AIL_Base_Report.* to 'sophie'@'localhost' identified by 'X1BEO/529yeajg8vCpiXXNv/OOk=';
grant select on ##dbname##_AIL_Base_Report.* to 'thomas'@'localhost' identified by 'X1CoTB+jvP8UZAUBfzauwaEKnjg=';
grant select on ##dbname##_AIL_Base_Report.* to 'report'@'localhost' identified by 'e98d2f001da5678b39482efbdf5770dc';
use ##dbname##_AIL_Base_Report;
 
create table if not exists ##dbname##_AIL_Base_Report.bordereau (
      id bigint(20) auto_increment primary key,
      broker varchar(255),
      quote_number varchar(255),
      policy_number varchar(255),
      quote_date date,
      accepted_date date,
      status varchar(255),
	  policy_holder_name varchar(255),
      premium_amount decimal(19,2),
      premium_currency varchar(255),
      product varchar(255),
      commission decimal(19,2),
      tax decimal(19,2)
  ) ENGINE=InnoDB;
  
  create table if not exists ##dbname##_AIL_Base_Report.report_summary (
      id bigint(20) auto_increment primary key,
      broker varchar(255),
      quote_date date,
      accepted_date date,
      status varchar(255),
      premium_amount decimal(19,2),
      premium_currency varchar(255),
      product varchar(255)
  ) ENGINE=InnoDB;
  
  create table if not exists ##dbname##_AIL_Base_Report.pre_policy (
      id bigint(20) auto_increment primary key,
      broker varchar(255),
	  quote_number varchar(255),
	  quote_date date,
	  status varchar(255),
	  page varchar(255),
	  policy_holder_name varchar(255),
	  policy_holder_number varchar(255),
	  policy_holder_email varchar(255),
	  premium_amount decimal(19,2),
	  premium_currency varchar(255),
	  product varchar(255)
  ) ENGINE=InnoDB;