package com.example.jobhunter.controller;


import com.example.jobhunter.domain.DTO.Response.file.ResUploadFile;
import com.example.jobhunter.service.FileService;
import com.example.jobhunter.utils.annotation.APIMessage;
import com.example.jobhunter.utils.errors.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;

@RestController
@RequestMapping("${api.version}")
public class FileController {
    @Value("${upload-file.base-path}")
    private String basePath;

    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    @APIMessage("Upload file")
    public ResponseEntity<ResUploadFile> uploadFile(@RequestParam("file") MultipartFile file,
                                                    @RequestParam("folder") String folder) throws URISyntaxException, IOException, StorageException {
            if(file.isEmpty()) {
                throw new StorageException("File is empty. Please upload a file.");
            }
            fileService.createDirectory(basePath + folder);
            String uploadFile = fileService.store(file, folder);
            ResUploadFile resUploadFile = new ResUploadFile(uploadFile, Instant.now());
            return ResponseEntity.ok(resUploadFile);
    }
    
}
