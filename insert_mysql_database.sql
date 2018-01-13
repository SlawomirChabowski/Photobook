-- MySQL database inserts

-- users
INSERT INTO user (
		nickname,
		email,
		password,
		role
	) VALUES (
		'Admin',
		'admin@photobook.com',
		'haslo123',
		0
);
INSERT INTO user (
		nickname,
		email,
		password
	) VALUES (
		'User',
		'user@photobook.com',
		'haslo123'
);

-- categories
INSERT INTO categories (name, description) VALUES ('3D art', 'Images of three-dimensional graphics');
INSERT INTO categories (name, description) VALUES ('2D art', 'Two-dimensional graphics');
INSERT INTO categories (name, description) VALUES ('Handmade pictures', 'Paintings, graffiti, drawings etc.');
INSERT INTO categories (name) VALUES ('Photographs');
INSERT INTO categories (name) VALUES ('Others');

-- images
-- all of the default images belong to admin
-- and are uploaded in the same time
INSERT INTO image(title, tags, category_id, author_id, img_file_name) VALUES (
	'A book',
	'book, minimalistic',
	4,
	1,
	'img_01.jpg'
);
INSERT INTO image(title, tags, category_id, author_id, img_file_name) VALUES (
	'Waterfall',
	'nature, waterfall, water',
	4,
	1,
	'img_02.jpg'
);
INSERT INTO image(title, description, tags, category_id, author_id, img_file_name) VALUES (
	'Shipwreck',
	'A ruined ship on a desert at night',
	'ship, night, desert, shipwreck, stars, sky',
	4,
	1,
	'img_03.jpg'
);
INSERT INTO image(title, description, tags, category_id, author_id, img_file_name) VALUES (
	'Blue car',
	'Beautiful old-fashioned car that I found somewhere in Berlin',
	'car, blue, cyan, street, old-fashioned, rustic',
	4,
	1,
	'img_04.jpg'
);
INSERT INTO image(title, tags, category_id, author_id, img_file_name) VALUES (
	'Escaping the city',
	'city, fence, street',
	4,
	1,
	'img_05.jpg'
);
INSERT INTO image(title, tags, category_id, author_id, img_file_name) VALUES (
	'Starry night',
	'night, countryside, stars, timelapse',
	4,
	1,
	'img_06.jpg'
);
INSERT INTO image(title, tags, category_id, author_id, img_file_name) VALUES (
	'Teddybear',
	'polar bear, bear, polar, white, nature, animal, animals',
	4,
	1,
	'img_07.jpg'
);
INSERT INTO image(title, tags, category_id, author_id, img_file_name) VALUES (
	'Beautiful',
	'beautiful, black, portrait',
	4,
	1,
	'img_08.jpg'
);
