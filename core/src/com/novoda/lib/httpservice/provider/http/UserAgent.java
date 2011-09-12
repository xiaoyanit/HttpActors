package com.novoda.lib.httpservice.provider.http;

import android.os.Build;

public class UserAgent {

	private static final String ANDROID = "Android";

	/* package */UserAgent with(String what) {
		return null;
	}

	/* package */String build() {
		return "this";
	}

	public static class Builder {

		StringBuilder builder;

		public Builder() {
			builder = new StringBuilder(ANDROID);
			builder.append('/').append(Build.VERSION.RELEASE).append(" (");
		}

		public Builder with(String what) {
			builder.append(what).append(',');
			return this;
		}

		public String build() {
			return builder.deleteCharAt(builder.length() - 1).append(')')
					.toString();
		}
	}
}
