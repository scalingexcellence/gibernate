/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package googleappsproxy;

import ImplClasses.ContactsHandlerImpl;
import ImplClasses.EventsHandlerImpl;
import ImplClasses.SpreadSheetHandlerImpl;
import Interfaces.ContactsHandler;
import Interfaces.GEventsHandler;
import Interfaces.SpreadsheetHandler;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kostas
 */
public class GoogleAppsProxy implements Interfaces.GAppsProxy{
    String username, password;
    /**
     * @param args the command line arguments
     */
    
    public GoogleAppsProxy(String user, String password){
        this.username = user;
        this.password = password;
    }
    public ContactsHandler getContactsHandler(String cal, int cachelimit) {
        ContactsHandler ch = null;
        try {
            ch = new ContactsHandlerImpl(username, password, cachelimit);
        } catch (AuthenticationException ex) {
            Logger.getLogger(GoogleAppsProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ch;
    }

    public GEventsHandler getEventsHandler(String cal) {
        GEventsHandler eh = null;
        try {
            eh = new EventsHandlerImpl(username, password, cal);
        } catch (AuthenticationException ex) {
            Logger.getLogger(GoogleAppsProxy.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(GoogleAppsProxy.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GoogleAppsProxy.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServiceException ex) {
            Logger.getLogger(GoogleAppsProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return eh;
    }

    public SpreadsheetHandler getSpreadsheetHandler(String spreadsheet,
			String worksheet, int cachelimit) {
        SpreadsheetHandler sh = null;
        try {
            sh = new SpreadSheetHandlerImpl(username, password, spreadsheet, worksheet, cachelimit);
        } catch (IOException ex) {
            Logger.getLogger(GoogleAppsProxy.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServiceException ex) {
            Logger.getLogger(GoogleAppsProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return sh;
    }


    
}
