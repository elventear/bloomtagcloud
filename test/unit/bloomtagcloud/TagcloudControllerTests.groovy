package bloomtagcloud

import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(TagcloudController)
class TagcloudControllerTests {

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
}
