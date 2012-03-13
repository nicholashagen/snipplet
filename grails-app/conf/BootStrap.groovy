import com.espn.snipplet.jobs.GistLoaderJob;
import com.espn.snipplet.jobs.GistUpdaterJob;
import com.znet.snipplet.*;

class BootStrap {

    def init = { servletContext ->
		
		// set default timezone
		TimeZone.setDefault(TimeZone.getTimeZone('UTC'));
		
		// improve upon JSON functionality
		net.sf.json.JSONNull.metaClass.isNull = { true }
		net.sf.json.JSONNull.metaClass.asBoolean = { -> false }
		net.sf.json.JSONArray.metaClass.isNull = { false }
		net.sf.json.JSONFunction.metaClass.isNull = { false }
		net.sf.json.JSONObject.metaClass.isNull = { false }
		net.sf.json.JSON.metaClass.isNull = { false }

		// improve upon Collection functionality
		Collection.metaClass.asMap = { arg ->
			def result = [:]
			delegate.each {
				if (arg instanceof Closure) {
					def key = arg(it)
					result[key] = it
				}
				else {
					def key = it[arg]
					result[key] = it
				}
			}
			return result
		}
		
		// improve upon Date functionality
		Date.metaClass.getRelativeTime = {
			def result = 0, metric = "";
			def diff = System.currentTimeMillis() - delegate.time;
            if (diff < 1000) {
                return "just now";
            }
            else if (diff < 1000 * 60) {
				metric = "second"
                result = Math.floor(diff/1000.0);
            }
            else if (diff < 1000 * 60 * 60) {
				metric = "minute";
                result = Math.floor(diff/(1000.0 * 60.0))
            }
            else if (diff < 1000 * 60 * 60 * 24) {
				metric = "hour";
				result = Math.floor(diff/(1000.0 * 60.0 * 60))
            }
            else if (diff < 1000 * 60 * 60 * 24 * 60) {
				metric = "day";
                result = Math.floor(diff/(1000.0 * 60.0 * 60 * 24))
            }
			else {
				return "on ${delegate.format('MMMM d, yyyy')}";
			}
			
			def value = result as Integer
			return "${value} ${metric}${value != 1 ? 's' : ''} ago"
		}

		// setup initial status
		def status = Status.get(1);
		if (status == null) {
			new Status(id:1, lastPage:1, lastUpdated:new Date(), apiCalls:0, elapsedTime:0).save(flush:true);
		}

		// setup initial database values
		def language = Language.findByAbbreviation('general');
		if (language == null) {
			new Language(name:'General', abbreviation:'general').save(flush:true)
		}

		// trigger initial load
		// GistLoaderJob.triggerNow()
		GistUpdaterJob.triggerNow();
    }
	
    def destroy = {
    }
}
