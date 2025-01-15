INSERT INTO product (id, name, remaining_quantity, unit_price, created_at, updated_at)
VALUES (1, "허재님의 생일케이크", 10, 500, STR_TO_DATE('2025010112', '%Y%m%d%H'), STR_TO_DATE('2025010112', '%Y%m%d%H'));

INSERT INTO coupon_template (id, discount_rate, issue_count, max_issue_count, issuable_until, created_at, updated_at)
VALUES (1, 10, 1, 10, STR_TO_DATE('20250201', '%Y%m%d'), STR_TO_DATE('20250201', '%Y%m%d'),STR_TO_DATE('20250201', '%Y%m%d'));
INSERT INTO coupon_template (id, discount_rate, issue_count, max_issue_count, issuable_until, created_at, updated_at)
VALUES (2, 10, 10, 10, STR_TO_DATE('20250201', '%Y%m%d'), STR_TO_DATE('20250201', '%Y%m%d'),STR_TO_DATE('20250201', '%Y%m%d'));
INSERT INTO coupon_template (id, discount_rate, issue_count, max_issue_count, issuable_until, created_at, updated_at)
VALUES (3, 10, 0, 10, STR_TO_DATE('20250201', '%Y%m%d'), STR_TO_DATE('20250201', '%Y%m%d'),STR_TO_DATE('20250201', '%Y%m%d'));


INSERT INTO issued_coupon (id, template_id, owned_by, expires_at, used_at, created_at, updated_at)
VALUES (1, 1, 1, STR_TO_DATE('20250201', '%Y%m%d'), null, STR_TO_DATE('20250201', '%Y%m%d'),STR_TO_DATE('20250201', '%Y%m%d'));

INSERT INTO point (id, user_id, balance, created_at, updated_at)
VALUES (1, 1, 1000, STR_TO_DATE('20250201', '%Y%m%d'),STR_TO_DATE('20250201', '%Y%m%d'))