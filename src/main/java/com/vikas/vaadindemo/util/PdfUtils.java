package com.vikas.vaadindemo.util;

import java.io.IOException;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.Style;

public class PdfUtils {

	public static Style fontStyle(String standardFonts, int fontSize) {
		PdfFont font;
		try {
			font = PdfFontFactory.createFont(standardFonts);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		Style style = new Style();
		style.setFont(font).setFontSize(fontSize);
		return style;
	}

	public static PdfFont font(String standardFonts) {
		PdfFont font;
		try {
			font = PdfFontFactory.createFont(standardFonts);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return font;
	}
}
