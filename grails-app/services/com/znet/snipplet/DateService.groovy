package com.znet.snipplet

import org.joda.time.DateTime;

class DateService {

	def now() {
		return new Date();
	}

    def parseDate(String date) {
		return date ? DateTime.parse(date).toDate() : null;
    }
}
