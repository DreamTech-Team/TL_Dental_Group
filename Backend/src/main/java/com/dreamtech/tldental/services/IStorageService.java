package com.dreamtech.tldental.services;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface IStorageService {
    public String storeFile(MultipartFile file);
    public boolean deleteFile(String url);
//    public Stream<Path> loadAll(); // load all files inside a folder
//    public byte[] readFileContent(String fileName);
//    public void deleteAllFiles();
}
