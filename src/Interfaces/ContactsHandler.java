/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Interfaces;

import HelperClasses.Contact;

/**
 *
 * @author kostas
 */
public interface ContactsHandler {
    public Contact[] getContacts();  
    public void addContact(Contact c, String[] groups); 
    public void purgeCache();
}
