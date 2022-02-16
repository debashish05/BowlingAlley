import java.util.*;

public class LaneSubscriber extends Thread {

    private Vector subscribers;

    public LaneSubscriber() {
        subscribers = new Vector();
    }

    /** subscribe
     *
     * Method that will add a subscriber
     *
     * @param subscribe	Observer that is to be added
     */

    public void subscribe( LaneObserver adding ) {
        subscribers.add( adding );
    }

    /** publish
     *
     * Method that publishes an event to subscribers
     *
     * @param event	Event that is to be published
     */

    public void publish( LaneEvent event ) {
        if( subscribers.size() > 0 ) {
            Iterator eventIterator = subscribers.iterator();

            while ( eventIterator.hasNext() ) {
                ( (LaneObserver) eventIterator.next()).receiveLaneEvent( event );
            }
        }
    }
}