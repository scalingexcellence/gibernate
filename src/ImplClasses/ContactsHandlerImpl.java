/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ImplClasses;

import HelperClasses.Contact;
import Interfaces.Cacheable;
import Interfaces.ContactsHandler;
//google imports
import com.google.gdata.client.contacts.*;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.FamilyName;
import com.google.gdata.data.extensions.FullName;
import com.google.gdata.data.extensions.GivenName;
import com.google.gdata.data.extensions.Name;
import com.google.gdata.util.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author kostas
 */
public class ContactsHandlerImpl implements ContactsHandler, Cacheable{
    private ContactsService myService;
    private ContactFeed resultFeed;
    private Contact[] contacts;
    private int cacheLimit, cacheExpire, numOfOperations;
    public ContactsHandlerImpl(String user, String password, int cachelimit) throws AuthenticationException{
        myService = new ContactsService("kostaskar-testContacts-1");
        myService.setUserCredentials(user, password);
        
        resetCacheLimit(cachelimit);
    }
    public Contact[] getContacts() {
        if(cacheExpired() || numOfOperations==0){
            try {
            URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");
            resultFeed = myService.getFeed(feedUrl, ContactFeed.class);
            } catch (IOException ex) {
                Logger.getLogger(ContactsHandlerImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ServiceException ex) {
                Logger.getLogger(ContactsHandlerImpl.class.getName()).log(Level.SEVERE, null, ex);
            } 
        
            contacts = new Contact[resultFeed.getEntries().size()];
            for(int i=0; i<contacts.length; i++){
                ContactEntry contact = resultFeed.getEntries().get(i);
                contacts[i] = new Contact(contact);
            }
            
        }
        return contacts;
        
    }
    

    public void addContact(Contact c, String[] groups) {
        try {
            // Create the entry to insert
            ContactEntry contact = new ContactEntry();
            Name name = new Name();
            final String NO_YOMI = null;
            name.setFullName(new FullName(c.getName(), NO_YOMI));
            name.setGivenName(new GivenName(c.getName(), NO_YOMI));
            name.setFamilyName(new FamilyName(c.getSurname(), NO_YOMI));
            contact.setName(name);
            

            Email primaryMail = new Email();
            primaryMail.setAddress(c.getEmail());
            
            primaryMail.setPrimary(true);
            contact.addEmailAddress(primaryMail);

            URL postUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");
            try {
                myService.insert(postUrl, contact);
            } catch (IOException ex) {
                Logger.getLogger(ContactsHandlerImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ServiceException ex) {
                Logger.getLogger(ContactsHandlerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(ContactsHandlerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void purgeCache() {
        contacts = null;
        resetCacheLimit(cacheLimit);
    }

    public void resetCacheLimit(int cacheLimit) {
        this.cacheLimit = cacheLimit;
        this.numOfOperations = 0;
    }

    public void increaseNumOfOperations() {
        numOfOperations++;
    }

    public boolean cacheExpired() {
        return cacheLimit==numOfOperations;
    }
    

}
