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
    -- Điện Tử
    ('Laptop', 2),
    ('Smart Phone', 2),
    -- Phụ Kiện
    ('Đồng Hồ', 3),
    ('Thắt Lưng', 3),
    -- Nội Thất
    ('Giường', 4),
    ('Bàn Ghế', 4),
    -- Trang Sức
    ('Nhẫn', 5),
    ('Vòng Cổ', 5),
    -- Tranh Vẽ
    ('Kỹ Thuật Số', 6),
    ('Hiện Vật', 6),
    -- Phương Tiện Di Chuyển
    ('Xe Hơi', 7),
    ('Tàu Bè', 7);