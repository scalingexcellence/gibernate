/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package HelperClasses;

import com.google.gdata.data.contacts.ContactEntry;

/**
 *
 * @author kostas
 */
public class Contact {
    private String name, surname, email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
   
    public Contact(ContactEntry contact) {
        if(contact.hasName())name = contact.getName().getGivenName().getValue();
        if(contact.hasName())surname = contact.getName().getFamilyName().getValue();
        if(contact.hasEmailAddresses())email = contact.getEmailAddresses().get(0).getAddress();
    }
    
    @Override
    public String toString(){
        return "Name: "+name+"\n"+"Surname: "+surname+"\n"+"Primary Email: "+email+"\n";
    }

}
