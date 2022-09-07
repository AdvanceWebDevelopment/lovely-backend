use auction

IF 
    (
        SELECT COUNT(*) 
        FROM `user`
    ) = 0
THEN
    (
        source 1-insert-category-and-subcategory.sql;
        source 2-insert-100-users.sql;
        source 3-insert-100-users-images.sql;
        source 4-insert-art-and-digital-10-items.sql;
        source 4.1-insert-art-and-digital-30-desc.sql;
        source 4.2-insert-art-and-digital-50-images.sql;
        source 4.3-insert-art-and-digital-150-bids.sql;
        source 5-insert-car-and-boat-10-items.sql;
        source 5.1-insert-car-and-boat-30-desc.sql;
        source 5.2-insert-car-and-boat-50-images.sql;
        source 5.3-insert-car-and-boat-150-bids.sql;
        source 6-insert-watches-and-belts-10-items.sql;
        source 6.1-insert-watches-and-belts-20-desc.sql;
        source 6.2-insert-watches-and-belts-50-images.sql;
        source 6.3-insert-watches-and-belts-150-bids.sql;
    )
END IF