package rest_service.com.minnibaev.controller;

import javax.print.Doc;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import common.com.minnibaev.entity.AppDocument;
import common.com.minnibaev.entity.AppPhoto;
import common.com.minnibaev.entity.BinaryContent;
import rest_service.com.minnibaev.service.FileService;

@RequestMapping("/file")
@RestController
public class FileController {

	private final FileService fileService;

	public FileController(FileService fileService) {
		this.fileService = fileService;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/get-doc")
	public ResponseEntity<?> getDoc(@RequestParam("id") String id) {
		AppDocument doc = fileService.getDocument(id);
		if (doc == null)
			return ResponseEntity.badRequest().build();
		BinaryContent binaryContent = doc.getBinaryContent();
		FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);
		if (fileSystemResource == null)
			return ResponseEntity.internalServerError().build();
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(doc.getMimeType()))
				.header("Content-disposition", "attachment; filename=" + doc.getDocName()).body(fileSystemResource);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/get-photo")
	public ResponseEntity<?> getPhoto(@RequestParam("id") String id) {
		AppPhoto photo = fileService.getPhoto(id);
		if (photo == null)
			return ResponseEntity.badRequest().build();
		BinaryContent binaryContent = photo.getBinaryContent();
		FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);
		if (fileSystemResource == null)
			return ResponseEntity.internalServerError().build();
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).header("Content-disposition", "attachment")
				.body(fileSystemResource);
	}

}
