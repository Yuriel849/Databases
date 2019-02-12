CREATE TABLE order_d (
    ord_num    VARCHAR2(20),
    ord_id     NUMBER,
    cust_num   VARCHAR2(20),
    isbn13     NUMBER,
    isbn10     NUMBER,
    quant      NUMBER,
    ord_date   NUMBER,
    price      NUMBER
);

CREATE TABLE order_m (
    ord_num      VARCHAR2(20),
    cust_num     VARCHAR2(20),
    price        NUMBER,
    ord_status   CHAR(5),
    quant        NUMBER
);

SELECT
    order_d.*,
    order_m.price        AS total_price,
    order_m.quant        AS total_quantity,
    order_m.ord_status   AS status
FROM
    order_d,
    order_m
WHERE
    order_d.ord_num = order_m.ord_num;