CREATE TABLE `feedback` (
  `order_id` bigint(20) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `buyer_id` bigint(20) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `score` varchar(255) NOT NULL,
  `seller_id` bigint(20) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `store_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;