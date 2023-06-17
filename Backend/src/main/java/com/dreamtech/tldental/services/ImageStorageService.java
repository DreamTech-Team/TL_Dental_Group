package com.dreamtech.tldental.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Service
public class ImageStorageService implements IStorageService {
    Cloudinary cloudinary;

    String cloudName = "646e61783174376d34";
    String apiKey = "393832343431313839353531313934";
    String apiSecret = "702d5a43534d356f63396e58614d534d574951596a5f5831615863";


    // constructor
    public ImageStorageService() throws DecoderException {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", new String(Hex.decodeHex(cloudName)),
                "api_key", new String(Hex.decodeHex(apiKey)),
                "api_secret", new String(Hex.decodeHex(apiSecret))));
    }
    private boolean isImageFile(MultipartFile file) {
        // Let install
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[] {"png", "jpg", "jpeg", "bmp"})
                .contains(fileExtension.trim().toLowerCase());
    }
    @Override
    public String storeFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }
            if (!isImageFile(file)) {
                throw new RuntimeException("You can only upload image file");
            }
            float fileSizeInMegabytes = file.getSize() / 1_000_000.0f;
            if (fileSizeInMegabytes > 5.0f) {
                throw new RuntimeException("File must be <= 5Mb");
            }

            Map c = this.cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));

            String imgPath = (String) c.get("secure_url");
            System.out.println(imgPath);

            return imgPath;
        }catch (IOException exception) {
            throw new RuntimeException("Failed to store file", exception);
        }
    }

//    @Override
//    public Stream<Path> loadAll() {
//        try {
//            //list all files in storageFolder
//            //How to fix this ?
//            return Files.walk(this.storageFolder, 1)
//                    .filter(path -> !path.equals(this.storageFolder) && !path.toString().contains("._"))
//                    .map(this.storageFolder::relativize);
//        }
//        catch (IOException e) {
//            throw new RuntimeException("Failed to load stored files", e);
//        }
//    }
//
//    @Override
//    public byte[] readFileContent(String fileName) {
//        try {
//            Path file = storageFolder.resolve(fileName);
//            Resource resource = new UrlResource(file.toUri());
//            if (resource.exists() || resource.isReadable()) {
//                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
//                return bytes;
//            }
//            else {
//                throw new RuntimeException(
//                        "Could not read file: " + fileName);
//            }
//        }
//        catch (IOException exception) {
//            throw new RuntimeException("Could not read file: " + fileName, exception);
//        }
//    }
//
//    @Override
//    public void deleteAllFiles() {
//
//    }
}
