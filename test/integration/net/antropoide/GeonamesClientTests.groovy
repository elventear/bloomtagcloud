package net.antropoide

import net.antropoide.GeonamesClient.*
import static org.junit.Assert.*
import org.junit.*

class GeonamesClientTests {

    @Test
    void testnumZipCodesForPlace() {
        def client = new GeonamesClient('elventear')
        def result = client.numZipCodesForPlace('MN')

        while ( ! result.done ) {
            Thread.sleep(500)
        }
        assert result.get() == 1084
    }
}
