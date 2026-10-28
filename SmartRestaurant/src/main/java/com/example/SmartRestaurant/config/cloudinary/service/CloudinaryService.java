package com.example.SmartRestaurant.config.cloudinary.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

    String uploadProductImage(MultipartFile file, Long productId);

}
