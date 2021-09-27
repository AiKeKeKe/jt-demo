package com.aike.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class FileController {

    private static final String IMAGE_URL = "D:/1-JT/images";

    @RequestMapping("/file")
    public String file(MultipartFile fileImage) throws IOException {
        String fileName = fileImage.getOriginalFilename();
        File dir = new File(IMAGE_URL);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(IMAGE_URL + fileName);
        fileImage.transferTo(file);
        return "redirect:/file.jsp";
    }
}
