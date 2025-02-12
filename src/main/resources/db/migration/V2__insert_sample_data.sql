INSERT INTO platform (id, name, description)
VALUES
    ('123e4567-e89b-12d3-a456-426614174000', 'Nintendo Wii U', 'Una plataforma injustamente tratada. Juegos first party de gran calidad.'),
    ('123e4567-e89b-12d3-a456-426614174001', 'Steam', 'Cuanto juego comprado y sin jugar');


INSERT INTO game (id, name, description, platform_id)
VALUES
    ('123e4567-e89b-12d3-a456-426614174000', 'The wonderful 101', 'Estupendo juego de Platinum games', '123e4567-e89b-12d3-a456-426614174000'),
    ('123e4567-e89b-12d3-a456-426614174001', 'Rayman Legends', 'Iba a ser exclusivo de Wii U.', '123e4567-e89b-12d3-a456-426614174000');

