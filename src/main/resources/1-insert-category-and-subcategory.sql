set GLOBAL sql_mode =
        'ONLY_FULL_GROUP_BY,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

INSERT IGNORE INTO `category`(`name`, `id`)
VALUES ('Tất Cả', 1),
       ('Điện Tử', 2),
       ('Phụ Kiện', 3),
       ('Nội Thất', 4),
       ('Trang Sức', 5),
       ('Tranh Vẽ', 6),
       ('Phương Tiện Di Chuyển', 7);

INSERT IGNORE INTO `subcategory`(`id`,`name`, `category_id`)
VALUES
    -- Điện Tử
    (1,'Laptop', 2),
    (2,'Smart Phone', 2),
    -- Phụ Kiện
    (3,'Đồng Hồ', 3),
    (4,'Thắt Lưng', 3),
    -- Nội Thất
    (5,'Giường', 4),
    (6,'Bàn Ghế', 4),
    -- Trang Sức
    (7,'Nhẫn', 5),
    (8,'Vòng Cổ', 5),
    -- Tranh Vẽ
    (9,'Kỹ Thuật Số', 6),
    (10,'Hiện Vật', 6),
    -- Phương Tiện Di Chuyển
    (11,'Xe Hơi', 7),
    (12,'Tàu Bè', 7);