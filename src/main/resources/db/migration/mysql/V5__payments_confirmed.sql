ALTER TABLE `payments`
    ADD COLUMN `confirmed` BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'El organizador da el pago por recibido' AFTER `marked`;
