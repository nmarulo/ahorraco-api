-- -----------------------------------------------------
-- Tabla `participants`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `participants`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT,
    `public_id`  CHAR(36)    NOT NULL,
    `pool_id`    BIGINT      NOT NULL,
    `full_name`  VARCHAR(80) NOT NULL,
    `phone`      VARCHAR(20) NULL,
    `created_at` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `pool_full_name_UNIQUE` (`pool_id` ASC, `full_name` ASC),
    CONSTRAINT `fk_participants_pools` FOREIGN KEY (`pool_id`) REFERENCES `pools` (`id`)
)
    ENGINE = InnoDB
    COMMENT = 'Participantes de una porra';
