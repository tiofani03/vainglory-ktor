-- Insert roles
INSERT INTO roles (name)
VALUES ('Assassin'),
       ('Mage'),
       ('Protector'),
       ('Sniper'),
       ('Warrior') ON CONFLICT (name) DO NOTHING;

-- Insert positions
INSERT INTO positions (position)
VALUES ('Jungle'),
       ('Lane') ON CONFLICT (position) DO NOTHING;

-- Insert attack types
INSERT INTO attack_types (type)
VALUES ('Melee'),
       ('Ranged') ON CONFLICT (type) DO NOTHING;
