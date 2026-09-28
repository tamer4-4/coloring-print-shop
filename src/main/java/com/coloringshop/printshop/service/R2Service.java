package com.coloringshop.printshop.service;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class R2Service {

    private final S3Presigner presigner;

    @Value("${R2_BUCKET_NAME}")
    private String bucketName;

    @Value("${R2_ENDPOINT}")
    private String endpoint;

    public R2Service(S3Presigner presigner) {
        this.presigner = presigner;
    }

    /**
     * يولّد presigned URL لأي نوع ملف (صورة / PDF / أي حاجة)
     * @param folder "books/covers" أو "books/pdfs"
     * @param contentType "image/jpeg" أو "application/pdf"
     * @param extension "jpg" أو "pdf"
     */
    public Map<String, String> generatePresignedUploadUrl(String folder, String contentType, String extension) {
        String key = folder + "/" + UUID.randomUUID() + "." + extension;

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .putObjectRequest(objectRequest)
                .build();

        String uploadUrl = presigner.presignPutObject(presignRequest).url().toString();
        String finalUrl = endpoint + "/" + bucketName + "/" + key;

        return Map.of(
            "uploadUrl", uploadUrl,
            "finalUrl", finalUrl
        );
    }

    /**
     * لحذف ملف من R2 (لما تعدل كتاب)
     */
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) return;
        try {
            String prefix = endpoint + "/" + bucketName + "/";
            if (!fileUrl.startsWith(prefix)) return;
            String key = fileUrl.substring(prefix.length());
            // TODO: لو عايز تحذف فعلياً، استخدم s3Client.deleteObject
        } catch (Exception e) {
            System.err.println("⚠️ فشل حذف الملف: " + e.getMessage());
        }
    }
}