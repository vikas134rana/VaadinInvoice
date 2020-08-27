package com.vikas.vaadindemo.util;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.vikas.vaadindemo.entity.Buyer;
import com.vikas.vaadindemo.entity.Seller;

public class InvoiceUtils {

	public static Map<Integer, String> invoiceTypeMap() {
		Map<Integer, String> invoiceTypeMap = new LinkedHashMap<>();
		invoiceTypeMap.put(1, "Asset Transfer");
		invoiceTypeMap.put(2, "Other");
		invoiceTypeMap.put(3, "Cross");
		invoiceTypeMap.put(4, "Export");
		invoiceTypeMap.put(5, "Inward Delivery Challan");
		invoiceTypeMap.put(6, "Outward Delivery Challan");
		invoiceTypeMap.put(7, "General Waste");
		invoiceTypeMap.put(8, "E-Waste");
		return invoiceTypeMap;
	}

	public static int invoiceTypeInt(String invoiceTypeValue) {

		Map<Integer, String> invoiceTypeMap = invoiceTypeMap();

		for (Entry<Integer, String> entry : invoiceTypeMap.entrySet()) {
			if (entry.getValue().equals(invoiceTypeValue)) {
				return entry.getKey();
			}
		}
		return 0;
	}

	public static char invoiceTypeChar(int invoiceType) {
		char type;

		switch (invoiceType) {
		case 1:
			type = 'A';
			break;
		case 2:
			type = 'O';
			break;
		case 3:
			type = 'C';
			break;
		case 4:
			type = 'X';
			break;
		case 5:
			type = 'N';
			break;
		case 6:
			type = 'U';
			break;
		case 7:
			type = 'G';
			break;
		case 8:
			type = 'E';
			break;
		default:
			throw new IllegalArgumentException("Invoice Type value " + invoiceType + " is invalid");
		}

		return type;
	}

	public static String invoiceTypeValue(int invoiceType) {
		Map<Integer, String> invoiceTypeMap = invoiceTypeMap();
		String invoiceTypeValue = invoiceTypeMap.containsKey(invoiceType) ? invoiceTypeMap.get(invoiceType) : null;
		return invoiceTypeValue;
	}

	public static List<String> getItemUnits() {
		return Arrays.asList("PCS", "GMS", "KGS", "NOS", "MTR", "BOX", "ROL", "SET", "PAC", "OTH");
	}

	public static List<String> getCurrencies() {
		return Arrays.asList("INR", "EURO", "Dollar");
	}

	public static Map<Integer, String> getIgstTypeMap() {
		Map<Integer, String> igstTypeMap = new LinkedHashMap<>();
		igstTypeMap.put(1, "Supply meant for export with payment of IGST");
		igstTypeMap.put(2, "Supply meant for export under bond or letter of undertaking without payment of IGST");
		return igstTypeMap;
	}

	public static int getIgstType(String igstTypeValue) {
		Map<Integer, String> invoiceTypeMap = getIgstTypeMap();

		for (Entry<Integer, String> entry : invoiceTypeMap.entrySet()) {
			if (entry.getValue().equals(igstTypeValue)) {
				return entry.getKey();
			}
		}
		return 0;
	}

	public static boolean isIGST(Seller seller, Buyer buyer) {
		return seller.getAddress().getState().getId() != buyer.getAddress().getState().getId();
	}

}
