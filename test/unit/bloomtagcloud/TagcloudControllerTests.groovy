package bloomtagcloud

import grails.test.mixin.*
import org.junit.*

@TestFor(TagcloudController)
class TagcloudControllerTests {
    
    private geonames 
    void setUp() {
        this.geonames = controller.geonames
        def futureMock = [ get: { time, unit -> return 1 } ]        
        controller.geonames = [
            numZipCodesState: { place, closure=null ->
                if (closure) { closure(1) }
                futureMock 
            } 
        ] 

    }
    
    void tearDown() {
        controller.geonames = this.geonames
    }
    
    void testIndex() {
        controller.state_count = null
        controller.index()
        assert response.redirectedUrl == null
        controller.empty()
        assert controller.state_count != null
        controller.index()
        assert response.redirectedUrl == '/tagcloud/primed' 
    }

    void testEmpty() {
        controller.state_count = null
        controller.empty()
        assert view == '/tagcloud/primed'
        assert model.states.size() == controller.states.size()
        controller.empty()
        assert response.redirectedUrl == '/tagcloud/primed' 
    }

    void testPrimed() {
        controller.state_count = null
        controller.primed()
        assert response.redirectedUrl == '/tagcloud/index'
        controller.empty()
        controller.primed()
        assert view == '/tagcloud/primed'
        assert model.states.size() == controller.states.size()
    }

    void testClear() {
        controller.state_count = [:]
        controller.clear()
        assert controller.state_count == null
        assert response.redirectedUrl == '/tagcloud/index'
    }

    void testEmptyWithClientErrors() {
        def geonames = controller.geonames
        def futureMock = [ get: { time, unit -> throw new Exception('test error') } ]
        // mock client
        controller.geonames = [
            numZipCodesState: { place, closure=null ->
                futureMock 
            } 
        ] 

        controller.empty()
        controller.geonames = geonames
        
        assert view == '/tagcloud/index'
        assert model.error_msg == 'There was a problem retrieving data' 
    }
}
