-- =======                                                                                                                                                                 
-- BID                                                                                                                                                                 
-- =======                                                                                                                                                                 
CREATE DATABASE IF NOT EXISTS `BID` DEFAULT CHARACTER SET = utf8;

-- -----                                                                                                                                                                   
-- BID                                                                                                                                                                   
-- -----                                                                                                                                                                   
DROP TABLE if EXISTS `BID`;

CREATE TABLE `bid` (
  `SOURCE_ID` VARCHAR(64) NOT NULL,
  `SOURCE` varchar(32) NOT NULL,
  `BID` numeric(10,4),
  `CREATED_AT` TIMESTAMP NOT NULL,
  `UPDATED_AT` TIMESTAMP NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8
;

