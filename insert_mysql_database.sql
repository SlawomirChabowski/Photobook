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
