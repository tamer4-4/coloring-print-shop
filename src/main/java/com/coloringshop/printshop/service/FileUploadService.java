package com.coloringshop.printshop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${app.upload.pdf-dir}")
    private String pdfDirectory;

    @Value("${app.upload.cover-dir}")
    private String coverDirectory;


    public String uploadFile(MultipartFile file, String folder, String baseDir) throws IOException {
        
        if (file.isEmpty()) {
            throw new RuntimeException("الملف فارغ");
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        
        if (!isAllowedExtension(fileExtension, folder)) {
            throw new RuntimeException("نوع الملف غير مسموح به: " + fileExtension);
        }

        String uniqueFileName = generateUniqueFileName(originalFilename);
        
        Path path = Paths.get(baseDir, folder);
        
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        // 4. حفظ الملف
        Path fullPath = path.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), fullPath, StandardCopyOption.REPLACE_EXISTING);

    
        return "/uploads/" + folder + "/" + uniqueFileName;
    }

    /**
     * رفع صورة الغلاف
     */
    public String uploadCoverImage(MultipartFile file) throws IOException {
        return uploadFile(file, "covers", coverDirectory);
    }

    /**
     * رفع ملف PDF
     */
    public String uploadPdf(MultipartFile file) throws IOException {
        return uploadFile(file, "pdfs", pdfDirectory);
    }


    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".")).toLowerCase();
    }

    private boolean isAllowedExtension(String extension, String folder) {
        if ("covers".equals(folder)) {
            return extension.equals(".jpg") || extension.equals(".jpeg") || 
                   extension.equals(".png") || extension.equals(".webp");
        } else if ("pdfs".equals(folder)) {
            return extension.equals(".pdf");
        }
        return false;
    }

    private String generateUniqueFileName(String originalFilename) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFilename);
        
        String cleanName = originalFilename.substring(0, originalFilename.lastIndexOf("."))
                                          .replaceAll("[^a-zA-Z0-9]", "_");
                                          
        return timestamp + "_" + uuid + "_" + cleanName + extension;
    }
}
