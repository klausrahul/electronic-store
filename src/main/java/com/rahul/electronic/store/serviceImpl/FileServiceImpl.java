package com.rahul.electronic.store.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rahul.electronic.store.exception.BadApiRequestException;
import com.rahul.electronic.store.service.FileService;
@Service
public class FileServiceImpl implements FileService {

	private Logger log=LoggerFactory.getLogger(FileServiceImpl.class);

	@Override
	public String uploadImage(MultipartFile file, String path) throws IOException {
		String originalFileName = file.getOriginalFilename();
		log.info("Filename : {}", originalFileName);
		String filename = UUID.randomUUID().toString();
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		String filenameWithExtension = filename + extension;
		String fullpathWithFileName = path  + filenameWithExtension;

		if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg")
				|| extension.equalsIgnoreCase(".jpeg")) {
			// File Save
			File folder = new File(path);

			if (!folder.exists()) {
				folder.mkdirs();
			}
			// Upload
			Files.copy(file.getInputStream(), Paths.get(fullpathWithFileName));
			return filenameWithExtension;
		} else {
			throw new BadApiRequestException("File With this" + extension + " Not allowed");

		}
	}

	@Override
	public InputStream getResource(String path, String name) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String fullPath=path+name;
		
		InputStream inputStream=new FileInputStream(fullPath);
		return inputStream;
	}

	@Override
	public InputStreamResource downloadImage(String path, String name) throws IOException {
		String fullPathOfImage=path+name;
		log.info("Full Filename : {}", fullPathOfImage);
		
		InputStreamResource resource = new InputStreamResource(new FileInputStream(fullPathOfImage));
		
		
		log.info("Image InputStreamResource  : {}", resource);
		return resource;
	}

}
