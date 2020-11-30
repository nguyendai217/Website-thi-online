package com.wru.onthi.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileInputStream;
import java.io.IOException;

@Controller
public class ImageController {
    @Value("${folder.upload}")
    String folderUpload;

    @RequestMapping(value = "/image/{subfolder}/{filename}",method = RequestMethod.GET)
    public @ResponseBody
    byte[] getImage(@PathVariable("subfolder") String subfolder, @PathVariable("filename") String fileName) throws IOException {
        FileInputStream inputStream = new FileInputStream(this.folderUpload + "/" + subfolder + "/" + fileName);
        return IOUtils.toByteArray(inputStream);
    }
}
