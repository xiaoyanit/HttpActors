package com.novoda.httpmock.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.novoda.httpmock.server.jdo.JdoRequestDao;
import com.novoda.httpmock.shared.Request;

public class FrontController extends HttpServlet {

    private static final Logger logger = Logger.getLogger(FrontController.class.getName());

    private static final long serialVersionUID = 1L;
    
    private static final String ENCODING = "UTF-8";

    private JdoRequestDao dao = new JdoRequestDao();
    
	public interface ContentType {
	    
	    String plainText = "text/plain";
	    
	    String xml = "text/xml";
	    
	    String atom = "application/atom+xml;charset=utf-8";
	
	}
    
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	try {
            String url = ((HttpServletRequest)request).getRequestURL().toString();
            logger.severe("ulr id : " + url);
            Request r = getRequest(url, Request.GET);
            handleResponse(r, response);
        } catch (Exception e) {
            logger.severe("Problem during generation of the feed : " + e.getMessage());
            returnContent("error", response);
        }
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
            String url = ((HttpServletRequest)request).getRequestURL().toString();
            logger.severe("ulr id : " + url);
            Request r = getRequest(url, Request.POST);
            r.setBody(getBody(request));
            handleResponse(r, response);
        } catch (Exception e) {
            logger.severe("Problem during generation of the feed : " + e.getMessage());
            returnContent("error", response);
        }
	}
	
	private void handleResponse(Request r, HttpServletResponse response) throws IOException {
		if(r.isNotFound()) {
			logger.severe("sending not found error");
        	response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else if(r.isServerException()) {
        	logger.severe("sending internal server error");
        	response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else if(r.isStaticResource() || r.isExternalResource()) {
        	logger.severe("sending external resource");
        	returnContent(getSource(r.getResource()), response);
        } else {
        	logger.severe("sending static resource");
        	returnContent(r.getResource(), response);
        }
	}
    
    private String getBody(HttpServletRequest request) {
    	InputStream is = null;
    	try {
	    	is = request.getInputStream();
	    	if (is != null) {
		    	Writer writer = new StringWriter();
		    	char[] buffer = new char[1024];
		    		Reader reader = new BufferedReader(new InputStreamReader(is, ENCODING));
		    		int n;
		    		while ((n = reader.read(buffer)) != -1) {
		    			writer.write(buffer, 0, n);
		    		}
		    	return writer.toString();
	    	}
    	} catch(Throwable t) {
    		logger.severe(t.getMessage());
    	} finally {
    		if (is != null) {
    			try {
    				is.close();
    			}catch(Throwable t) {
    	    		logger.severe(t.getMessage());
    			}
    		}
    	}
    	return null;
	}

	private Request getRequest(String url, Integer method) {
    	logger.info("getResponse : " + url + " " + method);
    	
    	//TODO filter by method
    	Request r = dao.getByUrl(url);
    	if(r == null) {
    		r = new Request(url);
    		r.setMethod(method);
    		dao.save(r);
    	}
		return r;
	}

	protected void returnContent(String content, HttpServletResponse response) {
	    returnContent(content, response, ContentType.plainText);
	}
	
	protected void returnContent(String content, HttpServletResponse response, String contentType) {
        response.setContentType(contentType);
        response.setContentLength(content.length());
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            throw new RuntimeException("Problem sending back the result");
        }
        out.println(content);
        out.close();
        out.flush();
    }
	
	public static String getSource(String urlString) {
		try {
			URL url = new URL(urlString);
		    URLConnection con = url.openConnection();
		    BufferedReader dis;
		    StringBuffer source = new StringBuffer();
		    String line;
		    dis = new BufferedReader(new InputStreamReader(con.getInputStream(), ENCODING));
	        while ((line = dis.readLine()) != null) {
	        	source.append(line);
	        }
		    dis.close();
		    return source.toString(); 
		} catch(IOException e) {
			throw new RuntimeException("Problem with get source in the html client.", e);
		}
	}
}
