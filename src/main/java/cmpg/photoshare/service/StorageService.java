package cmpg.photoshare.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;


@Service
@Slf4j
public class StorageService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    private final String SUFFIX = "/";

    public String uploadFile(String path, MultipartFile file) {
        File fileObj = convertMultiPartFileToFile(file);
        s3Client.putObject(new PutObjectRequest(bucketName, path, fileObj));
        fileObj.delete();
        return "File uploaded : " + path;
    }

    public void createFolder(String folderName) {
        try {
            // Create metadata with content 0
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(0L);

            // Empty content
            InputStream inputStream = new ByteArrayInputStream(new byte[0]);

            // Creates a PutObjectRequest by passing the folder name with the suffix
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
                    folderName.toString() + SUFFIX, inputStream, metadata);

            //Send the request to s3 to create the folder
            s3Client.putObject(putObjectRequest);

        }catch (Exception e) {
            System.out.println(e);
        }

    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }

    public byte[] downloadFile(String path) {
        S3Object s3Object = s3Client.getObject(bucketName, path);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String deleteFile(String path) {
        s3Client.deleteObject(bucketName, path);
        return path + " removed ...";
    }
}