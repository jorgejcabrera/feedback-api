CREATE TABLE `feedback_report` (
  `store_id` varchar(255) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `last_rank` varchar(255) NOT NULL,
  `rank` varchar(255) NOT NULL,
  PRIMARY KEY (`store_id`),
  CONSTRAINT feedback_report_fkey FOREIGN KEY (`store_id`) REFERENCES feedback(`store_id`)
) ENGINE=MyISAM DEFAULT CHARSET=UTF8;