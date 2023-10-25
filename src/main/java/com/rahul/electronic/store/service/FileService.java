package com.rahul.electronic.store.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	 String uploadImage(MultipartFile file,String path) throws IOException;
	 InputStream getResource(String path,String name) throws FileNotFoundException;
	 
	 InputStreamResource downloadImage(String path,String name) throws IOException;
}
