CREATE TABLE `company` (
`id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
`name` VARCHAR(20) NOT NULL,
`create_date` TIMESTAMP DEFAULT NOW()
)