/*
create table if not exists users (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  username varchar(256),
  password varchar(256),
  enabled boolean,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
*/
create table if not exists authorities (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  username varchar(256),
  authority varchar(256),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;