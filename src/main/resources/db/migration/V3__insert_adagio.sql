ALTER TABLE skin_statuses ADD CONSTRAINT unique_skin_per_hero UNIQUE (hero_id, skin_name);
ALTER TABLE skill_statuses ADD CONSTRAINT unique_skill_per_hero UNIQUE (hero_id, skill_name);
ALTER TABLE power_statuses ADD CONSTRAINT unique_power_per_hero UNIQUE (hero_id, name);

WITH role_id AS (SELECT id
                 FROM roles
                 WHERE name = 'Protector'),
     attack_type_id AS (SELECT id
                        FROM attack_types
                        WHERE type = 'Ranged'),
     position_id AS (SELECT id
                     FROM positions
                     WHERE position = 'Jungle')
INSERT
INTO heroes (name, image, image_background, description, role_id, attack_type_id, position_id)
SELECT 'Adagio',
       'https://firebasestorage.googleapis.com/v0/b/notess-c8095.appspot.com/o/heroes%2Fadagio%2Fadagio.png?alt=media&token=64c6f8c6-5fb2-4a64-8e4a-0dc22de75213',
       'https://firebasestorage.googleapis.com/v0/b/notess-c8095.appspot.com/o/heroes%2Fadagio%2FAdagioActive.png?alt=media&token=a1b11f28-cf1c-44ce-9484-9cb661465966',
       'The master manipulator of large-scale battles, Adagio brings incredible teamfight presence with huge area-of-effect damage and team-saving abilities. He almost seems too good: He can heal. He can amplify damage. He can stun and nuke the entire enemy team ... but none of these can be achieved easily without team-wide coordination. Adagio is extremely flexible and can start as carry, jungler or captain.',
       (SELECT id FROM role_id),
       (SELECT id FROM attack_type_id),
       (SELECT id FROM position_id) ON CONFLICT (name) DO NOTHING;

-- Insert Skins
INSERT INTO skin_statuses (hero_id, skin_name, skin_image, skin_type)
SELECT id,
       'Default',
       'https://firebasestorage.googleapis.com/v0/b/notess-c8095.appspot.com/o/heroes%2Fadagio%2Fadagio.png?alt=media&token=64c6f8c6-5fb2-4a64-8e4a-0dc22de75213',
       'DEFAULT'
FROM heroes
WHERE name = 'Adagio'
UNION ALL
SELECT id,
       'Parade Adagio (R)',
       'https://firebasestorage.googleapis.com/v0/b/notess-c8095.appspot.com/o/heroes%2Fadagio%2FAdagioT1Flat.jpg?alt=media&token=d04437ed-ebad-49d7-bf72-17f43547d97e',
       'RARE'
FROM heroes
WHERE name = 'Adagio'
UNION ALL
SELECT id,
       'Parade Adagio (E)',
       'https://firebasestorage.googleapis.com/v0/b/notess-c8095.appspot.com/o/heroes%2Fadagio%2FAdagioT2Flat.jpg?alt=media&token=3203c57b-1d97-46f3-a292-208c39d6d350',
       'EPIC'
FROM heroes
WHERE name = 'Adagio'
UNION ALL
SELECT id,
       'Parade Adagio (L)',
       'https://firebasestorage.googleapis.com/v0/b/notess-c8095.appspot.com/o/heroes%2Fadagio%2FAdagioT3Flat.jpg?alt=media&token=b5f4cbf3-66cf-48b3-aa5a-7cd806de353c',
       'LEGENDARY'
FROM heroes
WHERE name = 'Adagio' ON CONFLICT (hero_id, skin_name) DO NOTHING;

-- Insert Skills
INSERT INTO skill_statuses (hero_id, skill_name, skill_desc, skill_image)
SELECT id,
       'Arcane Renewal (Perk)',
       'Whenever enemies burning with Arcane Fire take damage from any source, Adagio regenerates 35% of that damage as energy.',
       'https://firebasestorage.googleapis.com/v0/b/notess-c8095.appspot.com/o/heroes%2Fadagio%2Fadagio_p.webp?alt=media&token=6a47631a-79f8-4a0e-893b-d7ed9c56e98f'
FROM heroes
WHERE name = 'Adagio'
UNION ALL
SELECT id,
       'Gift of Fire',
       'Adagio heals a target ally and splashes Arcane Fire onto nearby enemies, dealing damage every second. If Adagio targets himself, he''ll also slow nearby enemies by 70% for 1.5 seconds.',
       'https://firebasestorage.googleapis.com/v0/b/notess-c8095.appspot.com/o/heroes%2Fadagio%2Fadagio_a.webp?alt=media&token=6e02d464-b21c-411a-9734-9d31a10f05da'
FROM heroes
WHERE name = 'Adagio'
UNION ALL
SELECT id,
       'Verse of Judgement',
       'Adagio channels for 2 seconds then deals heavy damage to all enemies in a wide, rune-marked ring around him. Enemies burning with Arcane Fire are also stunned. Adagio temporarily gains fortified health during channeling.',
       'https://firebasestorage.googleapis.com/v0/b/notess-c8095.appspot.com/o/heroes%2Fadagio%2Fadagio_c.webp?alt=media&token=af763c32-6126-4b36-a308-3fc50396565c'
FROM heroes
WHERE name = 'Adagio' ON CONFLICT (hero_id, skill_name) DO NOTHING;

-- Insert State Color
INSERT INTO state_colors (hero_id, vibrant, dark_vibrant, on_dark_vibrant)
SELECT id, '#28A8E8', '#105878', '#ADFFFFFF'
FROM heroes
WHERE name = 'Adagio' ON CONFLICT (hero_id) DO NOTHING;

-- Insert Power Status
INSERT INTO power_statuses (hero_id, name, current_value, max_value)
SELECT id, 'Health', 1654.0, 2700.0 FROM heroes WHERE name = 'Adagio'
UNION ALL
SELECT id, 'Armor', 86.0, 100.0 FROM heroes WHERE name = 'Adagio'
UNION ALL
SELECT id, 'Attack', 117.9, 185.0 FROM heroes WHERE name = 'Adagio'
UNION ALL
SELECT id, 'Movement Speed', 32.0, 40.0 FROM heroes WHERE name = 'Adagio'
    ON CONFLICT (hero_id, name) DO NOTHING;