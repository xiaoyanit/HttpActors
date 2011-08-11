package com.novoda.lib.httpservice.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

//import org.junit.Assert;

import com.novoda.lib.httpservice.exception.FileNotFinished;

public class FileReader {

	private static final long THRESHOLD = 512l*1024l;
	private static final int DEFAULT_BUFFER_SIZE = 8*1024;
	
	private long threshold;
	private int bufferSize;
	
	public FileReader() {
		setThreshold(THRESHOLD);
		setBufferSize(DEFAULT_BUFFER_SIZE);
	}

	public void setThreshold(long threshold) {
		this.threshold = threshold;
	}
	
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}
	
	public long getThreshold() {
		return this.threshold;
	}
	
	public void addToFile(String filepath, InputStream is) throws FileNotFinished {
		try {
			File file = new File(filepath);
			FileOutputStream os = new FileOutputStream(file, true);
			byte[] buffer = new byte[bufferSize];
			long count = 0;
			int n = 0;
			while (-1 != (n = is.read(buffer))) {
				os.write(buffer, 0, n);
				count += n;
				if(count >= threshold) {
					is.close();
					os.flush();
					os.close();
					throw new FileNotFinished(file.getAbsolutePath(), new File(filepath).length());
				}
			}
			os.close();
			is.close();
		} catch (FileNotFoundException e) {
			if(Log.errorLoggingEnabled()) {
				Log.e("problem reading content", e);
			}
			throw new RuntimeException(e);
		} catch (IOException e) {
			if(Log.errorLoggingEnabled()) {
				Log.e("problem reading content", e);
			}
			throw new RuntimeException(e);
		}
	}
	
	public boolean exists(String filepath) {
		File file = new File(filepath);
		return file.exists();
	}
	
	public void delete(String filename) {
		File file = new File(filename);
		file.delete();
	}

}
