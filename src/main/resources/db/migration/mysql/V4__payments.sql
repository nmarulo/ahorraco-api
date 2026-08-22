-- -----------------------------------------------------
-- Tabla `payments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `payments`
(
    `id`             BIGINT    NOT NULL AUTO_INCREMENT,
    `pool_id`        BIGINT    NOT NULL,
    `participant_id` BIGINT    NOT NULL,
    `month`          DATE      NOT NULL COMMENT 'Mes de la cuota; siempre el dia 1, como el resto de meses',
    `marked`         BOOLEAN   NOT NULL DEFAULT FALSE COMMENT 'El participante dice que ya pago.',
    `created_at`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `pool_participant_month_UNIQUE` (`pool_id` ASC, `participant_id` ASC, `month` ASC),
    CONSTRAINT `fk_payments_pools` FOREIGN KEY (`pool_id`) REFERENCES `pools` (`id`),
    CONSTRAINT `fk_payments_participants` FOREIGN KEY (`participant_id`) REFERENCES `participants` (`id`)
)
    ENGINE = InnoDB
    COMMENT = 'Cuotas de una porra';
