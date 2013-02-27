package net.antropoide

@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.2' )
import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

class GeonamesError extends Exception {
    GeonamesError(message) {
        super(message)
    }
}

/** Client interface for the geonames.org API */
class GeonamesClient {
    static private base = 'http://api.geonames.org'
    private username, http
    /** Constructor
     * @param username      geonames.org user id as a String
     * */
    GeonamesClient(username) {
        this.username = username
        this.http = new AsyncHTTPBuilder(
            poolSize: 10,
            uri: this.base)
        this.http.handler.failure = { resp ->
            throw GeonamesError(resp.statusLine)
        }

    }

    /** Fetches asynchronously the number of Zip Codes within a state
     * @param place         Sate abbreviation as String
     * @param closure       Optional closure, to filter the results through
     * @return              Returns a Future, that will have the count when done
     * */
    def numZipCodesState (place, closure=null) {
        return this.http.get(path:'/postalCodeSearch', contentType: XML,
                        query: [placename:place, country:'US', style:'SHORT', 
                        maxRows:'1', username:this.username]) { resp, xml ->

                def totalResultsCount = xml.totalResultsCount.toString()

                if (totalResultsCount == "") {
                    throw new GeonamesError(xml.status.@message.toString()) 
                }

                totalResultsCount = totalResultsCount.asType(Integer)
                if (closure) {
                    totalResultsCount = closure(totalResultsCount)
                }
                return totalResultsCount
            }
    }

}

