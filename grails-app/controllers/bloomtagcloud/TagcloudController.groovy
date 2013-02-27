package bloomtagcloud

import java.util.concurrent.TimeUnit
import net.antropoide.GeonamesClient
import groovy.transform.*

/** State Tag Cloud Controller.
 *
 * The controller can have two states, primed and empty. When primed it has all 
 * the information necessary to display the state tag cloud, when empty the controller 
 * does not have the values necessary to display the tag cloud.
 */
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
    /** Global state that counts the Zip code count per state */
    static state_count = null

    /** Default action.
     * If the controller is empty, display it's view that prompts the user to 
     * initiate the priming. Otherwise redirect to primed to display the data
     */
    @WithReadLock
    def index() {
        if (this.state_count != null) {
            return redirect(action: 'primed')
        }
    }

    /** Empty action. 
     * Initiates the primiming and when done it will return the primed view 
     * directly; given the locking mechanism used in the class it will ensure that 
     * a user requesting to display the TagCloud will be able to view the results
     * of the requests, regardless of other concurrent requests to clear the data.
     *
     * If the priming action is unsuccsseful it will display the index view with 
     * and error message.
     */
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
            // After the first retry, sleep between retries
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
            // error
            this.state_count = null
            return render(view:'index', model:[error_msg:'There was a problem retrieving data'])
        }

        //success
        def max_cnt = state_count.values().max()
        def min_cnt = state_count.values().min()
       
        // store global data
        this.state_count = [states: state_count, max: max_cnt, min: min_cnt,
                        state_names: this.states]
        
        render(view: 'primed', model: this.state_count)
    }

    @WithReadLock
    /** Displays the global state data as a Tag Cloud
     */
    def primed() {
        if (this.state_count == null) {
            return redirect(action: 'index')    
        }
        return this.state_count 
    }

    /** Clears the global state data
     */
    @WithWriteLock
    def clear() {
        this.state_count = null
        return redirect(action: 'index')    
    }
}

