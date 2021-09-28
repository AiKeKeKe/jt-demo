package com.aike.service;

import com.aike.vo.EasyUI_Image;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    EasyUI_Image fileUpload(MultipartFile uploadFile);
}
