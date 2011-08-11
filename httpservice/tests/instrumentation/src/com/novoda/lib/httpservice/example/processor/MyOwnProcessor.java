
package com.novoda.lib.httpservice.example.processor;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;

import java.io.IOException;

public class MyOwnProcessor implements HttpProcessor {
    private String myvalue;

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
    }

    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException,
            IOException {
    }

    public void setMyvalue(String myvalue) {
        this.myvalue = myvalue;
    }

    public String getMyvalue() {
        return myvalue;
    }

}
