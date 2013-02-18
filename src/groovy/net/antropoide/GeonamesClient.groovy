package net.antropoide

@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.2' )
import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

class GeonamesClient {
    static base = 'http://api.geonames.org'
    private username, http
    GeonamesClient(username) {
        this.username = username
        this.http = new AsyncHTTPBuilder(
            poolSize: 10,
            uri: this.base)
    }

    def numZipCodesForPlace (place, country = 'US') {
        return this.http.get(path:'/postalCodeSearch', contentType: XML,
                        query: [placename:place, country:country, style:'SHORT', 
                        maxRows:'1', username:this.username]) { resp, xml ->
                xml.totalResultsCount.toString().asType(Integer)
            }
    }

}

