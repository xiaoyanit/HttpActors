package com.novoda.lib.httpservice.exception;

public class FileNotFinished extends Exception {

	private static final long serialVersionUID = 3020427103587875876L;

	private String filename;
	private long fileLength;
	
	public FileNotFinished(String filename, long fileLength) {
		this.filename = filename;
		this.fileLength = fileLength;
	}
	
	public String getFilename() {
		return filename;
	}

	public long getFileLength() {
		return fileLength;
	}

}