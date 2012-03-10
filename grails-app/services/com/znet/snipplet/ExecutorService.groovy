package com.znet.snipplet

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class ExecutorService {

    def executor(int threads = 3) {
		java.util.concurrent.ExecutorService exec = Executors.newFixedThreadPool(threads);
		return new Executor(exec);
    }
	
}

class Executor {
	
	def errors = [];
	java.util.concurrent.ExecutorService executor;
	
	Executor(java.util.concurrent.ExecutorService executor) {
		this.executor = executor;
	}
	
	def enqueue(callback) {
		def callable = { -> 
			try { return callback(); } 
			catch (Exception e) {
				errors << e
				e.printStackTrace();
			}
		} as Callable
		executor.submit(callable);
	}
	
	def enqueueAll(list, callback) {
		list.each { item ->
			def callable = { -> 
				try { return callback(item); }
				catch (Exception e) {
					errors << e
					e.printStackTrace();
				}
			} as Callable
			executor.submit(callable);
		}
	}
	
	def join(boolean failIfErrors = true) {
		if (failIfErrors && errors) {
			throw new ExecutorException("error invoking tasks", errors)
		}
		
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.MINUTES);
		return errors;
	}
}

class ExecutorException extends Exception {
	def errors = []
	
	ExecutorException(def message, def errors) {
		super(message);
		this.errors = errors;
	}
	
	def getErrors() {
		return errors;
	}
}
