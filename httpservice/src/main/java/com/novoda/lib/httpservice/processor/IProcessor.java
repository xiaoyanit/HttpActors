
package com.novoda.lib.httpservice.processor;

import com.novoda.lib.httpservice.lifecycle.IAndroidLifecycle;

import org.apache.http.protocol.HttpProcessor;

public interface IProcessor extends IAndroidLifecycle, HttpProcessor {
}
