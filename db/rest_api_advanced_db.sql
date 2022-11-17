DROP TABLE IF EXISTS `users_have_roles_aud`;
DROP TABLE IF EXISTS `roles_aud`;
DROP TABLE IF EXISTS `orders_details_aud`;
DROP TABLE IF EXISTS `orders_aud`;
DROP TABLE IF EXISTS `users_aud`;
DROP TABLE IF EXISTS `gift_certificates_have_tags_aud`;
DROP TABLE IF EXISTS `tags_aud`;
DROP TABLE IF EXISTS `gift_certificates_aud`;

DROP TABLE IF EXISTS `revinfo`;

DROP TABLE IF EXISTS `users_have_roles`;
DROP TABLE IF EXISTS `roles`;
DROP TABLE IF EXISTS `orders_details`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `gift_certificates_have_tags`;
DROP TABLE IF EXISTS `tags`;
DROP TABLE IF EXISTS `gift_certificates`;

DROP TABLE IF EXISTS `oauth2_authorized_client`;


CREATE TABLE IF NOT EXISTS `revinfo` (
  `rev` INT NOT NULL AUTO_INCREMENT,
  `revtstmp` BIGINT DEFAULT NULL,
  PRIMARY KEY (`rev`)
);

-- -----------------------------------------------------
-- Table `gift_certificates`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gift_certificates` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(45) NOT NULL,
  `price` DECIMAL(7,2) NOT NULL,
  `duration` INT NOT NULL,
  `create_date` TIMESTAMP(3) NOT NULL,
  `last_update_date` TIMESTAMP(3) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC)
);


-- -----------------------------------------------------
-- Table `gift_certificates_aud`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gift_certificates_aud` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `rev` INT NOT NULL,
  `revtype` TINYINT DEFAULT NULL,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(45) NOT NULL,
  `price` DECIMAL(7,2) NOT NULL,
  `duration` INT NOT NULL,
  `create_date` TIMESTAMP(3) NOT NULL,
  `last_update_date` TIMESTAMP(3) NULL,
  PRIMARY KEY (`id`, `rev`),
  CONSTRAINT `fk_gift_certificates_aud_revinfo` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
);


-- -----------------------------------------------------
-- Table `tags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tags` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC)
);


-- -----------------------------------------------------
-- Table `tags_aud`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tags_aud` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `rev` INT NOT NULL,
  `revtype` TINYINT DEFAULT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`, `rev`),
  CONSTRAINT `fk_tags_aud_revinfo` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
);


-- -----------------------------------------------------
-- Table `gift_certificates_have_tags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gift_certificates_have_tags` (
  `gift_certificate_id` INT NOT NULL,
  `tag_id` INT NOT NULL,
  PRIMARY KEY (`gift_certificate_id`, `tag_id`),
  INDEX `fk_gift_certificates_has_tags_tags1_idx` (`tag_id` ASC),
  INDEX `fk_gift_certificates_has_tags_gift_certificates_idx` (`gift_certificate_id` ASC),
  CONSTRAINT `fk_gift_certificates_has_tags_gift_certificates`
    FOREIGN KEY (`gift_certificate_id`)
    REFERENCES `gift_certificates` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_gift_certificates_has_tags_tags1`
    FOREIGN KEY (`tag_id`)
    REFERENCES `tags` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);


-- -----------------------------------------------------
-- Table `gift_certificates_have_tags_aud`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gift_certificates_have_tags_aud` (
  `rev` INT NOT NULL,
  `gift_certificate_id` INT NOT NULL,
  `tag_id` INT NOT NULL,
  `revtype` TINYINT DEFAULT NULL,
  PRIMARY KEY (`rev`, `gift_certificate_id`, `tag_id`),
  CONSTRAINT `fk_gift_certificates_have_tags_aud_revinfo` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
);


-- -----------------------------------------------------
-- Table `users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(256) NULL,
  `name` VARCHAR(45) NULL,
  `address` VARCHAR(45) NULL,
  `phone` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`login` ASC)
);


-- -----------------------------------------------------
-- Table `users_aud`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `users_aud` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `rev` INT NOT NULL,
  `revtype` TINYINT DEFAULT NULL,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(256) NULL,
  `name` VARCHAR(45) NULL,
  `address` VARCHAR(45) NULL,
  `phone` VARCHAR(45) NULL,
  PRIMARY KEY (`id`, `rev`),
  CONSTRAINT `fk_users_aud_revinfo` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
);


-- -----------------------------------------------------
-- Table `orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `orders` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `price` DECIMAL(11,2) NOT NULL,
  `purchase_time` TIMESTAMP(3) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_orders_users1_idx` (`user_id` ASC),
  CONSTRAINT `fk_orders_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);


-- -----------------------------------------------------
-- Table `orders_aud`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `orders_aud` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `rev` INT NOT NULL,
  `revtype` TINYINT DEFAULT NULL,
  `user_id` INT NOT NULL,
  `price` DECIMAL(11,2) NOT NULL,
  `purchase_time` TIMESTAMP(3) NOT NULL,
  PRIMARY KEY (`id`, `rev`),
  CONSTRAINT `fk_orders_aud_revinfo` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
);


-- -----------------------------------------------------
-- Table `orders_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `orders_details` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `order_id` INT NOT NULL,
  `gift_certificate_id` INT NOT NULL,
  `price` DECIMAL(7,2) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_orders_details_orders1_idx` (`order_id` ASC),
  INDEX `fk_orders_details_gift_certificates1_idx` (`gift_certificate_id` ASC),
  CONSTRAINT `fk_orders_details_orders1`
    FOREIGN KEY (`order_id`)
    REFERENCES `orders` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_orders_details_gift_certificates1`
    FOREIGN KEY (`gift_certificate_id`)
    REFERENCES `gift_certificates` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);


-- -----------------------------------------------------
-- Table `orders_details_aud`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `orders_details_aud` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `rev` INT NOT NULL,
  `revtype` TINYINT DEFAULT NULL,
  `order_id` INT NOT NULL,
  `gift_certificate_id` INT NOT NULL,
  `price` DECIMAL(7,2) NOT NULL,
  PRIMARY KEY (`id`, `rev`),
  CONSTRAINT `fk_orders_details_aud_revinfo` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
);


-- -----------------------------------------------------
-- Table `roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `roles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC)
);


-- -----------------------------------------------------
-- Table `roles_aud`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `roles_aud` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `rev` INT NOT NULL,
  `revtype` TINYINT DEFAULT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`, `rev`),
  CONSTRAINT `fk_roles_aud_revinfo` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
);


-- -----------------------------------------------------
-- Table `users_have_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `users_have_roles` (
  `user_id` INT NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`),
  INDEX `fk_users_has_roles_roles1_idx` (`role_id` ASC),
  INDEX `fk_users_has_roles_users1_idx` (`user_id` ASC),
  CONSTRAINT `fk_users_has_roles_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_has_roles_roles1`
    FOREIGN KEY (`role_id`)
    REFERENCES `roles` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);


-- -----------------------------------------------------
-- Table `users_have_roles_aud`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `users_have_roles_aud` (
  `rev` INT NOT NULL,
  `user_id` INT NOT NULL,
  `role_id` INT NOT NULL,
  `revtype` TINYINT DEFAULT NULL,
  PRIMARY KEY (`rev`, `user_id`, `role_id`),
  CONSTRAINT `fk_users_have_roles_aud_revinfo` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`)
);


-- -----------------------------------------------------
-- Table `oauth2_authorized_client`
-- -----------------------------------------------------
CREATE TABLE `oauth2_authorized_client` (
  client_registration_id varchar(100) NOT NULL,
  principal_name varchar(200) NOT NULL,
  access_token_type varchar(100) NOT NULL,
  access_token_value blob NOT NULL,
  access_token_issued_at timestamp NOT NULL,
  access_token_expires_at timestamp NOT NULL,
  access_token_scopes varchar(1000) DEFAULT NULL,
  refresh_token_value blob DEFAULT NULL,
  refresh_token_issued_at timestamp DEFAULT NULL,
  created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY (client_registration_id, principal_name)
);