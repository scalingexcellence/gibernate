/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Interfaces;

import HelperClasses.Size;
import com.google.gdata.util.ServiceException;
import java.io.IOException;

/**
 *
 * @author kostas
 */
public interface SpreadsheetHandler {
    public String[] rowValues(String sheet, int row);
    public Size getSize(String sheet);
    public void setValue(int row, int col, String value)  throws IOException, ServiceException;
    public void purgeCache();
}
