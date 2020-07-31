package com.example.wxtools.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.UUID;

@Controller
@RequestMapping("/wxtools")
public class ReverseImage {

    @ResponseBody
    @RequestMapping("/test")
    public String testRequest(@RequestParam("name") String name) {
        return "hello " + name + "!";
    }

    @ResponseBody
    @PostMapping(value = "/fileUpload", produces = MediaType.IMAGE_GIF_VALUE)
    public byte[] fileUpload(@RequestParam(value = "file") MultipartFile file, Model model, HttpServletRequest request) {
        if (file.isEmpty()) {
            System.out.println("文件为空空");
        }
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = "F:\\pic\\gif\\"; // 上传后的路径
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }


        filePath = "F:\\pic\\gif\\" + fileName;
        byte[] bytes = Picture.reverseImage(filePath, "F:\\pic\\gif\\outReverse.gif");

        return bytes;
    }

    @ResponseBody
    @RequestMapping(value = "/getImage", produces = MediaType.IMAGE_GIF_VALUE)
    public byte[] getImage() throws IOException {
        String tar = "F:\\pic\\gif\\outReverse.gif"; // 上传后的路径

        FileInputStream fileInputStream = new FileInputStream(new File(tar));
        byte[] bytes = new byte[fileInputStream.available()];
        fileInputStream.read(bytes, 0, fileInputStream.available());
        return bytes;
    }
}
