/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package HelperClasses;

/**
 *
 * @author kostas
 */
public class Event {
    private String title, content;

    public Event() {
        
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public Event(String tit, String con){
        this.title = tit;
        this.content = con;
    }
}
