package net.antropoide

import net.antropoide.GeonamesClient.*
import static org.junit.Assert.*
import org.junit.*

class GeonamesClientTests {

    @Test
    void testNumZipCodesState() {
        def client = new GeonamesClient('elventear')
        def numZipCodesState = { place, closure={it} -> 
                def result = client.numZipCodesState(place, closure)
                result.get()
            }

        // Zip codes for Minnesota according to Geonames
        assert numZipCodesState('MN') == 1084
        // Unknwon place
        assert numZipCodesState('ajsdakjdaklsdjaksdjklasd') == 0
        // Test with closure
        assert numZipCodesState('MN'){it-1} == 1083
    }
}
