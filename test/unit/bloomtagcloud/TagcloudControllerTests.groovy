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
        assert model.states.keySet() == controller.states as Set
        controller.empty()
        assert response.redirectedUrl == '/tagcloud/primed' 
    }
}
