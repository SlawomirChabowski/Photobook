CREATE TABLE user (
	id INT NOT NULL AUTO_INCREMENT,
	nickname CHAR(20) NOT NULL UNIQUE,
	email CHAR(255) NOT NULL UNIQUE CHECK (email LIKE '%@%.%'),
	password CHAR(20) NOT NULL UNIQUE,
	date_joined DATETIME DEFAULT now() NOT NULL,
	website CHAR(255),
	about TEXT,
	avatar_img_name CHAR(255) UNIQUE,
	PRIMARY KEY(id)
);

CREATE TABLE categories (
	id INT NOT NULL AUTO_INCREMENT,
	name CHAR(20) NOT NULL UNIQUE,
	description TEXT,
	PRIMARY KEY(id)
);

CREATE TABLE tags (
	id INT NOT NULL AUTO_INCREMENT,
	tag CHAR(20) NOT NULL UNIQUE,
	PRIMARY KEY(id)
);

CREATE TABLE image (
	id INT NOT NULL AUTO_INCREMENT,
	title CHAR(100) NOT NULL,
	description BLOB,
	date_added DATETIME DEFAULT now() NOT NULL,
	category_id INT NOT NULL UNIQUE,
	author_id INT NOT NULL UNIQUE,
	img_file_name CHAR(255) NOT NULL UNIQUE,
	PRIMARY KEY(id),
	FOREIGN KEY(category_id) REFERENCES categories(id),
	FOREIGN KEY(author_id) REFERENCES user(id)
);

CREATE TABLE tags_list (
	id_image INT NOT NULL UNIQUE,
	id_tag INTEGER NOT NULL,
	FOREIGN KEY(id_image) REFERENCES image(id),
	FOREIGN KEY(id_tag) REFERENCES tags(id)
);

CREATE TABLE mark (
	id INT NOT NULL AUTO_INCREMENT,
	image_id INT NOT NULL UNIQUE,
	author_id INT NOT NULL UNIQUE,
	value DECIMAL(1) NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY(image_id) REFERENCES image(id),
	FOREIGN KEY(author_id) REFERENCES user(id)
);

CREATE TABLE comment (
	id INT NOT NULL AUTO_INCREMENT,
	image_id INT NOT NULL,
	author_id INT NOT NULL,
	comment_text VARCHAR(10000),
	date_added DATETIME DEFAULT now() NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY(image_id) REFERENCES image(id),
	FOREIGN KEY(author_id) REFERENCES user(id)
);
