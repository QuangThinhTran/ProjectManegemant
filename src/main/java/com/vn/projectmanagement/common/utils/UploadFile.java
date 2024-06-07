package com.vn.projectmanagement.common.utils;

import com.vn.projectmanagement.common.constants.ExceptionConstant;
import com.vn.projectmanagement.exceptions.ApiRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class UploadFile {
    @Value("${file.upload.directory}")
    private String pathDir;

    /**
     * Upload file
     *
     * @param multipartFile - File to upload
     * @return - Slug of file
     * @throws IOException - Exception when upload file
     */
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        // Kiểm tra file có rỗng không
        if (multipartFile.isEmpty()) {
            throw new ApiRequestException(ExceptionConstant.FILE_IS_EMPTY, HttpStatus.BAD_REQUEST);
        }

        // Lấy tên file và xóa ký tự đặc biệt trong tên file để tạo slug
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        // Validate file
        validateFile(fileName);

        // Tạo slug từ tên file
        String slug = Helper.setSlug(fileName);

        // Tạo file từ slug và đường dẫn
        // File.separator để tạo file trên cả windows và linux
        File file = new File(pathDir + File.separator + slug);
        // Lưu file vào đường dẫn vừa tạo
        multipartFile.transferTo(file);
        return slug;
    }

    /**
     * Validate file
     *
     * @param fileName - File name to validate
     */
    private void validateFile(String fileName) {
        List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png");
        if (!allowedExtensions.contains(fileName.substring(fileName.lastIndexOf(".")).toLowerCase())) {
            throw new ApiRequestException(ExceptionConstant.FILE_IS_INVALID, HttpStatus.BAD_REQUEST);
        }
    }
}
