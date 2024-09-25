INSERT INTO cart_table
    (consumer_id)
VALUES
    ('2012db3a-1ad7-4446-bbac-844977e8649b'),
    ('a5794f3a-621a-4bb3-8c2d-c12301c66357'),
    ('ddbaa874-5477-4bdf-bb7b-4475150dcc9d');

INSERT INTO item_table
    (id, item_id, qnt, consumer_id)
VALUES
    ('4550e95d-9ec3-44d6-b337-eb0c7754c591', 1, 2, '2012db3a-1ad7-4446-bbac-844977e8649b'),
    ('114736f0-71b9-4180-8ceb-5bebd139ae62', 2, 3, '2012db3a-1ad7-4446-bbac-844977e8649b'),

    ('f2c3fe10-1933-4dba-b6af-b00258b90b73', 1, 2, 'a5794f3a-621a-4bb3-8c2d-c12301c66357'),
    ('189c415a-ae87-4095-b7cf-5d5cc1042080', 2, 2, 'a5794f3a-621a-4bb3-8c2d-c12301c66357'),
    ('46099ae1-02a2-44ec-8567-c33816650e44', 3, 3, 'a5794f3a-621a-4bb3-8c2d-c12301c66357');