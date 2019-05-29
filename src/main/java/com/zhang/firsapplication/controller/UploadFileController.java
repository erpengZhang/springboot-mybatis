package com.zhang.firsapplication.controller;

import com.zhang.firsapplication.fastfds.FastDFSClient;
import com.zhang.firsapplication.fastfds.FastDFSFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Administrator
 */
@Controller
public class UploadFileController {

    private static final Logger log = LoggerFactory.getLogger(UploadFileController.class);

    @PostMapping("/file_upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadResult";
        }
        try {
            String path = saveFile(file);
            redirectAttributes.addFlashAttribute("message", "Successfully uploaded '" + file.getOriginalFilename() + "'");
            redirectAttributes.addFlashAttribute("path", "file path url : " + path);
        } catch (Exception e) {
            log.error("upload file failed", e);
        }
        return "redirect:/success";
    }

    private String saveFile(MultipartFile multipartFile) throws IOException {
        String[] fileAbsolutePath = {};
        String fileName = multipartFile.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        byte[] file_buff = null;
        InputStream inputStream = multipartFile.getInputStream();
        if (inputStream != null) {
            int len1 = inputStream.available();
            file_buff = new byte[len1];
            inputStream.read(file_buff);
        }
        if (inputStream != null) {
            inputStream.close();
        }
        FastDFSFile file = new FastDFSFile(fileName, file_buff, ext);
        try {
            // upload to fastdfs
            fileAbsolutePath = FastDFSClient.upload(file);
        } catch (Exception e) {
            log.error("upload file Exception", e);
        }
        if (fileAbsolutePath == null) {
            log.error("upload file failed, please upload again");
        }
        String path = fileAbsolutePath[0] + "/" + fileAbsolutePath[1];
        return path;
    }
}
