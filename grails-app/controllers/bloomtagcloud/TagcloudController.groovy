package bloomtagcloud

import java.util.concurrent.TimeUnit
import net.antropoide.GeonamesClient
import groovy.transform.*

class TagcloudController {
    static geonames = new GeonamesClient('elventear')
    static states = ['AL':'Alabama','AK':'Alaska','AZ':'Arizona',
                     'AR':'Arkansas','CA':'California','CO':'Colorado',
                     'CT':'Connecticut','DE':'Delaware','FL':'Florida',
                     'GA':'Georgia','HI':'Hawaii', 'ID':'Idaho','IL':'Illinois',
                     'IN':'Indiana','IA':'Iowa','KS':'Kansas',
                     'KY':'Ketucky','LA':'Louisiana','ME':'Maine','MD':'Maryland',
                     'MA':'Massachusetts','MI':'Michigan','MN':'Minnesota',
                     'MS':'Mississippi', 'MO':'Missouri','MT':'Montana',
                     'NE':'Nebraska','NV':'Nevada','NH':'New Hampshire',
                     'NJ':'New Jersey','NM':'New Mexico','NY':'New York',
                     'NC':'North Carolina','ND':'North Dakota','OH':'Ohio',
                     'OK':'Oklahoma','OR':'Oregon', 'PA':'Pennsylvania',
                     'RI':'Rhode Island','SC':'South Carolina','SD':'South Dakota',
                     'TN':'Tennessee','TX':'Texas','UT':'Utah','VT':'Vermont',
                     'VA':'Virginia','WA':'Washington','WV':'West Virginia',
                     'WI':'Visconsin','WY':'Wyoming']
    static state_count = null

    @WithReadLock
    def index() {
        if (this.state_count != null) {
            return redirect(action: 'primed')
        }
    }

    @WithWriteLock
    def empty() {
        if (this.state_count != null) {
            return redirect(action: 'primed')
        }
        
        def state_count = new TreeMap()
        def retry_count = 0
        // Keep looping while we are under the max retries and state_count is 
        // missing some of the states
        while ((retry_count < 5) && (state_count.size() < this.states.size())) {
            if (retry_count > 0) { sleep 1000 }
            def futures = []

            // Loop over states that are not in state_count and retrieve them
            // using the Geonames API. When retrieved store them in state_count
            this.states.keySet().minus(state_count.keySet()).each { state -> 
                 def future = this.geonames.numZipCodesState(state) { cnt ->
                    state_count[state] = cnt
                    return cnt
                }
                futures.push(future)
            }

            // retrieve state count from thread, in case of error, skip the value
            try {
                futures.each{it.get(5, TimeUnit.SECONDS)}
            } catch (e) { }
            retry_count++
        }

        if (state_count.size() < this.states.size()) {
            this.state_count = null
            return render(view:'index', model:[error_msg:'There was a problem retrieving data'])
        }

        def max_cnt = state_count.values().max()
        def min_cnt = state_count.values().min()
        
        this.state_count = [states: state_count, max: max_cnt, min: min_cnt,
                        state_names: this.states]
        
        render(view: 'primed', model: this.state_count)
    }

    @WithReadLock
    def primed() {
        if (this.state_count == null) {
            return redirect(action: 'index')    
        }
        return this.state_count 
    }

    @WithWriteLock
    def clear() {
        this.state_count = null
        return redirect(action: 'index')    
    }
}

