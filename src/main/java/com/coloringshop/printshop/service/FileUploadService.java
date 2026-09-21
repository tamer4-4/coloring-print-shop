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

    /**
     * رفع ملف وحفظه وإرجاع الرابط
     */
    public String uploadFile(MultipartFile file, String folder, String baseDir) throws IOException {
        if (file == null || file.isEmpty()) {
            return null; // بدل ما نرمي Exception، نرجع null لو مفيش ملف
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        
        if (!isAllowedExtension(fileExtension, folder)) {
            throw new RuntimeException("نوع الملف غير مسموح به: " + fileExtension);
        }

        String uniqueFileName = generateUniqueFileName(originalFilename);
        
        // ✅ الحفظ في الـ baseDir مباشرة (بما أنه يحتوي على اسم الفولدر النهائي)
        Path path = Paths.get(baseDir);
        
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        Path fullPath = path.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), fullPath, StandardCopyOption.REPLACE_EXISTING);

        // إرجاع الرابط للـ Frontend
        return "/uploads/" + folder + "/" + uniqueFileName;
    }

    public String uploadCoverImage(MultipartFile file) throws IOException {
        return uploadFile(file, "covers", coverDirectory);
    }

    public String uploadPdf(MultipartFile file) throws IOException {
        return uploadFile(file, "pdfs", pdfDirectory);
    }

    /**
     * حذف صورة غلاف قديمة
     */
    public void deleteCoverImage(String imageUrl) {
        // ✅ مبنمررش الـ folder هنا، بنمرر بس الـ baseDir
        deleteFile(imageUrl, coverDirectory); 
    }

    /**
     * حذف ملف PDF قديم
     */
    public void deletePdf(String pdfUrl) {
        // ✅ مبنمررش الـ folder هنا، بنمرر بس الـ baseDir
        deleteFile(pdfUrl, pdfDirectory);
    }

    /**
     * دالة مساعدة للحذف الفعلي من القرص
     */
    private void deleteFile(String fileUrl, String baseDir) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }

        try {
            // 1. استخراج اسم الملف من الرابط
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            
            // 2. ✅ بناء المسار الصحيح (baseDir + fileName فقط)
            Path filePath = Paths.get(baseDir, fileName);
            
            // 3. حذف الملف
            boolean deleted = Files.deleteIfExists(filePath);
            
            if (deleted) {
                System.out.println("✅ تم حذف الملف من القرص: " + fileName);
            } else {
                System.out.println("⚠️ الملف غير موجود في القرص: " + filePath.toString());
            }
            
        } catch (IOException e) {
            System.err.println("❌ فشل حذف الملف: " + e.getMessage());
        }
    }

    // ================= دوال مساعدة =================

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
        
        String cleanName = "";
        if (originalFilename.contains(".")) {
            cleanName = originalFilename.substring(0, originalFilename.lastIndexOf("."))
                                          .replaceAll("[^a-zA-Z0-9]", "_");
        } else {
            cleanName = originalFilename.replaceAll("[^a-zA-Z0-9]", "_");
        }
                                          
        return timestamp + "_" + uuid + "_" + cleanName + extension;
    }
}