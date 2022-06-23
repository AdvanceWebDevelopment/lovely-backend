package com.hcmus.lovelybackend.service.impl;

import com.hcmus.lovelybackend.entity.dao.Image;
import com.hcmus.lovelybackend.repository.ImageRepository;
import com.hcmus.lovelybackend.service.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService implements IImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public void saveImage(Image image) {
        imageRepository.save(image);
    }
}
