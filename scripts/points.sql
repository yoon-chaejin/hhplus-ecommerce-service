DELIMITER $$

CREATE PROCEDURE InsertPoints()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 100 DO
            INSERT INTO point (id, user_id, balance, created_at, updated_at)
            VALUES (i, i, 0, STR_TO_DATE('20250201', '%Y%m%d'), STR_TO_DATE('20250201', '%Y%m%d'));
            SET i = i + 1;
        END WHILE;
END$$

DELIMITER ;

-- 프로시저 실행
CALL InsertPoints();