grails.servlet.version = "3.0"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve

    repositories {
        inherits true // Whether to inherit repository definitions from plugins
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()

        // uncomment these to enable remote dependency resolution from public Maven repositories
        //mavenCentral()
        //mavenLocal()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

		compile 'org.codehaus.groovy.modules.http-builder:http-builder:0.5.2'
        runtime 'mysql:mysql-connector-java:5.1.16'
    }

    plugins {
		build ":tomcat:$grailsVersion"
		
		runtime ":hibernate:$grailsVersion"

		compile ":cache-headers:1.1.5"
		
		runtime ":resources:1.1.5"
		runtime ":zipped-resources:1.0"
		runtime ":cached-resources:1.0"
		runtime ":yui-minify-resources:0.1.4"
		
		runtime ":jquery:1.7.1"
		compile ":jquery-ui:1.8.15"
		compile ":twitter-bootstrap:2.0.0.16"

		compile ":cloud-foundry:1.2.1"

		compile ":quartz:0.4.2"
		compile ":joda-time:1.3.1"
    }
}
