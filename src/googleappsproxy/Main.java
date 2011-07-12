/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package googleappsproxy;

import ImplClasses.ContactsHandlerImpl;
import Interfaces.GAppsProxy;
import Interfaces.GEventsHandler;
import Interfaces.SpreadsheetHandler;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kostas
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
                                                        GAppsProxy gp = new GoogleAppsProxy("[email]", "[password]");
        GEventsHandler eh = gp.getEventsHandler("");
        SpreadsheetHandler gs = null;
                                                                gs = gp.getSpreadsheetHandler("aei-tei", "ΜΑΘΗΜΑΤΑ", 100);
            
        BufferedWriter writer = null;
        /*try {
            writer = new BufferedWriter(new FileWriter(new File("courses.txt")));
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        System.out.println("SpreadSheet size: "+gs.getSize("ΜΑΘΗΜΑΤΑ").getRows()+" rows - " + gs.getSize("ΜΑΘΗΜΑΤΑ").getColumns()+" columns");
	for(int i=2; i<gs.getSize("ΜΑΘΗΜΑΤΑ").getRows(); i++){
            String uni_name = gs.rowValues("ΜΑΘΗΜΑΤΑ", i)[1];
            if(!uni_name.equalsIgnoreCase(""))
                System.out.println(uni_name);
                    /*try {
                        //writer.write(uni_name+"\n");
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }*/
                
        }
        /*try {
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        ContactsHandlerImpl ch = null;
        
}
    
}