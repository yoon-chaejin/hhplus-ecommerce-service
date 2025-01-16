INSERT INTO product (id, name, remaining_quantity, unit_price, created_at, updated_at)
VALUES (1, "허재님의 생일케이크", 10, 500, STR_TO_DATE('2025010112', '%Y%m%d%H'), STR_TO_DATE('2025010112', '%Y%m%d%H'));
INSERT INTO product (id, name, remaining_quantity, unit_price, created_at, updated_at)
VALUES (2, "한결님의 응원봉", 10, 500, STR_TO_DATE('2025010112', '%Y%m%d%H'), STR_TO_DATE('2025010112', '%Y%m%d%H'));
INSERT INTO product (id, name, remaining_quantity, unit_price, created_at, updated_at)
VALUES (3, "희니의 불주먹", 0, 500, STR_TO_DATE('2025010112', '%Y%m%d%H'), STR_TO_DATE('2025010112', '%Y%m%d%H'));
INSERT INTO product (id, name, remaining_quantity, unit_price, created_at, updated_at)
VALUES (4, "덕이의 캣타워", 0, 500, STR_TO_DATE('2025010112', '%Y%m%d%H'), STR_TO_DATE('2025010112', '%Y%m%d%H'));
INSERT INTO product (id, name, remaining_quantity, unit_price, created_at, updated_at)
VALUES (5, "민이의 이중생활", 0, 500, STR_TO_DATE('2025010112', '%Y%m%d%H'), STR_TO_DATE('2025010112', '%Y%m%d%H'));
INSERT INTO product (id, name, remaining_quantity, unit_price, created_at, updated_at)
VALUES (6, "영이의 음주가무", 2, 500, STR_TO_DATE('2025010112', '%Y%m%d%H'), STR_TO_DATE('2025010112', '%Y%m%d%H'));

INSERT INTO coupon_template (id, discount_rate, issue_count, max_issue_count, issuable_until, created_at, updated_at)
VALUES (1, 10, 1, 10, STR_TO_DATE('20250201', '%Y%m%d'), STR_TO_DATE('20250201', '%Y%m%d'),STR_TO_DATE('20250201', '%Y%m%d'));
INSERT INTO coupon_template (id, discount_rate, issue_count, max_issue_count, issuable_until, created_at, updated_at)
VALUES (2, 10, 10, 10, STR_TO_DATE('20250201', '%Y%m%d'), STR_TO_DATE('20250201', '%Y%m%d'),STR_TO_DATE('20250201', '%Y%m%d'));
INSERT INTO coupon_template (id, discount_rate, issue_count, max_issue_count, issuable_until, created_at, updated_at)
VALUES (3, 10, 0, 10, STR_TO_DATE('20250201', '%Y%m%d'), STR_TO_DATE('20250201', '%Y%m%d'),STR_TO_DATE('20250201', '%Y%m%d'));


INSERT INTO issued_coupon (id, template_id, owned_by, expires_at, used_at, created_at, updated_at)
VALUES (1, 1, 1, STR_TO_DATE('20250201', '%Y%m%d'), null, STR_TO_DATE('20250201', '%Y%m%d'),STR_TO_DATE('20250201', '%Y%m%d'));

INSERT INTO point (id, user_id, balance, created_at, updated_at)
VALUES (1, 1, 1000, STR_TO_DATE('20250201', '%Y%m%d'),STR_TO_DATE('20250201', '%Y%m%d'));
INSERT INTO point (id, user_id, balance, created_at, updated_at)
VALUES (2, 2, 1000000, STR_TO_DATE('20250201', '%Y%m%d'),STR_TO_DATE('20250201', '%Y%m%d'));
INSERT INTO point (id, user_id, balance, created_at, updated_at)
VALUES (10, 10, 1000, STR_TO_DATE('20250201', '%Y%m%d'),STR_TO_DATE('20250201', '%Y%m%d'));

INSERT INTO `order` (id, coupon_id,ordered_by,payment_price,total_price,created_at,updated_at)
VALUES (1, null,1,1500,1500,STR_TO_DATE('20250103', '%Y%m%d'),STR_TO_DATE('20250103', '%Y%m%d'));
INSERT INTO order_product (id, product_id,quantity,unit_price,created_at,updated_at)
VALUES(1,1,1,100,STR_TO_DATE('20250103', '%Y%m%d'),STR_TO_DATE('20250103', '%Y%m%d'));
INSERT INTO order_product (id, product_id,quantity,unit_price,created_at,updated_at)
VALUES(2,2,2,100,STR_TO_DATE('20250103', '%Y%m%d'),STR_TO_DATE('20250103', '%Y%m%d'));
INSERT INTO order_product (id, product_id,quantity,unit_price,created_at,updated_at)
VALUES(3,3,3,100,STR_TO_DATE('20250103', '%Y%m%d'),STR_TO_DATE('20250103', '%Y%m%d'));
INSERT INTO order_product (id, product_id,quantity,unit_price,created_at,updated_at)
VALUES(4,4,4,100,STR_TO_DATE('20250103', '%Y%m%d'),STR_TO_DATE('20250103', '%Y%m%d'));
INSERT INTO order_product (id, product_id,quantity,unit_price,created_at,updated_at)
VALUES(5,5,5,100,STR_TO_DATE('20250103', '%Y%m%d'),STR_TO_DATE('20250103', '%Y%m%d'));

INSERT INTO `order` (id, coupon_id,ordered_by,payment_price,total_price,created_at,updated_at)
VALUES (2, null,1,1500,1500,STR_TO_DATE('20250107', '%Y%m%d'),STR_TO_DATE('20250107', '%Y%m%d'));
INSERT INTO order_product (id, product_id,quantity,unit_price,created_at,updated_at)
VALUES(6,1,1,100,STR_TO_DATE('20250107', '%Y%m%d'),STR_TO_DATE('20250107', '%Y%m%d'));
INSERT INTO order_product (id, product_id,quantity,unit_price,created_at,updated_at)
VALUES(7,2,3,100,STR_TO_DATE('20250107', '%Y%m%d'),STR_TO_DATE('20250107', '%Y%m%d'));
INSERT INTO order_product (id, product_id,quantity,unit_price,created_at,updated_at)
VALUES(8,3,2,100,STR_TO_DATE('20250107', '%Y%m%d'),STR_TO_DATE('20250107', '%Y%m%d'));
INSERT INTO order_product (id, product_id,quantity,unit_price,created_at,updated_at)
VALUES(9,4,4,100,STR_TO_DATE('20250107', '%Y%m%d'),STR_TO_DATE('20250107', '%Y%m%d'));
INSERT INTO order_product (id, product_id,quantity,unit_price,created_at,updated_at)
VALUES(10,5,5,100,STR_TO_DATE('20250107', '%Y%m%d'),STR_TO_DATE('20250107', '%Y%m%d'));

INSERT INTO `order` (id, coupon_id,ordered_by,payment_price,total_price,created_at,updated_at)
VALUES (3, null,1,500,500,STR_TO_DATE('20250110', '%Y%m%d'),STR_TO_DATE('20250110', '%Y%m%d'));
INSERT INTO order_product (id, product_id,quantity,unit_price,created_at,updated_at)
VALUES(11,1,1,100,STR_TO_DATE('20250110', '%Y%m%d'),STR_TO_DATE('20250110', '%Y%m%d'));
INSERT INTO order_product (id, product_id,quantity,unit_price,created_at,updated_at)
VALUES(12,2,1,100,STR_TO_DATE('20250110', '%Y%m%d'),STR_TO_DATE('20250110', '%Y%m%d'));
INSERT INTO order_product (id, product_id,quantity,unit_price,created_at,updated_at)
VALUES(13,3,1,100,STR_TO_DATE('20250110', '%Y%m%d'),STR_TO_DATE('20250110', '%Y%m%d'));
INSERT INTO order_product (id, product_id,quantity,unit_price,created_at,updated_at)
VALUES(14,4,1,100,STR_TO_DATE('20250110', '%Y%m%d'),STR_TO_DATE('20250110', '%Y%m%d'));
INSERT INTO order_product (id, product_id,quantity,unit_price,created_at,updated_at)
VALUES(15,5,1,100,STR_TO_DATE('20250110', '%Y%m%d'),STR_TO_DATE('20250110', '%Y%m%d'));
