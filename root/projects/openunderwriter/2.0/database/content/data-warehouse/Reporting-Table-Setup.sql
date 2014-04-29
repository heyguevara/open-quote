 create table if not exists ##dbname##.bordereau (
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
      product varchar(255)
  ) ENGINE=InnoDB;