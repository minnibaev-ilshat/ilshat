package node.com.minnibaev.exceptions;

import java.io.IOException;
import java.net.MalformedURLException;

public class UploadFileException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UploadFileException(String message) {
		super(message);
	}

	public UploadFileException(Throwable e) {
		// TODO Auto-generated constructor stub
	}

	public UploadFileException(String externalForm, Throwable e) {
		// TODO Auto-generated constructor stub
	}

}
