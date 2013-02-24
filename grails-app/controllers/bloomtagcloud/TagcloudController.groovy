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
        def futures = []
        def state_count = new TreeMap()
        this.states.keySet().each() { state -> 
             def future = this.geonames.numZipCodesState(state) { cnt ->
                state_count[state] = cnt
                cnt
            }
            futures.push(future)
        }
        futures.each(){it.get(10, TimeUnit.SECONDS)}
        if (state_count.size() != this.states.size()) {
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

