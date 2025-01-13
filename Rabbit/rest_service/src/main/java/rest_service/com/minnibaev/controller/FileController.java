package rest_service.com.minnibaev.controller;

import java.io.IOException;

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
import jakarta.servlet.http.HttpServletResponse;
import rest_service.com.minnibaev.service.FileService;

@RequestMapping("/file")
@RestController
public class FileController {

	private final FileService fileService;

	public FileController(FileService fileService) {
		this.fileService = fileService;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/get-doc")
	public void getDoc(@RequestParam("id") String id, HttpServletResponse response) {
		AppDocument doc = fileService.getDocument(id);
		if (doc == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		response.setContentType(MediaType.parseMediaType(doc.getMimeType()).toString());
		response.setHeader("Content-disposition", "attachment; filename=" + doc.getDocName());
		response.setStatus(HttpServletResponse.SC_OK);
		BinaryContent binaryContent = doc.getBinaryContent();

		try {
			var out = response.getOutputStream();
			out.write(binaryContent.getFileAsArrayOfBytes());
			out.close();
		} catch (IOException e) {
			System.out.println("response error" + e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/get-photo")
	public void getPhoto(@RequestParam("id") String id, HttpServletResponse response) {
		AppPhoto photo = fileService.getPhoto(id);
		if (photo == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		response.setContentType(MediaType.IMAGE_JPEG.toString());
		response.setHeader("Content-disposition", "attachment;");
		response.setStatus(HttpServletResponse.SC_OK);
		BinaryContent binaryContent = photo.getBinaryContent();
		try {
			var out = response.getOutputStream();
			out.write(binaryContent.getFileAsArrayOfBytes());
			out.close();
		} catch (IOException e) {
			System.out.println("response error" + e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
