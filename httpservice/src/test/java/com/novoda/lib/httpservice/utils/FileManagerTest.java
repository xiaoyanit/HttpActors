package com.novoda.lib.httpservice.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.novoda.lib.httpservice.exception.FileNotFinished;

public class FileManagerTest {

	private static final String FILE_PATH = "filepath";
	private static final String ENCODING = "UTF-8";
	private static final String CONTENT = "1234567890A1234567890B1234567890";

	private FileReader reader;
	
	@Before
	public void setUp() {
		reader = new FileReader();
		clean();
	}
	
	@After
	public void after() {
		clean();
	}
	
	@Ignore
	@Test
	public void shouldSaveToFile() throws FileNotFinished {
		InputStream stream = getInputStream(CONTENT);

		reader.addToFile(FILE_PATH, stream);
		
		assertFile(CONTENT);
	}
	
	@Ignore
	@Test
	public void shouldThrowFileNotFinishedFile() {
		InputStream stream = getInputStream(CONTENT);
		
		reader.setBufferSize(4);
		reader.setThreshold(4);
		try {
			reader.addToFile(FILE_PATH, stream);
		} catch (FileNotFinished fnf) {
		}
		
		assertFile("1234");
	}
	
	@Ignore
	@Test
	public void shouldReadOneByte() {
		InputStream stream = getInputStream(CONTENT);
		reader.setBufferSize(1);
		reader.setThreshold(1);		
		try {
			reader.addToFile(FILE_PATH, stream);
		} catch (FileNotFinished fnf) {
		}
		assertFile("1");
	}
	
//	@Test
//	public void shouldReadAllTheFile() {
//		reader.setBufferSize(1);
//		reader.setThreshold(3);		
//		
//		boolean keepGoing = true;
//		int i = 0;
//		while (keepGoing) {
//			InputStream stream = getInputStream(CONTENT);
//			try {
//				reader.addToFile(FILE_PATH, stream);
//				keepGoing = false;
//			} catch (FileNotFinished fnf) {
//				keepGoing = true;
//			}
//		}
//		assertFile(CONTENT);
//	}
	
//	@Test
//	public void shouldSumUpStream() {
//		InputStream stream = getInputStream("12345");
//		reader.setBufferSize(5);
//		reader.setThreshold(5);
//		try {
//			reader.addToFile(FILE_PATH, stream);
//		} catch (FileNotFinished fnf) {
//		}
//		stream = getInputStream("67890");
//		try {
//			reader.addToFile(FILE_PATH, stream);
//		} catch (FileNotFinished fnf) {
//		}
//		assertFile("1234567890");	
//	}

	private void assertFile(String content) {
		File file = new File(FILE_PATH);
		Assert.assertTrue(file.exists());
		
		Assert.assertEquals(content, covertFileToString(file));
	}

	public InputStream getInputStream(String convert) {
		try {
			return (InputStream) new ByteArrayInputStream(convert.getBytes(ENCODING));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public String covertFileToString(File file) {
		try {
			return convertStreamToString(new FileInputStream(file));
		} catch(Exception e) {
			throw new RuntimeException("Problem reading stream");
		}
	}

	public String convertStreamToString(InputStream is) {
		try {
			if (is != null) {
				Writer writer = new StringWriter();
				char[] buffer = new char[1];
				try {
					Reader reader = new BufferedReader(new InputStreamReader(is, ENCODING));
					int n;
					while ((n = reader.read(buffer)) != -1) {
						writer.write(buffer, 0, n);
					}
				} finally {
					is.close();
				}
				return writer.toString();
			} else {
				return "";
			}
		} catch(Exception e) {
			throw new RuntimeException("Problem reading stream");
		}
	}
	
	private void clean() {
		File file = new File(FILE_PATH);
		file.delete();
		file = new File(FILE_PATH);
		Assert.assertFalse(file.exists());
	}

}
