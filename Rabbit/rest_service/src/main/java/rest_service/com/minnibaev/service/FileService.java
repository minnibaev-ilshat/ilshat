package rest_service.com.minnibaev.service;

import org.springframework.core.io.FileSystemResource;

import common.com.minnibaev.entity.AppDocument;
import common.com.minnibaev.entity.AppPhoto;
import common.com.minnibaev.entity.BinaryContent;

public interface FileService {
	AppDocument getDocument(String id);

	AppPhoto getPhoto(String id);

	FileSystemResource getFileSystemResource(BinaryContent binaryContent);
}
