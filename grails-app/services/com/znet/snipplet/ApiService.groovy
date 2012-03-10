package com.znet.snipplet

import java.util.concurrent.atomic.AtomicInteger

import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

class ApiService {

	public static final String API_URL = 'https://api.github.com';
	
	public static final int RATE_LIMIT = 5000;
	public static final int RATE_LIMIT_THRESHOLD = 500;
	
	private static ThreadLocal<AtomicInteger> MONITOR = new ThreadLocal<AtomicInteger>() {
		protected AtomicInteger initialValue() {
			return new AtomicInteger(0);
		}
	};

	void startMonitoring() {
		MONITOR.set(new AtomicInteger(0));
	}
	
	int endMonitoring() {
		return MONITOR.get().get();
	}

	def invoke(URL url, Map query = [:]) {
		return invoke(url.toExternalForm(), query);
	}
	
    def invoke(String path, Map query = [:]) {
		if (path.startsWith(API_URL)) {
			path = path.substring(API_URL.length());
		}
		
		def result = null;
		int cnt = MONITOR.get().incrementAndGet();
		println "Calling API ${cnt}: ${API_URL} AT: ${path}"
		def http = new HTTPBuilder(API_URL)
		http.request( GET, JSON ) {
			uri.path = path
			uri.query = query
		
			response.success = { resp, json ->
				checkLimits(resp);
				result = json;
			}
			
			response.failure = { resp ->
				// TODO: WHAT DO WE DO HERE TO THROW ERROR?
				println "Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
			}
		}
		
		return result;
    }
	
	def invokeAll(URL url, int page, Map query = [:]) {
		return invokeAll(url.toExternalForm(), page, query);
	}
	
	def invokeAll(String path, int page, Map query = [:]) {
		query.page = page
		query.per_page = query.per_page ?: 25
		return invoke(path, query);
	}
	
	protected void checkLimits(def resp) {
		def header = resp.headers['X-RateLimit-Remaining'];
		if (header) {
			int limit = Integer.parseInt(header.value);
			println "RATE LIMIT: ${limit}"
			if (limit < RATE_LIMIT_THRESHOLD) {
				// TODO: mail and log error
			}
		}
	}
}
