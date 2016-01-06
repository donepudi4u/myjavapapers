package com.myjavapapers.examples.poi;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.hssf.record.cf.PatternFormatting;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.util.CellRangeAddress;

public class CreateCSVFile {
	
	public HSSFCellStyle hssfCellStyle = null;
	public HSSFCellStyle boldStyle = null;
	public HSSFCellStyle newStyle = null;
	public HSSFFont defaultFont = null;
	private HSSFWorkbook createWorkBook(){
		HSSFWorkbook workbook = new HSSFWorkbook();
		return workbook;
	}
	
	private HSSFSheet createWorkSheet(HSSFWorkbook workbook){
		HSSFSheet sheet = workbook.createSheet("TIH-Demurage");
		
		SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
		 
	    // Condition 1: Formula Is   =A2=A1   (White Font)
	    ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule("MOD(ROW(),2)");
	    org.apache.poi.ss.usermodel.PatternFormatting fill1 = rule1.createPatternFormatting();
	    fill1.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.index);
	    fill1.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
	 
	    CellRangeAddress[] regions = {
	            CellRangeAddress.valueOf("A1:C5")
	    };
	 
	    sheetCF.addConditionalFormatting(regions, rule1);
	 
	    sheet.createRow(0).createCell(1).setCellValue("Shade Alternating Rows");
	    sheet.createRow(1).createCell(1).setCellValue("Condition: Formula Is  =MOD(ROW(),2)   (Light Green Fill)");
		return sheet;
	}
	
	private HSSFRow createRow(HSSFSheet sheet , int rownum){
		HSSFRow newRow = sheet.createRow(rownum);
		if(rownum == 0){
			newRow.setRowStyle(newStyle);
		}
		return newRow;
		
	}
	
	private HSSFCell createCell(HSSFRow row ,Object value, int cellNum){
		HSSFCell cell = row.createCell(cellNum);
		//row.setRowStyle(newStyle);
		if(value instanceof String){
			cell.setCellValue((String)value);
		}
		if(value instanceof Integer){
			cell.setCellValue((Integer)value);
		}
		return cell;
	}
	
	private HSSFCell createHeader(HSSFRow row ,Object value, int cellNum){
		HSSFCell cell = row.createCell(cellNum);
		if(value instanceof String){
			cell.setCellValue((String)value);
			cell.setCellStyle(newStyle);
		}
		if(value instanceof Integer){
			cell.setCellValue((Integer)value);
		}
		return cell;
	}
	
	private void writeToFile(HSSFWorkbook workbook){
		try
        {
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(new File("xsl/Tih_Demurage.xls"));
            workbook.write(out);
            out.close();
            System.out.println("File written successfully on disk.");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}
	
	private Map<String, Object[]> getData() {
		//This data needs to be written (Object[])
        Map<String, Object[]> data = new TreeMap<String, Object[]>();
       // data.put("1", new Object[] {"ID", "NAME", "LASTNAME"});
        data.put("1", new Object[] {1, "Amit", "Shukla"});
        data.put("4", new Object[] {2, "Lokesh", "Gupta"});
        data.put("3", new Object[] {3, "John", "Adwards"});
        data.put("2", new Object[] {4, "Brian", "Schultz"});
        data.put("8", new Object[] {6, "Brian", "Schultz"});
        data.put("7", new Object[] {8, "Brian", "Schultz"});
        data.put("6", new Object[] {7, "Brian", "Schultz"});
        data.put("5", new Object[] {5, "Brian", "Schultz"});
        
        return data;
	}
	
	public static void main(String[] args) {
		CreateCSVFile csvFile = new CreateCSVFile();
		HSSFWorkbook workBook = csvFile.createWorkBook();
		HSSFSheet workSheet = csvFile.createWorkSheet(workBook);
		Map<String, Object[]> data = csvFile.getData();
		csvFile.createStyles(workBook,workSheet);
		populateDataToWorkSheet(csvFile, workSheet, data);
		csvFile.writeToFile(workBook);
	}


	private void createStyles(HSSFWorkbook workBook, HSSFSheet workSheet) {
		/*Font bold = workBook.createFont();
		  bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
		  bold.setFontHeightInPoints((short) 10);
		  bold.setColor(Font.COLOR_RED);*/
		  
		  HSSFFont hssfFont = workBook.createFont();
		  hssfFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		  hssfFont.setColor(Font.COLOR_RED);
		  
		  boldStyle = workBook.createCellStyle();
		  boldStyle.setBorderBottom(CellStyle.BORDER_THIN);
		  boldStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		  boldStyle.setFont(hssfFont);
		  boldStyle.setFillBackgroundColor(HSSFColor.BLUE.index);
		  
			    defaultFont= workBook.createFont();
			    defaultFont.setFontHeightInPoints((short)10);
			    defaultFont.setFontName("Arial");
			    defaultFont.setColor(IndexedColors.BLACK.getIndex());
			    defaultFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			    defaultFont.setItalic(true);
			    
			newStyle = workBook.createCellStyle();
			//newStyle.setFillBackgroundColor(IndexedColors.DARK_GREEN.getIndex());
		//	newStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			newStyle.setAlignment(CellStyle.ALIGN_CENTER);
			newStyle.setFont(defaultFont);
			    
			    
		  
		 /* hssfCellStyle = workBook.createCellStyle();
		  hssfCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		  hssfCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		  hssfCellStyle.setFont(bold);*/
		
	}


	private static void populateDataToWorkSheet(CreateCSVFile csvFile,
			HSSFSheet workSheet, Map<String, Object[]> data) {
		Set<String> keySet = data.keySet();
		HSSFRow row1 = csvFile.createRow(workSheet,0);
		Object[] objects1 = new String[] {"ID","NAME","LAST NAME","",""};
		int cellNum1 = 0;
		for (Object object : objects1) {
				csvFile.createHeader(row1, object,cellNum1++);
		}
		int rowNum = 1;
		for (String header : keySet) {
			HSSFRow row = csvFile.createRow(workSheet,rowNum++);
			Object[] objects = data.get(header);
			int cellNum = 0;
			for (Object object : objects) {
					csvFile.createCell(row, object,cellNum++);
			}
		}
	}

}
