/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ImplClasses;

import HelperClasses.Event;
import Interfaces.GEventsHandler;
import java.util.Date;
import com.google.gdata.client.calendar.*;
import com.google.gdata.data.*;
import com.google.gdata.data.calendar.*;
import com.google.gdata.data.extensions.*;
import com.google.gdata.util.*;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author kostas
 */
public class EventsHandlerImpl implements GEventsHandler {
    private String username, password, calendar;
    private long hourDur = 3600000;
    CalendarService myService;
    public EventsHandlerImpl(String username, String password, String calendar) throws AuthenticationException, MalformedURLException, IOException, ServiceException{
        myService = new CalendarService("exampleCo-exampleApp-1");
        this.username = "kkaravolas@gmail.com";
        this.password = "wsd12lrfs!@#";
        this.calendar = calendar;
        myService.setUserCredentials(this.username, this.password);
        
        
    }
    public void getCalendars() throws MalformedURLException{
        // Send the request and print the response
        URL feedUrl = new URL("https://www.google.com/calendar/feeds/default/allcalendars/full");
        CalendarFeed resultFeed = null;
        try {
            resultFeed = this.myService.getFeed(feedUrl, CalendarFeed.class);
        } catch (IOException ex) {
            Logger.getLogger(EventsHandlerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServiceException ex) {
            Logger.getLogger(EventsHandlerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Your calendars:");
        System.out.println();
        for (int i = 0; i < resultFeed.getEntries().size(); i++) {
          CalendarEntry entry = resultFeed.getEntries().get(i);
          System.out.println("\t" + entry.getTitle().getPlainText());
        }   
    }
    public Event[] getEvents(int year, int month, int day) {
        Event[] results = null;
        URL feedUrl = null;
        try {
            feedUrl = new URL("https://www.google.com/calendar/feeds/default/private/full");
        } catch (MalformedURLException ex) {
            Logger.getLogger(EventsHandlerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        Date dt = new Date(year-1900, month-1, day+1);
        CalendarQuery myQuery = new CalendarQuery(feedUrl);
        myQuery.setMinimumStartTime(new DateTime(dt));
       
        
        CalendarEventFeed  events = null;
        try {
            events = myService.query(myQuery, CalendarEventFeed.class);
        } catch (IOException ex) {
            Logger.getLogger(EventsHandlerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServiceException ex) {
            Logger.getLogger(EventsHandlerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        results = new Event[events.getEntries().size()];
        for (int i = 0; i < events.getEntries().size(); i++) {
          CalendarEventEntry entry = events.getEntries().get(i);
          results[i] = new Event();
          results[i].setTitle(entry.getTitle().getPlainText());
          
          //System.out.println("\t" + entry.getTitle().getPlainText() +" - "+ entry.getEdited().toUiString());
          
        }   
        
        return results;
    }

    public void setEvent(int year, int month, int day, int hours, int minutes, Event evnt, int duration) {
        URL postUrl = null;
        try {
            postUrl = new URL("https://www.google.com/calendar/feeds/"+username+"/private/full");
        } catch (MalformedURLException ex) {
            Logger.getLogger(EventsHandlerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        CalendarEventEntry myEntry = new CalendarEventEntry();
        DateTime dt = new DateTime(new Date(year-1900, month-1, day+1));
        myEntry.setTitle(new PlainTextConstruct(evnt.getTitle()));
        myEntry.setContent(new PlainTextConstruct(evnt.getContent()));

        When eventTimes = new When();
        eventTimes.setStartTime(dt);
        DateTime end = new DateTime(dt.getValue()+duration*hourDur);
        eventTimes.setEndTime(end);
        myEntry.addTime(eventTimes);
        try {
            // Send the request and receive the response:
            CalendarEventEntry insertedEntry = myService.insert(postUrl, myEntry);
        } catch (IOException ex) {
            Logger.getLogger(EventsHandlerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServiceException ex) {
            Logger.getLogger(EventsHandlerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    

}
