package com.vikas.vaadindemo.features.invoicepdf;

public enum PdfCopy {

	SELLER("Seller Copy"), RECEPIENT("Recepient Copy"), TRANSPORT("Transport Copy"), EXTRA("Extra Copy");

	private String value;

	private PdfCopy(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}

}
