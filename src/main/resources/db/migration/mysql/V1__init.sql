-- -----------------------------------------------------
-- Tabla `pools`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pools`
(
    `id`               BIGINT         NOT NULL AUTO_INCREMENT,
    `public_id`        CHAR(36)       NOT NULL COMMENT 'Identificador público de la porra',
    `name`             VARCHAR(80)    NOT NULL,
    `monthly_fee`      DECIMAL(11, 2) NOT NULL COMMENT 'Cuota mensual',
    `num_participants` INT UNSIGNED   NOT NULL,
    `start_date`       DATE           NOT NULL COMMENT 'Fecha de inicio, el dia siempre sera el primero de cada mes',
    `payment_due_day`  INT UNSIGNED   NOT NULL COMMENT 'Dia de vencimiento de la cuota, desde el primero de cada mes',
    `notes`            VARCHAR(500)   NULL COMMENT 'Nota libre del organizador para el grupo (por ejemplo, cómo se paga)',
    `management_code`  VARCHAR(30)    NOT NULL COMMENT 'Clave del organizador',
    `invitation_token` VARCHAR(10)    NOT NULL COMMENT 'Token de invitación',
    `created_at`       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `invitation_token_UNIQUE` (`invitation_token` ASC),
    CONSTRAINT `monthly_fee_NOT_NEGATIVE` CHECK (`monthly_fee` >= 0),
    CONSTRAINT `payment_due_day_NOT_NEGATIVE_MAX` CHECK (`payment_due_day` >= 1 AND `payment_due_day` <= 20),
    CONSTRAINT `num_participants_MAX` CHECK (`num_participants` >= 2 AND `num_participants` <= 30)
)
    ENGINE = InnoDB
    COMMENT = 'Porras';
