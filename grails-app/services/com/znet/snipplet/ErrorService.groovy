package com.znet.snipplet

class ErrorService {

    def saveError(String type, String source, Exception exception) {
		def baos = new ByteArrayOutputStream();
		exception.printStackTrace(new PrintStream(baos));
		baos.close();
		
		String msg = exception.message;
		if (msg.length() > 510) {
			msg = msg[0..510];
			println "LONG MESSAGE: ${exception.message}"
		}
		
		Error error = new Error(type:type, source:source, message:msg, stacktrace:baos.toString())
		error.save();
    }
}
