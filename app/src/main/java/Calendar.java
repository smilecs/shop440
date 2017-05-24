/**
 * Created by mmumene on 21/05/2017.
 */

import android.util.ArraySet;

import java.util.*;

public class Calendar {
    ArrayList<Event> scheduleEvent;
    public Calendar() {
        scheduleEvent = new ArrayList();
    }

    // Should allow multiple events to be scheduled over the same time window.
    public void schedule(Event event) {
        // IMPLEMENT ME
        scheduleEvent.add(event);
    }

    public List<ConflictedTimeWindow> findConflictedTimeWindow() {
        // IMPLEMENT ME
        List<ConflictedTimeWindow> conflictedTimeWindows = new ArrayList<>();
        for(int i=0; i < scheduleEvent.size(); i++){
            Event eventToCheck = scheduleEvent.get(i);
            Set<Integer> eventId = new HashSet<>();
            for(int k=0; k < scheduleEvent.size(); k++){
                if(eventToCheck.id != scheduleEvent.get(k).id){
                    if((eventToCheck.startDate.before(scheduleEvent.get(k).startDate) && eventToCheck.endDate.after(scheduleEvent.get(k).endDate)) ||  (eventToCheck.startDate.after(scheduleEvent.get(k).startDate) && eventToCheck.endDate.before(scheduleEvent.get(k).endDate))){
                        eventId.add(scheduleEvent.get(k).id);
                        ConflictedTimeWindow conflictedTimeWindow = new ConflictedTimeWindow(scheduleEvent.get(i).startDate, scheduleEvent.get(i).endDate, eventId);
                        conflictedTimeWindows.add(conflictedTimeWindow);
                        scheduleEvent.remove(k);
                    }
                }
            }
        }
        return null;
    }

    public static class ConflictedTimeWindow {
        private final Date startDate;
        private final Date endDate;
        private final Set<Integer> conflictedEventIds;

        public ConflictedTimeWindow(Date startDate, Date endDate, Set<Integer> conflictedEventIds) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.conflictedEventIds = conflictedEventIds;
        }

        public Date getStartDate() {
            return startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public Set<Integer> getConflictedEventIds() {
            return conflictedEventIds;
        }

        @Override
        public String toString() {
            return "ConflictedTimeWindow{" +
                  "startDate=" + startDate +
                  ", endDate=" + endDate +
                  ", conflictedEventIds=" + conflictedEventIds +
                  '}';
        }
    }

    public static class Event {
        private final int id;
        private final Date startDate;
        private final Date endDate;

        public Event(int id, Date startDate, Date endDate) {
            this.id = id;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        @Override
        public String toString() {
            return "Event{" +
                  "id=" + id +
                  ", startDate=" + startDate +
                  ", endDate=" + endDate +
                  '}';
        }
    }

    public static void main(String[] args) {
        Calendar calendar = new Calendar();

        calendar.schedule(new Event(1, new Date(114, 0, 1, 10, 0), new Date(114, 0, 1, 11, 0))); // 2014-01-01 10:00 - 11:00
        calendar.schedule(new Event(2, new Date(114, 0, 1, 11, 0), new Date(114, 0, 1, 12, 0))); // 2014-01-01 11:00 - 12:00
        calendar.schedule(new Event(3, new Date(114, 0, 1, 12, 0), new Date(114, 0, 1, 13, 0))); // 2014-01-01 12:00 - 13:00

        calendar.schedule(new Event(4, new Date(114, 0, 2, 10, 0), new Date(114, 0, 2, 11, 0))); // 2014-01-02 10:00 - 11:00
        calendar.schedule(new Event(5, new Date(114, 0, 2, 9, 30), new Date(114, 0, 2, 11, 30))); // 2014-01-02 09:30 - 11:30
        calendar.schedule(new Event(6, new Date(114, 0, 2, 8, 30), new Date(114, 0, 2, 12, 30))); // 2014-01-02 08:30 - 12:30

        calendar.schedule(new Event(7, new Date(114, 0, 3, 10, 0), new Date(114, 0, 3, 11, 0))); // 2014-01-03 10:00 - 11:00
        calendar.schedule(new Event(8, new Date(114, 0, 3, 9, 30), new Date(114, 0, 3, 10, 30))); // 2014-01-03 09:30 - 10:30
        calendar.schedule(new Event(9, new Date(114, 0, 3, 9, 45), new Date(114, 0, 3, 10, 45))); // 2014-01-03 09:45 - 10:45

        List<ConflictedTimeWindow> conflictedTimeWindows = calendar.findConflictedTimeWindow();
        System.out.println(conflictedTimeWindows);
        // should print something like
        // [ConflictedTimeWindow{startDate=Thu Jan 02 09:30:00 PST 2014, endDate=Thu Jan 02 10:00:00 PST 2014, conflictedEventIds=[5, 6]},
        //  ConflictedTimeWindow{startDate=Thu Jan 02 10:00:00 PST 2014, endDate=Thu Jan 02 11:00:00 PST 2014, conflictedEventIds=[4, 5, 6]},
        //  ConflictedTimeWindow{startDate=Thu Jan 02 11:00:00 PST 2014, endDate=Thu Jan 02 11:30:00 PST 2014, conflictedEventIds=[5, 6]},
        //  ConflictedTimeWindow{startDate=Fri Jan 03 09:45:00 PST 2014, endDate=Fri Jan 03 10:00:00 PST 2014, conflictedEventIds=[8, 9]},
        //  ConflictedTimeWindow{startDate=Fri Jan 03 10:00:00 PST 2014, endDate=Fri Jan 03 10:30:00 PST 2014, conflictedEventIds=[7, 8, 9]},
        //  ConflictedTimeWindow{startDate=Fri Jan 03 10:30:00 PST 2014, endDate=Fri Jan 03 10:45:00 PST 2014, conflictedEventIds=[7, 9]}]
    }

}
