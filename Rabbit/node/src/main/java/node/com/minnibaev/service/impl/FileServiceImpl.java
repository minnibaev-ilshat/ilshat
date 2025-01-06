package node.com.minnibaev.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import common.com.minnibaev.dao.AppDocumentDAO;
import common.com.minnibaev.dao.AppPhotoDAO;
import common.com.minnibaev.dao.BinaryContentDAO;
import common.com.minnibaev.entity.AppDocument;
import common.com.minnibaev.entity.AppPhoto;
import common.com.minnibaev.entity.BinaryContent;
import lombok.extern.log4j.Log4j2;
import node.com.minnibaev.exceptions.UploadFileException;
import node.com.minnibaev.service.FileService;

@Log4j2
@Service
public class FileServiceImpl implements FileService {

	@Value("${bot.token}")
	private String token;

	@Value("${service.file_info.uri}")
	private String fileInfoUri;

	@Value("${service.file_storage.uri}")
	private String fileStorageUri;

	private final AppDocumentDAO appDocumentDAO;

	private final AppPhotoDAO appPhotoDAO;

	private final BinaryContentDAO binaryContentDAO;

	public FileServiceImpl(AppDocumentDAO appDocumentDAO, BinaryContentDAO binaryContentDAO, AppPhotoDAO appPhotoDAO) {
		this.appDocumentDAO = appDocumentDAO;
		this.binaryContentDAO = binaryContentDAO;
		this.appPhotoDAO = appPhotoDAO;
	}

	@Override
	public AppDocument proccessDoc(Message externalMessage) {
		Document telegramDoc = externalMessage.getDocument();
		String fileID = telegramDoc.getFileId();
		ResponseEntity<String> responseEntity = getFilePath(fileID);
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			BinaryContent persistentBinaryContent = getPersistentBinaryContent(responseEntity);
			AppDocument transientAppDoc = buildTransientAppDoc(telegramDoc, persistentBinaryContent);
			return appDocumentDAO.save(transientAppDoc);
		} else {
			System.out.println("NODE: error: Bad response from telegram service");
			throw new UploadFileException("Bad response from telegram service: " + responseEntity);
		}

	}

	@Override
	public AppPhoto proccessPhoto(Message externalMessage) {
		// TODO add list of photos handling
		PhotoSize telegramPhoto = externalMessage.getPhoto().get(0);
		String fileID = telegramPhoto.getFileId();
		ResponseEntity<String> responseEntity = getFilePath(fileID);
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			BinaryContent persistentBinaryContent = getPersistentBinaryContent(responseEntity);
			AppPhoto transientAppPhoto = buildTransientAppPhoto(telegramPhoto, persistentBinaryContent);
			return appPhotoDAO.save(transientAppPhoto);
		} else {
			System.out.println("NODE: error: Bad response from telegram service");
			throw new UploadFileException("Bad response from telegram service: " + responseEntity);
		}
	}

	private BinaryContent getPersistentBinaryContent(ResponseEntity<String> responseEntity) {
		String filePath = getFilePath(responseEntity);
		byte[] fileInByte = downloadFile(filePath);
		BinaryContent transientBinaryContent = BinaryContent.builder().fileAsArrayOfBytes(fileInByte).build();
		return binaryContentDAO.save(transientBinaryContent);
	}

	private String getFilePath(ResponseEntity<String> responseEntity) {
		JSONObject jsonObject = new JSONObject(responseEntity.getBody());
		return String.valueOf(jsonObject.getJSONObject("result").getString("file_path"));
	}

	private AppDocument buildTransientAppDoc(Document telegramDoc, BinaryContent persistentBinaryContent) {
		return AppDocument.builder().telegramFileId(telegramDoc.getFileId()).docName(telegramDoc.getFileName())
				.binaryContent(persistentBinaryContent).mimeType(telegramDoc.getMimeType())
				.fileSize(telegramDoc.getFileSize()).build();
	}

	private AppPhoto buildTransientAppPhoto(PhotoSize telegramPhoto, BinaryContent persistentBinaryContent) {
		return AppPhoto.builder().telegramFileId(telegramPhoto.getFileId()).binaryContent(persistentBinaryContent)
				.fileSize(telegramPhoto.getFileSize()).build();
	}

	private byte[] downloadFile(String filePath) {
		String fullUri = fileStorageUri.replace("{bot.token}", token).replace("{filePath}", filePath);
		URL urlObj = null;
		try {
			urlObj = new URL(fullUri);
		} catch (MalformedURLException e) {
			throw new UploadFileException(e);
		}
		try (InputStream is = urlObj.openStream()) {
			return is.readAllBytes();
		} catch (IOException e) {
			throw new UploadFileException(urlObj.toExternalForm(), e);
		}

	}

	private ResponseEntity<String> getFilePath(String fileID) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> request = new HttpEntity<String>(headers);
		return restTemplate.exchange(fileInfoUri, HttpMethod.GET, request, String.class, token, fileID);
	}

}
