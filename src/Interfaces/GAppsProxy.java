/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Interfaces;

/**
 *
 * @author kostas
 */
public interface GAppsProxy {
    public ContactsHandler getContactsHandler(String cal, int cachelimit);
    public GEventsHandler getEventsHandler(String cal);
    public SpreadsheetHandler getSpreadsheetHandler(String spreadsheet,
			String worksheet, int cachelimit);
}
