package com.viettel.bccs.inventory.common;

import com.google.common.collect.Lists;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.ErrorCode;
import org.apache.poi.ss.usermodel.*;
import org.primefaces.model.UploadedFile;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Created by ChungNV7 on 12/23/2015.
 */
public class ExcellUtil {

    public static final Logger logger = Logger.getLogger(ExcellUtil.class);

    private UploadedFile uploadedFile;
    private byte[] fileContent;
    private Workbook workbook;

    public ExcellUtil(UploadedFile uploadedFile, byte[] fileContent) throws LogicException, Exception {
        this.uploadedFile = uploadedFile;
        this.fileContent = fileContent;
        try {
            this.workbook = WorkbookFactory.create(new ByteArrayInputStream(this.fileContent));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "list.product.validate.fileError");
        }
    }

    /**
     * doc file excel, doc tu dong dau tien den het
     * @author ThanhNT77
     * @param indexSheet
     * @param beginRow
     * @param fromCol
     * @param toCol
     * @return
     * @throws Exception
     */
    public List<Object[]> readExcelFile(int indexSheet, int beginRow, int fromCol, int toCol) throws Exception {
        Sheet sheet = workbook.getSheetAt(indexSheet);
        int totalRow = getTotalRowAtSheet(sheet);
        List<Object[]> result = Lists.newArrayList();
        for (int i = beginRow; i < totalRow; i++) {
            Object[] obj = new Object[toCol - fromCol + 1];
            Row row = sheet.getRow(i);
            for (int j = fromCol; j <= toCol; j++) {
                obj[j] = getStringValue(row.getCell(j));
            }
            result.add(obj);
        }
        return result;
    }

    /**
     * doc file excel, chi dinh dong
     * @author ThanhNT77
     * @param indexSheet
     * @param beginRow
     * @param endRow
     * @param fromCol
     * @param toCol
     * @return
     * @throws Exception
     */
    public List readExcelFileFromTo(int indexSheet, int beginRow, int endRow, int fromCol, int toCol) throws Exception {
        Sheet sheet = workbook.getSheetAt(indexSheet);
        //int totalRow = getTotalRowAtSheet(sheet);
        List<Object[]> result = Lists.newArrayList();
        for (int i = beginRow; i < endRow; i++) {
            Object[] obj = new Object[toCol - fromCol + 1];
            Row row = sheet.getRow(i);
            for (int j = fromCol; j <= toCol; j++) {
                obj[j] = getStringValue(row.getCell(j));
            }
            result.add(obj);
        }
        return result;
    }

    public Sheet getSheetAt(int index) throws ExcelUtilException {
        if (workbook == null) {
            throw new ExcelUtilException("ExcelUtil is not construct.");
        }
        return workbook.getSheetAt(index);
    }

    public Sheet getSheetAt(String index) throws ExcelUtilException {
        if (workbook == null) {
            throw new ExcelUtilException("ExcelUtil is not construct.");
        }
        return workbook.getSheet(index);
    }

    public int getTotalSheet() throws ExcelUtilException {
        if (workbook == null) {
            throw new ExcelUtilException("ExcelUtil is not construct.");
        }
        return workbook.getActiveSheetIndex();
    }

    public int getTotalRowAtSheet(int sheetNumber) throws ExcelUtilException {
        if (workbook == null) {
            throw new ExcelUtilException("ExcelUtil is not construct.");
        }
        if (workbook.getSheetAt(sheetNumber) == null) {
            throw new ExcelUtilException("SheetAt: " + sheetNumber + " is null.");
        }
        return workbook.getSheetAt(sheetNumber).getPhysicalNumberOfRows();
    }

    public int getTotalRowAtSheet(Sheet sheet) throws ExcelUtilException {
        if (sheet == null) {
            throw new ExcelUtilException("Input Sheet is null.");
        }
        return sheet.getPhysicalNumberOfRows();
    }

    public int getTotalColumnAtRow(Row row) throws ExcelUtilException {
        if (row == null) {
            throw new ExcelUtilException("Row input is null.");
        }
        return row.getPhysicalNumberOfCells();
    }

    public String getStringValue(Cell cell) {
        try {
            if (cell == null) {
                return "";
            } else if (Cell.CELL_TYPE_BLANK == cell.getCellType()) {
                return "";
            } else if (Cell.CELL_TYPE_BOOLEAN == cell.getCellType()) {
                return cell.getBooleanCellValue() +"";
            } else if (Cell.CELL_TYPE_ERROR == cell.getCellType()) {
                return null;
            } else if (Cell.CELL_TYPE_FORMULA == cell.getCellType()) {
                return cell.getCellFormula();
            } else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                return String.format("%.0f", cell.getNumericCellValue());
            } else if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
                return cell.getStringCellValue();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return "";
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

}
