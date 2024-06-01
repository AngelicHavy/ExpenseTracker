package com.example.ExpenseTrackr.expenseTrackr.image;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/transactional/upload")
public class UploadController {

    private final String imagePath = "C:/Users/nadya/OneDrive/Desktop/univer/3 course/2 semester/mobile/an/image/";

    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            String filename = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
            Path filePath = Paths.get(imagePath).resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/image/")
                    .path(filename)
                    .toUriString();

            return ResponseEntity.ok(fileDownloadUri);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to upload image");
        }
    }
}
