package com.wru.onthi.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

@Controller
public class UploadImageController {

    public void uploadImage(MultipartFile multipartFile, String fileImage,
                            String folderUpload, String subfolder) throws IOException {
        String uploadDir = folderUpload +"/"+ subfolder;
        Path uploadpath= Paths.get(uploadDir);
        if(!Files.exists(uploadpath)){
            Files.createDirectories(uploadpath);
        }
        try(InputStream inputStream= multipartFile.getInputStream()) {
            Path filePath= uploadpath.resolve(fileImage);
            Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            e.printStackTrace();
            throw new IOException("Could not upload file"+ fileImage);
        }
    }

    public String getImageName(MultipartFile multipartFile){
        Date date= new Date();
        long time= date.getTime();
        String strName= String.valueOf(time);
        String flName = multipartFile.getOriginalFilename();
        String[] flPath = flName.split("[.]");
        String flExtension = flPath[flPath.length - 1];
        String imgname= strName +"." +flExtension;
        return imgname;
    }
}
