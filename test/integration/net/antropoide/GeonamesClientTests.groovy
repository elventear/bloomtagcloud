package net.antropoide

import net.antropoide.GeonamesClient.*
import static org.junit.Assert.*
import org.junit.*

class GeonamesClientTests {

    @Test
    void testnumZipCodesForPlace() {
        def client = new GeonamesClient('elventear')
        def numZipCodesForPlace = {
                def result = client.numZipCodesForPlace(it)
                while ( ! result.done ) {
                    Thread.sleep(500)
                }
                result.get()
            }

        // Zip codes for Minnesota according to Geonames
        assert numZipCodesForPlace('MN') == 1084
        // Unknwon place
        assert numZipCodesForPlace('ajsdakjdaklsdjaksdjklasd') == 0
    }
}
