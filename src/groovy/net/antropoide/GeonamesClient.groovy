package net.antropoide

@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.2' )
import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

class GeonamesClient {
    static base = 'http://api.geonames.org'
    private username
    GeonamesClient(username) {
        this.username = username;    
    }

    def numZipCodesForPlace (place, country = 'US') {
        def http = new HTTPBuilder(this.base)
        http.request(GET, XML) {
            uri.path = '/postalCodeSearch'
            uri.query = [placename:place, country:country, style:'SHORT', 
                maxRows:'1', username:this.username]
        
            response.success = { resp, json ->
                println resp.statusLine
            }

  
            response.failure = { resp ->
                println "Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
            } 
        }
    }

}
