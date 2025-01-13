package rest_service.com.minnibaev.service;


import common.com.minnibaev.entity.AppDocument;
import common.com.minnibaev.entity.AppPhoto;

public interface FileService {
	AppDocument getDocument(String id);

	AppPhoto getPhoto(String id);
}
