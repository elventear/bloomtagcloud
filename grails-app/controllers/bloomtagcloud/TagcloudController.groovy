package bloomtagcloud

import net.antropoide.GeonamesClient
import groovy.transform.*

class TagcloudController {
    static geonames = new GeonamesClient('elventear')
    static states = ['AL','AK','AZ','AR','CA','CO','CT','DE','FL','GA','HI',
        'ID','IL','IN','IA','KS','KY','LA','ME','MD','MA','MI','MN','MS',
        'MO','MT','NE','NV','NH','NJ','NM','NY','NC','ND','OH','OK','OR',
        'PA','RI','SC','SD','TN','TX','UT','VT','VA','WA','WV','WI','WY']
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
        this.states.each() { state -> 
             def future = this.geonames.numZipCodesState(state) { cnt ->
                state_count[state] = cnt
                cnt
            }
            futures.push(future)
        }
        futures.each(){it.get()}
        this.state_count = state_count
       
        def max_cnt = state_count.values().max()
        def min_cnt = state_count.values().min()
        
        render(view: 'primed', model:[states: state_count, max: max_cnt, min: min_cnt])
    }

    @WithReadLock
    def primed() {
        if (this.state_count == null) {
            return redirect(action: 'index')    
        }
        def max_cnt = state_count.values().max()
        def min_cnt = state_count.values().min()

        return [states: state_count, max: max_cnt, min: min_cnt]
    }

    @WithWriteLock
    def clear() {
        this.state_count = null
        return redirect(action: 'index')    
    }
}

