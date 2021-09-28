package com.aike.service;

import com.aike.vo.EasyUI_Image;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService {

    @Value("${image.localDirPath}")
    private String localDirPath;

    @Value("${image.urlDirPath}")
    private String urlDirPath;

    @Override
    public EasyUI_Image fileUpload(MultipartFile uploadFile) {
        EasyUI_Image image = new EasyUI_Image();
        String fileName = uploadFile.getOriginalFilename().toLowerCase();
        if (!fileName.matches("^.+\\.(jpg|png|gif)$")) {
            image.setError(1);
            return image;
        }
        try {
            BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();
            if (height == 0 || width == 0) {
                image.setError(1);
                return image;
            }
            image.setHeight(height).setWidth(width);
            String dateDirPath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            String realDirPath = localDirPath + dateDirPath;
            File dir = new File(realDirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            String realFileName = uuid + fileType;
            String realFilePath = realDirPath + "/" + realFileName;
            uploadFile.transferTo(new File(realFilePath));

            String realUrlPath = urlDirPath + dateDirPath + "/" + realFileName;
            image.setUrl(realUrlPath);
        } catch (Exception e) {
            e.printStackTrace();
            image.setError(1);
            return image;
        }
        return image;
    }
}
