/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package HelperClasses;

/**
 *
 * @author kostas
 */
public class Size {
    private int rows, columns;

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
    
    public Size(){
        
    }
    
    public Size(int rows, int cols){
        this.rows = rows;
        this.columns = cols;
    }
}
