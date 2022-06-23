use auction;

CREATE TABLE `auction`.`user` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(50) NOT NULL,
	`email` VARCHAR(100) NOT NULL,
	`password` VARCHAR(100) NULL,
	`role` INT NOT NULL DEFAULT 1,
	`point` INT NULL DEFAULT 0,
	`image` VARCHAR(100)       NULL,
	`email_verified` BIT(1)  NULL DEFAULT 0,
	`provider`       VARCHAR(100)      NOT NULL,
	`provider_id`    VARCHAR(100)       NULL,
	`id_watch_list` INT NULL,
	`id_bid_list` INT NULL,
    `rf_token` VARCHAR(32) NULL,
    `verification_code` VARCHAR(6) NULL,
    `enabled` BIT(1) NOT NULL DEFAULT 0,
	UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
	UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
	CHECK (role = 0 OR role = 1 OR role = 2),
	CONSTRAINT pk_user PRIMARY KEY (`id`)
);

create table `auction`.`category`(
	`id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL UNIQUE,
	UNIQUE INDEX `category_name_UNIQUE` (`name` ASC) VISIBLE,
    CONSTRAINT pk_category PRIMARY KEY (`id`)
);

create table `auction`.`subcategory`(
	`id` INT NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(100) NOT NULL UNIQUE,
    `category_id` INT NOT NULL,
	UNIQUE INDEX `subcategory_name_UNIQUE` (`name` ASC) VISIBLE,
    CONSTRAINT pk_subcategory PRIMARY KEY (`id`)
);

create table `auction`.`product`(
	`id` INT NOT NULL AUTO_INCREMENT,
     `name` VARCHAR(100) NOT NULL,
     `current_price` DECIMAL(15,2) NOT NULL,
     `quick_price` DECIMAL(15,2),
     `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
     `end_at` DATETIME NOT NULL,
     `description` VARCHAR(255),
     `bidder_highest_id` INT,
     `seller_id` INT NOT NULL,
     `subcategory_id` INT NOT NULL,
     CONSTRAINT pk_product PRIMARY KEY(`id`)
);

create table `auction`.`image`(
	`id` INT NOT NULL AUTO_INCREMENT,
    `product_id` INT NOT NULL,
    `main` BIT(1) NOT NULL,
    `url` VARCHAR(255) NOT NULL,
    CONSTRAINT pk_image PRIMARY KEY(`id`)
);

create table `auction`.`product_bidder`(
	`id` INT NOT NULL AUTO_INCREMENT,
    `product_id` INT NOT NULL,
    `bidder_id` INT NOT NULL,
    `bid_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `price` DECIMAL(15,2) NOT NULL,
    CONSTRAINT pk_product_bidder PRIMARY KEY(`id`)
);

ALTER TABLE product
ADD CONSTRAINT FK_Product_Subcategory
FOREIGN KEY (subcategory_id) REFERENCES subcategory(id);

ALTER TABLE product
ADD CONSTRAINT FK_Product_Bidder_Highest
FOREIGN KEY (bidder_highest_id) REFERENCES user(id);

ALTER TABLE product
ADD CONSTRAINT FK_Product_Seller
FOREIGN KEY (seller_id) REFERENCES user(id);

ALTER TABLE product
ADD CONSTRAINT FK_Product_Bidder
FOREIGN KEY (seller_id) REFERENCES user(id);

ALTER TABLE product_bidder
ADD CONSTRAINT FK_Product_Bidder_Product
FOREIGN KEY (product_id) REFERENCES product(id);

ALTER TABLE product_bidder
ADD CONSTRAINT FK_Product_Bidder_Bidder
FOREIGN KEY (bidder_id) REFERENCES user(id);

ALTER TABLE image
ADD CONSTRAINT FK_Image_Product
FOREIGN KEY (product_id) REFERENCES product(id);

ALTER TABLE subcategory
ADD CONSTRAINT FK_Subcategory_Category
FOREIGN KEY (category_id) REFERENCES category(id);

INSERT INTO auction.category(name, id)
VALUES ('Tất Cả', 1),
       ('Điện Tử', 2),
       ('Phụ Kiện', 3),
       ('Nội Thất', 4),
       ('Trang Sức', 5),
       ('Tranh Vẽ', 6),
       ('Phương Tiện Di Chuyển', 7);

INSERT INTO auction.subcategory(name, category_id)
VALUES
    # Điện Tử
    ('Laptop', 2),
    ('Smart Phone', 2),
    # Phụ Kiện
    ('Đồng Hồ', 3),
    ('Thắt Lưng', 3),
    # Nội Thất
    ('Giường', 4),
    ('Bàn Ghế', 4),
    # Trang Sức
    ('Nhẫn', 5),
    ('Vòng Cổ', 5),
    # Tranh Vẽ
    ('Kỹ Thuật Số', 6),
    ('Hiện Vật', 6),
    # Phương Tiện Di Chuyển
    ('Xe Hơi', 7),
    ('Tàu Bè', 7);

INSERT INTO user(name, email, password, role, point, provider, enabled) VALUES ('Viet Hoang', 'daoleviethoang@gmail.com', '$2a$12$FizdQ45Yx0GEATaGDJWbXeDW3gTfPdBKQ5qRIHYKk4bKYeC0TUL6y', 1, 0, 'local', 1);

INSERT INTO product_bidder(product_id, bidder_id, price) VALUES(2, 3, 1550);
INSERT INTO product_bidder(product_id, bidder_id, price) VALUES(3, 3, 1450);
INSERT INTO product_bidder(product_id, bidder_id, price) VALUES(4, 3, 1350);
INSERT INTO product_bidder(product_id, bidder_id, price) VALUES(2, 4, 1530);
INSERT INTO product_bidder(product_id, bidder_id, price) VALUES(5, 4, 1450);
INSERT INTO product_bidder(product_id, bidder_id, price) VALUES(7, 4, 1350);
INSERT INTO product_bidder(product_id, bidder_id, price) VALUES(6, 4, 1350);
INSERT INTO product_bidder(product_id, bidder_id, price) VALUES(2, 4, 1560);
INSERT INTO product_bidder(product_id, bidder_id, price) VALUES(3, 4, 1460);
INSERT INTO product_bidder(product_id, bidder_id, price) VALUES(4, 3, 1360);
INSERT INTO product_bidder(product_id, bidder_id, price) VALUES(5, 3, 1450);
INSERT INTO product_bidder(product_id, bidder_id, price) VALUES(7, 3, 1350);
INSERT INTO product_bidder(product_id, bidder_id, price) VALUES(6, 3, 1350);
INSERT INTO product_bidder(product_id, bidder_id, price) VALUES(5, 3, 1450);
INSERT INTO product_bidder(product_id, bidder_id, price) VALUES(5, 4, 1450);
INSERT INTO product_bidder(product_id, bidder_id, price) VALUES(7, 3, 1350);
INSERT INTO product_bidder(product_id, bidder_id, price) VALUES(7, 3, 1350);
INSERT INTO product_bidder(product_id, bidder_id, price) VALUES(7, 3, 1350);
INSERT INTO product_bidder(product_id, bidder_id, price) VALUES(7, 3, 1350);
INSERT INTO product_bidder(product_id, bidder_id, price) VALUES(6, 3, 1350);

INSERT INTO image(product_id, main, url) VALUES(2, 1, "https://images.fpt.shop/unsafe/fit-in/960x640/filters:quality(90):fill(white):upscale()/fptshop.com.vn/Uploads/Originals/2021/11/4/637716443491808351_5.jpg");
INSERT INTO image(product_id, main, url) VALUES(2, 0, "https://images.fpt.shop/unsafe/fit-in/960x640/filters:quality(90):fill(white):upscale()/fptshop.com.vn/Uploads/Originals/2021/11/4/637716443491808351_3.jpg");

INSERT INTO image(product_id, main, url) VALUES(3, 1, "https://images.fpt.shop/unsafe/fit-in/960x640/filters:quality(90):fill(white):upscale()/fptshop.com.vn/Uploads/Originals/2021/11/4/637716443491808351_5.jpg");
INSERT INTO image(product_id, main, url) VALUES(3, 0, "https://images.fpt.shop/unsafe/fit-in/960x640/filters:quality(90):fill(white):upscale()/fptshop.com.vn/Uploads/Originals/2021/11/4/637716443491808351_3.jpg");
INSERT INTO image(product_id, main, url) VALUES(4, 1, "https://images.fpt.shop/unsafe/fit-in/960x640/filters:quality(90):fill(white):upscale()/fptshop.com.vn/Uploads/Originals/2021/11/4/637716443491808351_5.jpg");
INSERT INTO image(product_id, main, url) VALUES(4, 0, "https://images.fpt.shop/unsafe/fit-in/960x640/filters:quality(90):fill(white):upscale()/fptshop.com.vn/Uploads/Originals/2021/11/4/637716443491808351_3.jpg");
INSERT INTO image(product_id, main, url) VALUES(5, 1, "https://images.fpt.shop/unsafe/fit-in/960x640/filters:quality(90):fill(white):upscale()/fptshop.com.vn/Uploads/Originals/2021/11/4/637716443491808351_5.jpg");
INSERT INTO image(product_id, main, url) VALUES(5, 0, "https://images.fpt.shop/unsafe/fit-in/960x640/filters:quality(90):fill(white):upscale()/fptshop.com.vn/Uploads/Originals/2021/11/4/637716443491808351_3.jpg");
INSERT INTO image(product_id, main, url) VALUES(6, 1, "https://images.fpt.shop/unsafe/fit-in/960x640/filters:quality(90):fill(white):upscale()/fptshop.com.vn/Uploads/Originals/2021/11/4/637716443491808351_5.jpg");
INSERT INTO image(product_id, main, url) VALUES(6, 0, "https://images.fpt.shop/unsafe/fit-in/960x640/filters:quality(90):fill(white):upscale()/fptshop.com.vn/Uploads/Originals/2021/11/4/637716443491808351_3.jpg");
INSERT INTO image(product_id, main, url) VALUES(7, 1, "https://images.fpt.shop/unsafe/fit-in/960x640/filters:quality(90):fill(white):upscale()/fptshop.com.vn/Uploads/Originals/2021/11/4/637716443491808351_5.jpg");
INSERT INTO image(product_id, main, url) VALUES(7, 0, "https://images.fpt.shop/unsafe/fit-in/960x640/filters:quality(90):fill(white):upscale()/fptshop.com.vn/Uploads/Originals/2021/11/4/637716443491808351_3.jpg");

INSERT INTO image(product_id, main, url) VALUES(9, 1, "https://images.fpt.shop/unsafe/fit-in/960x640/filters:quality(90):fill(white):upscale()/fptshop.com.vn/Uploads/Originals/2021/11/4/637716443491808351_5.jpg");
INSERT INTO image(product_id, main, url) VALUES(10, 0, "https://images.fpt.shop/unsafe/fit-in/960x640/filters:quality(90):fill(white):upscale()/fptshop.com.vn/Uploads/Originals/2021/11/4/637716443491808351_3.jpg");
INSERT INTO image(product_id, main, url) VALUES(11, 1, "https://images.fpt.shop/unsafe/fit-in/960x640/filters:quality(90):fill(white):upscale()/fptshop.com.vn/Uploads/Originals/2021/11/4/637716443491808351_5.jpg");
INSERT INTO image(product_id, main, url) VALUES(12, 0, "https://images.fpt.shop/unsafe/fit-in/960x640/filters:quality(90):fill(white):upscale()/fptshop.com.vn/Uploads/Originals/2021/11/4/637716443491808351_3.jpg");
INSERT INTO image(product_id, main, url) VALUES(13, 1, "https://images.fpt.shop/unsafe/fit-in/960x640/filters:quality(90):fill(white):upscale()/fptshop.com.vn/Uploads/Originals/2021/11/4/637716443491808351_5.jpg");
INSERT INTO image(product_id, main, url) VALUES(14, 0, "https://images.fpt.shop/unsafe/fit-in/960x640/filters:quality(90):fill(white):upscale()/fptshop.com.vn/Uploads/Originals/2021/11/4/637716443491808351_3.jpg");
INSERT INTO image(product_id, main, url) VALUES(15, 1, "https://images.fpt.shop/unsafe/fit-in/960x640/filters:quality(90):fill(white):upscale()/fptshop.com.vn/Uploads/Originals/2021/11/4/637716443491808351_5.jpg");
INSERT INTO image(product_id, main, url) VALUES(16, 0, "https://images.fpt.shop/unsafe/fit-in/960x640/filters:quality(90):fill(white):upscale()/fptshop.com.vn/Uploads/Originals/2021/11/4/637716443491808351_3.jpg");
INSERT INTO image(product_id, main, url) VALUES(17, 1, "https://images.fpt.shop/unsafe/fit-in/960x640/filters:quality(90):fill(white):upscale()/fptshop.com.vn/Uploads/Originals/2021/11/4/637716443491808351_5.jpg");
INSERT INTO image(product_id, main, url) VALUES(18, 0, "https://images.fpt.shop/unsafe/fit-in/960x640/filters:quality(90):fill(white):upscale()/fptshop.com.vn/Uploads/Originals/2021/11/4/637716443491808351_3.jpg");

INSERT INTO product(name, current_price, end_at, seller_id, subcategory_id) 
VALUES ('Iphone 13 Pro Max', 1500, CURRENT_TIMESTAMP, 3, 1);
INSERT INTO product(name, current_price, end_at, seller_id, subcategory_id) 
VALUES ('Iphone 12 Pro Max', 1400, CURRENT_TIMESTAMP, 3, 1);
INSERT INTO product(name, current_price, end_at, seller_id, subcategory_id) 
VALUES ('Iphone 11 Pro Max', 1300, CURRENT_TIMESTAMP, 3, 1);
INSERT INTO product(name, current_price, end_at, seller_id, subcategory_id) 
VALUES ('Iphone 13 Pro', 1420, CURRENT_TIMESTAMP, 3, 1);
INSERT INTO product(name, current_price, end_at, seller_id, subcategory_id) 
VALUES ('Iphone 12 Pro', 1320, CURRENT_TIMESTAMP, 3, 1);
INSERT INTO product(name, current_price, end_at, seller_id, subcategory_id) 
VALUES ('Iphone 13', 1310, CURRENT_TIMESTAMP, 3, 1);
INSERT INTO product(name, current_price, end_at, seller_id, subcategory_id) 
VALUES ('Iphone 12', 1200, CURRENT_TIMESTAMP, 4, 1);
SELECT * FROM product;

INSERT INTO product(name, current_price, end_at, seller_id, subcategory_id) 
VALUES ('Macbook Pro 2021', 1500, CURRENT_TIMESTAMP, 3, 2);
INSERT INTO product(name, current_price, end_at, seller_id, subcategory_id) 
VALUES ('Macbook Air 2021', 1400, CURRENT_TIMESTAMP, 3, 2);
INSERT INTO product(name, current_price, end_at, seller_id, subcategory_id) 
VALUES ('Macbook Pro 2020', 1300, CURRENT_TIMESTAMP, 3, 2);
INSERT INTO product(name, current_price, end_at, seller_id, subcategory_id) 
VALUES ('Macbook Air 2020', 1420, CURRENT_TIMESTAMP, 3, 2);
INSERT INTO product(name, current_price, end_at, seller_id, subcategory_id) 
VALUES ('Macbook Pro 2019', 1320, CURRENT_TIMESTAMP, 3, 2);
INSERT INTO product(name, current_price, end_at, seller_id, subcategory_id) 
VALUES ('Macbook Air 2019', 1310, CURRENT_TIMESTAMP, 3, 2);
INSERT INTO product(name, current_price, end_at, seller_id, subcategory_id) 
VALUES ('Macbook Pro 2018', 1200, CURRENT_TIMESTAMP, 4, 2);
SELECT * FROM product;

DELETE FROM user WHERE id = 2;

select * from user;
select * from product_bidder;
select * from category;
select * from subcategory;
select * from product;