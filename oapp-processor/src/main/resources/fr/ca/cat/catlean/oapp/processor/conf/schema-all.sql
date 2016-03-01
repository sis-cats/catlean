CREATE TABLE `batch-processing`.`consolidation` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `server_name` VARCHAR(255) NOT NULL,
  `ts_start` BIGINT(20) NOT NULL,
  `ts_end` BIGINT(20) NOT NULL,
  `slot_id` INT(11) NOT NULL,
  `job_id(11)` INT(11) NOT NULL,
  PRIMARY KEY (`id`));
