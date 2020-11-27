package com.wru.onthi.controller.admin;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

@Controller
public class DownloadFileController {
    @GetMapping(value = "/download/{filename}")
    public void downfile(HttpServletResponse response, @PathVariable("filename") String filename) {
        try {
            File file = new File(
                    getClass().getClassLoader().getResource("template-excel/" + filename + ".xlsx").getFile());
            byte[] data = FileUtils.readFileToByteArray(file);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".xlsx");
            response.setContentLength(data.length);
            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }
}
