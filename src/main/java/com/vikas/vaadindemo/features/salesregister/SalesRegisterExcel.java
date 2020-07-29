package com.vikas.vaadindemo.features.salesregister;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.vikas.vaadindemo.entity.Invoice;

public class SalesRegisterExcel implements SalesRegister {

	private List<Invoice> invoices;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private static final List<Integer> dateColumns = Arrays.asList(2);

	public SalesRegisterExcel(List<Invoice> invoices) {
		this.invoices = invoices;
	}

	@Override
	public byte[] generate() throws IOException {
		this.workbook = new XSSFWorkbook();
		this.sheet = this.workbook.createSheet("Sales Register Excel");
		XSSFRow headerRow = this.sheet.createRow(0);

		// Set Header Values
		int col = -1;
		headerRow.createCell(++col).setCellValue("S.No");
		headerRow.createCell(++col).setCellValue("Invoice Number");
		headerRow.createCell(++col).setCellValue("Creation Date");
		headerRow.createCell(++col).setCellValue("Status");
		headerRow.createCell(++col).setCellValue("Seller");
		headerRow.createCell(++col).setCellValue("Buyer");
		headerRow.createCell(++col).setCellValue("Total Quantity");
		headerRow.createCell(++col).setCellValue("Total Amount");

		// Set Data Values
		int rowNum = 0;
		for (Invoice invoice : invoices) {
			col = -1;
			XSSFRow row = this.sheet.createRow(++rowNum);
			row.createCell(++col).setCellValue(rowNum);
			row.createCell(++col).setCellValue(invoice.getInvoiceNumber());
			row.createCell(++col).setCellValue(invoice.getCreationDate());
			row.createCell(++col).setCellValue(invoice.getStatusCode().toString());
			row.createCell(++col).setCellValue(invoice.getSeller().getAddress().getName());
			row.createCell(++col).setCellValue(invoice.getBuyer().getAddress().getName());
			row.createCell(++col).setCellValue(invoice.getTotalQuantity());
			row.createCell(++col).setCellValue(invoice.getTotalAmount());
		}

		setHeaderStyle();
		setDataStyle();
		autoSizeAllColumn();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		workbook.close();

		return bos.toByteArray();
	}

	private void setHeaderStyle() {
		XSSFRow row = this.sheet.getRow(0);
		for (Cell cell : row) {

			CellStyle cellStyle = this.workbook.createCellStyle();
			setBackgroundColorStyle(cellStyle, IndexedColors.GREY_25_PERCENT);
			setBorderStyle(cellStyle);
			setBoldStyle(cellStyle);

			cell.setCellStyle(cellStyle);
		}
	}

	private void setDataStyle() {
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			XSSFRow row = this.sheet.getRow(i);
			for (Cell cell : row) {

				CellStyle cellStyle = this.workbook.createCellStyle();
				setBackgroundColorStyle(cellStyle, IndexedColors.WHITE);
				setBorderStyle(cellStyle);
				if (dateColumns.contains(cell.getColumnIndex())) // check current cell is dateColumn
					setDateStyle(cellStyle);

				cell.setCellStyle(cellStyle);
			}
		}
	}

	// set all column width so that whole text is visible
	private void autoSizeAllColumn() {

		XSSFRow row = this.sheet.getRow(0);
		for (Cell cell : row) {
			sheet.autoSizeColumn(cell.getColumnIndex());
		}

	}

	private void setBoldStyle(CellStyle cellStyle) {
		XSSFFont font = this.workbook.createFont();
		cellStyle.setFont(font);
		font.setBold(true);
	}

	private void setDateStyle(CellStyle cellStyle) {
		CreationHelper createHelper = this.workbook.getCreationHelper();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy/mm/dd h:mm:ss"));
	}

	private void setBorderStyle(CellStyle cellStyle) {
		BorderStyle borderStyle = BorderStyle.THIN;
		cellStyle.setBorderTop(borderStyle);
		cellStyle.setBorderRight(borderStyle);
		cellStyle.setBorderBottom(borderStyle);
		cellStyle.setBorderLeft(borderStyle);
	}

	/*-private CellStyle setForegroundColorStyle(CellStyle cellStyle, IndexedColors color) {
		XSSFFont font = this.workbook.createFont();
		cellStyle.setFont(font);
		font.setColor(color.index);
		return cellStyle;
	}*/

	private CellStyle setBackgroundColorStyle(CellStyle cellStyle, IndexedColors color) {
		cellStyle.setFillForegroundColor(color.index);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		return cellStyle;
	}

}
