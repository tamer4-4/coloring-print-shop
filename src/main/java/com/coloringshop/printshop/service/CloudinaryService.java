package com.coloringshop.printshop.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.coloringshop.printshop.excption.FileTooLargeException;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    private static final long MAX_PDF_SIZE = 10 * 1024 * 1024;   // 10MB
    private static final long MAX_IMAGE_SIZE = 25 * 1024 * 1024; // 25MB

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    
    


    /**
     * رفع صورة غلاف
     */
    public String uploadCoverImage(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        
        
        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new FileTooLargeException(
                "❌ حجم الصورة كبير جداً: " +
                "الحد الأقصى المسموح هو 25M"
            );
        }
        
        try {
            byte[] bytesToUpload;
        

        if (file.getSize() > 200 * 1024) {  // أكبر من 200KB
            

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream())
                    .size(1200, 1600)
                    .outputQuality(0.8)
                    .outputFormat("jpg")
                    .toOutputStream(baos);

            byte[] compressed = baos.toByteArray();

            if (compressed.length < file.getSize()) {
                bytesToUpload = compressed;

            } else {
                bytesToUpload = file.getBytes();
            }
        } else {
            bytesToUpload = file.getBytes();
        }
        

            Map uploadResult = cloudinary.uploader().upload(
            		bytesToUpload,
                    ObjectUtils.asMap(
                            "folder", "printshop/covers",
                            "resource_type", "image"
                    )
            );            
       
            return uploadResult.get("secure_url").toString();

        
        } catch (IOException e) {
            throw new RuntimeException("❌ فشل رفع الصورة: " + e.getMessage(), e);
        }
        }
        

    /**
     * رفع ملف PDF
     */
    public String uploadPdf(MultipartFile file) {
    	  if (file == null || file.isEmpty()) return null;
    
    	    if (file.getSize() > MAX_PDF_SIZE) {
    	        throw new FileTooLargeException(
    	            "حجم الملف كبير جدا " 
    	        + "استخدم الموقع دا لضغط pdf ilovepdf.com"
    	        );
    	    }
    	  
          try {
              Map uploadResult = cloudinary.uploader().uploadLarge(
                      file.getInputStream(),
                      ObjectUtils.asMap(
                              "folder", "printshop/pdfs",
                              "resource_type", "raw"
//                              "chunk_size", 6000000   
                      )
              );

     

              return uploadResult.get("secure_url").toString();
          } catch (IOException e) {
              throw new RuntimeException("❌ فشل رفع الـ PDF: " + e.getMessage(), e);
          }
    }
    

 
}