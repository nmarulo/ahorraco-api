-- -----------------------------------------------------
-- Tabla `turns`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `turns`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT,
    `pool_id`        BIGINT       NOT NULL,
    `participant_id` BIGINT       NOT NULL,
    `position`       INT UNSIGNED NOT NULL COMMENT 'Posicion en el orden, empezando en 1',
    `month`          DATE         NOT NULL COMMENT 'Mes en que cobra; siempre el dia 1, como la fecha de inicio de la porra',
    `pinned`         BOOLEAN      NOT NULL DEFAULT FALSE COMMENT 'Posicion reservada.',
    `created_at`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `pool_position_UNIQUE` (`pool_id` ASC, `position` ASC),
    UNIQUE INDEX `pool_participant_UNIQUE` (`pool_id` ASC, `participant_id` ASC),
    UNIQUE INDEX `pool_month_UNIQUE` (`pool_id` ASC, `month` ASC),
    CONSTRAINT `fk_turns_pools` FOREIGN KEY (`pool_id`) REFERENCES `pools` (`id`),
    CONSTRAINT `fk_turns_participants` FOREIGN KEY (`participant_id`) REFERENCES `participants` (`id`)
)
    ENGINE = InnoDB
    COMMENT = 'Orden de cobro de una porra';
