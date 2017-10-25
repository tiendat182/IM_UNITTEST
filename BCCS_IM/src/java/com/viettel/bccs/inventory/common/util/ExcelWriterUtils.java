/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.common.util;

/**
 *
 * @author qlmvt_duytx
 */

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * Class help to manipulate excel file
 *
 * @author ThuanNHT
 * @version 1.0
 * @since: 1.0
 */
public class ExcelWriterUtils {

    private Workbook workbook;
	public static final Logger logger = Logger.getLogger(ExcelWriterUtils.class);
    private FileOutputStream fileOut;

    /**
     * Method to create a workbook to work with excel
     *
     * @param filePathName ThuanNHT
     */
    public void createWorkBook(String filePathName) {
	if (filePathName.endsWith(".xls") || filePathName.endsWith(".XLS")) {
	    workbook = (Workbook) new HSSFWorkbook();
	} else if (filePathName.endsWith(".xlsx") || filePathName.endsWith(".XLSX")) {
	    workbook = new XSSFWorkbook();
	}
    }


	public void saveToFileExcel(String filePathName) {
		try {
			fileOut = new FileOutputStream(filePathName);
			workbook.write(fileOut);
		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			try {
				fileOut.close();
				workbook = null;
			} catch (IOException ex) {
				logger.error(ex);
			}
		}
	}

	/**
	 * method to create a sheet
	 *
	 * @param sheetName ThuanNHT
	 */
	public Sheet createSheet(String sheetName) {
		String temp = WorkbookUtil.createSafeSheetName(sheetName);
		return workbook.createSheet(temp);
	}

	/**
	 * method t create a row
	 *
	 * @param r
	 * @return ThuanNHT
	 */
	public Row createRow(Sheet sheet, int r) {
		Row row = sheet.createRow(r);
		return row;
	}

	/**
	 * method to create a cell with value
	 *
	 * @param cellValue ThuanNHT
	 */
	public Cell createCell(Row row, int column, String cellValue) {
		// Create a cell and put a value in it.
		Cell cell = row.createCell(column);
		cell.setCellValue(cellValue);
		return cell;
	}

	/**
	 * method to create a cell with value
	 *
	 * @param cellValue ThuanNHT
	 */
	public Cell createCell(Sheet sheet, int c, int r, String cellValue) {
		Row row = sheet.getRow(r);
		if (row == null) {
			row = sheet.createRow(r);
		}
		// Create a cell and put a value in it.
		Cell cell = row.createCell(c);
		cell.setCellValue(cellValue);
//	cell.setCellValue(cellValue);
		return cell;
	}

	/**
	 * method to create a cell with value with style
	 *
	 * @param cellValue ThuanNHT
	 */
	public Cell createCell(Sheet sheet, int c, int r, String cellValue, CellStyle style) {
		Row row = sheet.getRow(r);
		if (row == null) {
			row = sheet.createRow(r);
		}
		// Create a cell and put a value in it.
		Cell cell = row.createCell(c);
		cell.setCellValue(cellValue);
		cell.setCellStyle(style);
		return cell;
	}

	/**
	 * Method get primitive content Of cell
	 *
	 * @param sheet
	 * @param c
	 * @param r
	 * @return
	 */
	public static Object getCellContent(Sheet sheet, int c, int r) {
		Cell cell = getCellOfSheet(r, c, sheet);
		if (cell == null) {
			return "";
		}
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				return cell.getRichStringCellValue().getString();
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					return cell.getDateCellValue();
				} else {
					return cell.getNumericCellValue();
				}
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue();
			case Cell.CELL_TYPE_FORMULA:
				return cell.getCellFormula();
			default:
				return "";

		}
	}

	/**
	 * Method set sheet is selected when is opened
	 *
	 * @param posSheet
	 */
	public void setSheetSelected(int posSheet) {
		try {
			workbook.setActiveSheet(posSheet);
		} catch (IllegalArgumentException ex) {
			logger.error(ex.getMessage(), ex);
			workbook.setActiveSheet(0);
		}
	}

	/**
	 * method to merge cell
	 *
	 * @param sheet
	 * @param firstRow based 0
	 * @param lastRow based 0
	 * @param firstCol based 0
	 * @param lastCol based 0
	 */
	public static void mergeCells(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
		sheet.addMergedRegion(new CellRangeAddress(
				firstRow, //first row (0-based)
				lastRow, //last row  (0-based)
				firstCol, //first column (0-based)
				lastCol //last column  (0-based)
		));
	}

	/**
	 * method to fill color background for cell
	 *
	 * @param cell
	 * @param colors:BLACK, WHITE, RED, BRIGHT_GREEN, BLUE, YELLOW, PINK,
	 * TURQUOISE, DARK_RED, GREEN, DARK_BLUE, DARK_YELLOW, VIOLET, TEAL,
	 * GREY_25_PERCENT, GREY_50_PERCENT, CORNFLOWER_BLUE, MAROON, LEMON_CHIFFON,
	 * ORCHID, CORAL, ROYAL_BLUE, LIGHT_CORNFLOWER_BLUE, SKY_BLUE,
	 * LIGHT_TURQUOISE, LIGHT_GREEN, LIGHT_YELLOW, PALE_BLUE, ROSE, LAVENDER,
	 * TAN, LIGHT_BLUE, AQUA, LIME, GOLD, LIGHT_ORANGE, ORANGE, BLUE_GREY,
	 * GREY_40_PERCENT, DARK_TEAL, SEA_GREEN, DARK_GREEN, OLIVE_GREEN, BROWN,
	 * PLUM, INDIGO, GREY_80_PERCENT, AUTOMATIC;
	 */
	public void fillAndColorCell(Cell cell, IndexedColors colors) {
		CellStyle style = workbook.createCellStyle();
		style.setFillBackgroundColor(colors.getIndex());
		cell.setCellStyle(style);
	}
	// datpk  lay object tu Row

	public static Object getCellContentRow(int c, Row row) {
		Cell cell = getCellOfSheetRow(c, row);
		if (cell == null) {
			return "";
		}
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				return cell.getRichStringCellValue().getString();
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					return cell.getDateCellValue();
				} else {
					return cell.getNumericCellValue();
				}
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue();
			case Cell.CELL_TYPE_FORMULA:
				return cell.getCellFormula();
			default:
				return "";

		}
	}

	/**
	 * Method get text content Of cell
	 *
	 * @param sheet
	 * @param c
	 * @param r
	 * @return
	 */
	public static String getCellStrContent(Sheet sheet, int c, int r) {
		Cell cell = getCellOfSheet(r, c, sheet);
		if (cell == null) {
			return "";
		}
		String temp = getCellContent(sheet, c, r).toString().trim();
		if (temp.endsWith(".0")) {
			return temp.substring(0, temp.length() - 2);
		}
		return temp;
	}
	// datpk getStringconten tu Row

	public static String getCellStrContentRow(int c, Row row) {
		Cell cell = getCellOfSheetRow(c, row);
		if (cell == null) {
			return "";
		}
		String temp = getCellContentRow(c, row).toString().trim();
		if (temp.endsWith(".0")) {
			return temp.substring(0, temp.length() - 2);
		}
		return temp;
	}



	public Sheet getSheetAt(int pos) {
		return workbook.getSheetAt(pos);
	}

	public Sheet getSheet(String name) {
		return workbook.getSheet(name);
	}

	/**
	 * Method to read an excel file
	 *
	 * @param filePathName
	 * @return * ThuanNHT
	 */
	public Workbook readFileExcel(String filePathName) {
		InputStream inp = null;
		try {
			inp = new FileInputStream(filePathName);
			workbook = WorkbookFactory.create(inp);
		} catch (FileNotFoundException ex) {
			logger.error(ex);
		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			try {
				if (inp != null) {
					inp.close();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}
		}
		return workbook;
	}

	/**
	 *  * ThuanNHT
	 *
	 * @param r
	 * @param c
	 * @param sheet
	 * @return
	 */
	public static Cell getCellOfSheet(int r, int c, Sheet sheet) {
		try {
			Row row = sheet.getRow(r);
			if (row == null) {
				return null;
			}
			return row.getCell(c);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return null;
		}
	}

	/**
	 * set style for cell
	 *
	 * @param cell
	 * @param halign
	 * @param valign
	 * @param border
	 * @param borderColor
	 */
	public void setCellStyle(Cell cell, short halign, short valign, short border, short borderColor, int fontHeight) {
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) fontHeight);
		style.setAlignment(halign);
		style.setVerticalAlignment(valign);
		style.setBorderBottom(border);
		style.setBottomBorderColor(borderColor);
		style.setBorderLeft(border);
		style.setLeftBorderColor(borderColor);
		style.setBorderRight(border);
		style.setRightBorderColor(borderColor);
		style.setBorderTop(border);
		style.setTopBorderColor(borderColor);
		cell.setCellStyle(style);
	}

	/**
	 * Method to draw an image on excel file
	 *
	 * @param imgSrc
	 * @param sheet
	 * @param colCorner
	 * @param rowCorner
	 * @throws IOException
	 */
	public void drawImageOnSheet(String imgSrc, Sheet sheet, int colCorner, int rowCorner) throws IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(imgSrc);
			byte[] bytes = IOUtils.toByteArray(is);
			int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
			if (imgSrc.endsWith(".jpg") || imgSrc.endsWith(".JPG") || imgSrc.endsWith(".jpeg") || imgSrc.endsWith(".JPEG")) {
				pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
			} else if (imgSrc.endsWith(".png") || imgSrc.endsWith(".PNG")) {
				pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
			}

			CreationHelper helper = workbook.getCreationHelper();
			// Create the drawing patriarch.  This is the top level container for all shapes.
			Drawing drawing = sheet.createDrawingPatriarch();
			//add a picture shape
			ClientAnchor anchor = helper.createClientAnchor();
			//set top-left corner of the picture,
			//subsequent call of Picture#resize() will operate relative to it
			anchor.setCol1(colCorner);
			anchor.setRow1(rowCorner);
			Picture pict = drawing.createPicture(anchor, pictureIdx);

			//auto-size picture relative to its top-left corner
			pict.resize();
		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			if (is != null) {
				is.close();
			}
		}

	}

	public void setStandardCellStyle(Cell cell) {
		setCellStyle(cell, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, CellStyle.BORDER_THIN, IndexedColors.BLACK.getIndex(), 12);
	}

	// datpk: lay cell tu Row
	public static Cell getCellOfSheetRow(int c, Row row) {
		try {
			if (row == null) {
				return null;
			}
			return row.getCell(c);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return null;
		}
	}

	public static Boolean compareToLong(String str, Long t) {
		Boolean check = false;
		try {
			Double d = Double.valueOf(str);
			Long l = d.longValue();
			if (l.equals(t)) {
				check = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			check = false;
		}
		return check;
	}

	public static Boolean doubleIsLong(String str) {
		Boolean check = false;
		try {
			Double d = Double.valueOf(str);
			Long l = d.longValue();
			if (d.equals(Double.valueOf(l))) {
				check = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			check = false;
		}
		return check;
	}

	public static void main(String[] arg) {
		try {
//            long freeMem = r.freeMemory();
//            System.out.println("free memory before creating array: " + freeMem);
//
			ExcelWriterUtils ewu = new ExcelWriterUtils();
//            ewu.readFileExcel("C:\\DIEMLOM1111111111111111111.xls");
//            Sheet shet = ewu.getSheetAt(0);
//            String a = ExcelWriterUtils.getCellStrContent(shet, 8, 17);
//            ewu.createSheet("Toi la ai3");
//            for (int i = 0; i < 60000; i++) {
//                for (int j = 0; j < 4; j++) {
//                   String str = ExcelWriterUtils.getCellStrContent(ewu.getSheetAt(0), 2, 4);
//                   Double db = Double.parseDouble(str);
//                    ExcelWriterUtils.getCellStrContent(ewu.getSheetAt(0), 3, 4);
//                }
//
//            }
//            ewu.setSheetSelected(5);
//            mergeCells(shet, 1, 5, 1, 5);
//            ewu.setStandardCellStyle(getCellOfSheet(2, 2, shet));
//            ewu.setCellStyleBottomBorder(getCellOfSheet(2, 2, shet), CellStyle.BORDER_DOUBLE, IndexedColors.RED.getIndex());
//            ewu.setCellStyleBottomBorder(getCellOfSheet(2, 2, shet), CellStyle.BORDER_DOUBLE, IndexedColors.GREEN.getIndex());
//            String[] arr = new String[lst.size()];
//            arr = lst.toArray(arr);
//            ewu.createDropDownlistValidation(arr);
//            ewu.readFileExcel("C:\\a.xls");
//            System.out.println(ewu.getCellStrContent(ewu.getSheetAt(0), 1, 1) + "------------");
//            System.out.println(ewu.getCellContent(ewu.getSheetAt(0), 1, 1) + "------------");
//            ewu.setSheet(ewu.getWorkbook().getSheetAt(0));
//            ewu.createDropDownListValidateFromSpreadSheet("'Sheet2'!$B$4:$B$11", 8, 12, 4, 5, ewu.getWorkbook().getSheetAt(0));
//            Cell cel = getCellOfSheet(17, 5, sh1);
//            ewu.saveToFileExcel("C:\\a.xlsx");

//            freeMem = r.freeMemory();
//            System.out.println("free memory after creating array:  " + freeMem);
//            r.gc();
//            freeMem = r.freeMemory();
//            System.out.println("free memory after running gc():    " + freeMem);
//
//            ExcelWriterUtils ewu2 = new ExcelWriterUtils();
//            ewu2.readFileExcel("C:\\aaa.xls");
////            Sheet shet2 = ewu2.createSheet("Toi la ai3");
//            for (int i = 0; i < 60000; i++) {
//                for (int j = 0; j < 4; j++) {
//                    ewu2.createCell(ewu2.getSheetAt(1), j, i, "Hang thu " + i);
//                }
//
//            }

			Workbook wb = ewu.readFileExcel("C:\\ccc.xls");
			Sheet she = wb.getSheetAt(0);

			int i = she.addMergedRegion(new CellRangeAddress(0, 5, 0, 0));
//            she.getRow(1).getCell(0).setCellValue("test");
			ewu.saveToFileExcel("C:\\ccc.xls");
			System.out.println(i);
//            System.out.println(she.getLastRowNum());
//            ewu.readFileExcel("C:\\b.xlsx");
//            Sheet she1 = ewu.getSheetAt(0);
//            System.out.println(she1.getPhysicalNumberOfRows());
//            System.out.println(she1.getLastRowNum());

//            for (int i = 40000; i < 60000; i++) {
//                for (int j = 0; j < 10; j++) {
//                    ewu.createCell(ewu.getSheetAt(0), j, i, "Hang thu " + i);
//                }
//
//            }
//
////            String[] arr = new String[lst.size()];
////            arr = lst.toArray(arr);
////            ewu.createDropDownlistValidation(arr);
////            ewu.readFileExcel("C:\\a.xls");
////            System.out.println(ewu.getCellStrContent(ewu.getSheetAt(0), 1, 1) + "------------");
////            System.out.println(ewu.getCellContent(ewu.getSheetAt(0), 1, 1) + "------------");
////            ewu.setSheet(ewu.getWorkbook().getSheetAt(0));
////            ewu.createDropDownListValidateFromSpreadSheet("'Sheet2'!$B$4:$B$11", 8, 12, 4, 5, ewu.getWorkbook().getSheetAt(0));
////            Cell cel = getCellOfSheet(17, 5, sh1);
//            ewu2.saveToFileExcel("C:\\aaa.xls");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}

	public CellStyle createCellStyle(SXSSFWorkbook wbTempXLSX) {
		CellStyle style = wbTempXLSX.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		return style;
	}

	public void createCellNoStyle(Sheet sheet,  int colDate,int rowDate, String date){
		try {

			Row row = getOrCreateRow(sheet, rowDate);
			getOrCreateCellNoStyle(row, colDate, date);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

		}
	}

	private Row getOrCreateRow(Sheet sheet, int rowIndex) {
		Row row = sheet.getRow(rowIndex);

		if (row == null) {
			row = sheet.createRow(rowIndex);
		}

		return row;
	}

	private Cell getOrCreateCellNoStyle(Row row, int cellIndex,
										Object value) {
		Cell cell = row.getCell(cellIndex);

		if (cell == null) {
			cell = row.createCell(cellIndex);
		}

		if(value!=null) {
			cell.setCellValue(String.valueOf(value));
		}

		return cell;
	}
}