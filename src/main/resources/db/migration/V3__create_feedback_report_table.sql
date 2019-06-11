CREATE TABLE `feedback_report` (
  `store_id` varchar(255) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `points` bigint(20) NOT NULL,
  `total_count` bigint(20) NOT NULL,
  `rank` varchar(255) NOT NULL,
  PRIMARY KEY (`store_id`),
  CONSTRAINT feedback_report_fkey FOREIGN KEY (`store_id`) REFERENCES feedback(`store_id`)
) ENGINE=MyISAM DEFAULT CHARSET=UTF8;