INSERT INTO order_table
    (order_id, consumer_id,payment_type, value, status)
VALUES
    ('8a54cd2f-19f1-4c91-8900-fb8f8f5a9b4d', '2012db3a-1ad7-4446-bbac-844977e8649b', 'br_credit_card', 1500, 'pending'),
    ('f95d1624-433d-49fa-9496-975f5912666e', 'a5794f3a-621a-4bb3-8c2d-c12301c66357', 'br_debit_card', 5000, 'approved'),
    ('08f2beac-19fc-4fa5-aed8-0d8f632e7c30', 'a5794f3a-621a-4bb3-8c2d-c12301c66357', 'br_credit_card', 3500, 'declined');

INSERT INTO item_table
    (id, item_id, qnt, order_id)
VALUES
    ('9a4a9f47-98ae-469f-9c45-1d70e8ab2501', 1, 1, '8a54cd2f-19f1-4c91-8900-fb8f8f5a9b4d'),

    ('2678351d-382d-4788-8226-e12ca8879149', 1, 1, 'f95d1624-433d-49fa-9496-975f5912666e'),
    ('ffb047ce-384a-4600-bdda-88c0ec64b950', 2, 1, 'f95d1624-433d-49fa-9496-975f5912666e'),
    ('d595ebde-4b29-4e73-abb6-f50f663da65a', 3, 1, 'f95d1624-433d-49fa-9496-975f5912666e'),

    ('59b5a53f-6ffc-4de6-9424-b8b2c803c727', 1, 1, '08f2beac-19fc-4fa5-aed8-0d8f632e7c30'),
    ('5d8e51e6-053d-487f-9033-4773ec273af9', 2, 1, '08f2beac-19fc-4fa5-aed8-0d8f632e7c30');