package com.viettel.web.errorcode;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.LazyDataModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by thiendn1 on 20/4/2016.
 */

@Component
@ManagedBean
@Scope("view")
public class BusinessCodeController {
    private LazyDataModel<BusinessCode> lazyModel;
    private List<BusinessCode> filteredCodes;

    @PostConstruct
    public void init() {
        lazyModel = new BusinessCodeLazyModel(loadBusinessCode());
    }

    private static List<BusinessCode> loadBusinessCode() {
        try {
            List<BusinessCode> businessCodes = new ArrayList<>();
            InputStream file = BusinessCodeController.class.getClassLoader().getResourceAsStream("BusinessCode.xlsx");
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            //skip the first line
            rowIterator.next();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                BusinessCode businessCode = new BusinessCode();
                for (int i = 0; i < 3; i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        //Check the cell type and format accordingly
                        String value = convertCellToString(cell);
                        switch (i) {
                            case 0:
                                businessCode.setId(value);
                                break;
                            case 1:
                                businessCode.setDescription(value);
                                break;
                            case 2:
                                businessCode.setAction(value);
                                break;
                        }

                    }

                }
                businessCodes.add(businessCode);
            }
            file.close();
            return businessCodes;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<BusinessCode> getFilteredCodes() {
        return filteredCodes;
    }

    public void setFilteredCodes(List<BusinessCode> filteredCodes) {
        this.filteredCodes = filteredCodes;
    }

    private static String convertCellToString(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
        }
        return null;
    }

    public LazyDataModel<BusinessCode> getLazyModel() {
        return lazyModel;
    }

    public static void main(String[] args) {
        loadBusinessCode();
    }
}
