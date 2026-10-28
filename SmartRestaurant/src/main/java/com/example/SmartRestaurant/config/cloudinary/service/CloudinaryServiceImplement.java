package com.example.SmartRestaurant.config.cloudinary.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CloudinaryServiceImplement implements CloudinaryService {
    Cloudinary cloudinary;

    @Override
    public String uploadProductImage(MultipartFile file, Long productId) {
        try {
            String publicId =
                    "smart-restaurant/product/product_" + productId;

            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "public_id", publicId,
                            "overwrite", true,
                            "invalidate", true,
                            "resource_type", "image"
                    )
            );
            return result.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException(
                    "Không thể tải ảnh sản phẩm lên Cloudinary",
                    e
            );
        }
    }
}
