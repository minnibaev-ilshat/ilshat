package rest_service.com.minnibaev.service.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import common.com.minnibaev.dao.AppDocumentDAO;
import common.com.minnibaev.dao.AppPhotoDAO;
import common.com.minnibaev.entity.AppDocument;
import common.com.minnibaev.entity.AppPhoto;
import common.com.minnibaev.entity.BinaryContent;
import common.com.minnibaev.utils.CryptoTool;
import rest_service.com.minnibaev.service.FileService;

@Service
public class FileServiceImpl implements FileService {

	private final AppDocumentDAO appDocumentDAO;

	private final AppPhotoDAO appPhotoDAO;

	private final CryptoTool cryptoTool;

	public FileServiceImpl(AppDocumentDAO appDocumentDAO, AppPhotoDAO appPhotoDAO, CryptoTool cryptoTool) {
		this.appDocumentDAO = appDocumentDAO;
		this.appPhotoDAO = appPhotoDAO;
		this.cryptoTool = cryptoTool;
	}

	@Override
	public AppDocument getDocument(String hash) {
		var id = cryptoTool.idOf(hash);
		if (id == null)
			return null;
		return appDocumentDAO.findById(id).orElse(null);
	}

	@Override
	public AppPhoto getPhoto(String hash) {
		var id = cryptoTool.idOf(hash);
		if (id == null)
			return null;
		return appPhotoDAO.findById(id).orElse(null);
	}

	@Override
	public FileSystemResource getFileSystemResource(BinaryContent binaryContent) {
		try {
			File temp = File.createTempFile("tempFile", ".bin");
			temp.deleteOnExit();
			FileUtils.writeByteArrayToFile(temp, binaryContent.getFileAsArrayOfBytes());
			return new FileSystemResource(temp);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

}
