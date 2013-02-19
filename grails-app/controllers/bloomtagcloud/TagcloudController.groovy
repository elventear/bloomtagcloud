package bloomtagcloud

import net.antropoide.GeonamesClient

class TagcloudController {
    static geonames = new GeonamesClient('elventear')
    static states = ['AL','AK','AZ','AR','CA','CO','CT','DE','FL','GA','HI',
        'ID','IL','IN','IA','KS','KY','LA','ME','MD','MA','MI','MN','MS',
        'MO','MT','NE','NV','NH','NJ','NM','NY','NC','ND','OH','OK','OR',
        'PA','RI','SC','SD','TN','TX','UT','VT','VA','WA','WV','WI','WY']
    static state_count = null

    def index() {
        if (this.state_count == null) {
            return redirect(action: 'empty')
        }
        return redirect(action: 'primed')
    }

    def empty() {
        def futures = []
        def state_count = [:]
        this.states.each() { state -> 
             def future = this.geonames.numZipCodesState(state) { cnt ->
                state_count[state] = cnt
                cnt
            }
            futures.push(future)
        }
        futures.each(){it.get()}

        return [states: state_count]
    }

    def primed() {
    }

    def clear() {
        this.state_count = null
        return redirect(action: 'index')    
    }
}

