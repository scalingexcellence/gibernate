/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Interfaces;

import HelperClasses.Event; 
import java.net.MalformedURLException;

/**
 *
 * @author kostas
 */
public interface GEventsHandler {
    public Event[] getEvents(int year, int month, int day);
    public void setEvent(int year, int month, int day, int hours, int minutes, Event evnt, int duration);
    public void getCalendars() throws MalformedURLException;
}
