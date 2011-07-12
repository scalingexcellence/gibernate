package ImplClasses;

import HelperClasses.Size;
import Interfaces.Cacheable;
import Interfaces.SpreadsheetHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.google.gdata.client.GoogleService.CaptchaRequiredException;
import com.google.gdata.client.spreadsheet.CellQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import java.util.logging.Level;
import java.util.logging.Logger; 

public class SpreadSheetHandlerImpl implements SpreadsheetHandler, Cacheable{

	private SpreadsheetService service;
	private SpreadsheetEntry spreadsheetEntry;
	private WorksheetEntry wss;
	private URL listFeedUrl;
	private URL cellFeedUrl;
        private int cacheLimit, cacheExpire, numOfOperations;
        private ListFeed listfeed;
        private CellFeed cellfeed;
        private SpreadsheetFeed spreadsheetfeed;
        private List<WorksheetEntry> worksheetfeed;
        
	public SpreadSheetHandlerImpl(String email, String password, String spreadsheet,
			String worksheet, int cachelimit) throws IOException, ServiceException {
		// authenticate
		service = new SpreadsheetService("lookfwd-test-v1");

		try {
			service.setUserCredentials(email, password);
		} catch (CaptchaRequiredException e) {
			System.out.println("Please visit " + e.getCaptchaUrl());
			System.out.print("Answer to the challenge? ");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			String answer = in.readLine();
			service.setUserCredentials(email, password, e.getCaptchaToken(),
					answer);
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}

		this.selectSpreadSheet(spreadsheet);
                this.selectWorkSheet(worksheet);
                
                resetCacheLimit(cacheLimit);
	}

        public void selectSpreadSheet(String spreadsheet) throws MalformedURLException, IOException, ServiceException{
            URL metafeedUrl = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
            spreadsheetfeed = service.getFeed(metafeedUrl,SpreadsheetFeed.class);
		List<SpreadsheetEntry> spreadsheets = spreadsheetfeed.getEntries();
		for (int i = 0; i < spreadsheets.size(); i++) {
			SpreadsheetEntry entry = spreadsheets.get(i);
			if (spreadsheet.equals(entry.getTitle().getPlainText())) {
				spreadsheetEntry = entry;
				break;
			}
		}
        }
        public void selectWorkSheet(String worksheet) throws IOException, ServiceException{
            worksheetfeed = spreadsheetEntry.getWorksheets();
				for (int j = 0; j < worksheetfeed.size(); j++) {
					wss = worksheetfeed.get(j);
					String title = wss.getTitle().getPlainText();
					if (worksheet.equals(title)) {
						listFeedUrl = wss.getListFeedUrl();
						cellFeedUrl = wss.getCellFeedUrl();
						break;
					}
				}
        }
	private void listAll() throws IOException, ServiceException {
		// using listfeed
		if(cacheExpired() || numOfOperations==0){
                    listfeed = service.getFeed(listFeedUrl, ListFeed.class);
                }
		for (ListEntry entry : listfeed.getEntries()) {
			System.out.println(entry.getTitle().getPlainText());
			for (String tag : entry.getCustomElements().getTags()) {
				System.out.println("  <gsx:" + tag + ">" + entry.getCustomElements().getValue(tag) + "</gsx:" + tag + ">");
			}
		}

		// using cellfeed
		if(cacheExpired() || numOfOperations==0){
                    cellfeed = service.getFeed(cellFeedUrl, CellFeed.class);
                }
		for (CellEntry cell : cellfeed.getEntries()) {
			String shortId = cell.getId().substring(cell.getId().lastIndexOf('/') + 1);
			System.out.println(" -- Cell(" + shortId + "/" + cell.getTitle().getPlainText() + ") formula(" + cell.getCell().getInputValue() + ") numeric(" + cell.getCell().getNumericValue() + ") value(" + cell.getCell().getValue() + ")");
		}
	}


	
    public String[] rowValues(String sheet, int row) {
        String[] results = null;
        CellQuery query = new CellQuery(cellFeedUrl);
        
        query.setMinimumRow(row);
        query.setMaximumRow(row);
        
        cellfeed = null;
        try {
            cellfeed = service.query(query, CellFeed.class);
        } catch (IOException ex) {
            Logger.getLogger(SpreadSheetHandlerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServiceException ex) {
            Logger.getLogger(SpreadSheetHandlerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        results = new String[cellfeed.getEntries().size()];
        for(int i=0; i<results.length; i++){
            CellEntry cell = cellfeed.getEntries().get(i);
            results[i] = cell.getPlainTextContent();
        }
        
        return results;
    }

    public Size getSize(String sheet) {
        return new Size(wss.getRowCount(), wss.getColCount());
    }

    public void setValue(int row, int col, String value)  throws IOException, ServiceException{
        if (row > wss.getRowCount() || col > wss.getColCount()) {
			if (row > wss.getRowCount()) {
				wss.setRowCount(row);
			}
			if (col > wss.getColCount()) {
				wss.setColCount(col);
			}
			wss.update();
		}
		CellEntry cell = new CellEntry(row, col, value);
		service.insert(cellFeedUrl, cell);
                
                increaseNumOfOperations();
    }

    public void purgeCache() {
        listfeed = null;
        cellfeed = null;
        spreadsheetfeed = null;
        worksheetfeed = null;
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
