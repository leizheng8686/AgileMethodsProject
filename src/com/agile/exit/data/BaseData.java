package com.agile.exit.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author exit
 *
 */
public class BaseData {
	private String id;
	protected String lastTagName = "";
	public BaseData(String id){
		this.id = id;
	}
	
	public String id(){
		return id;
	}

	/**
	 * override me
	 * @param tagName
	 * @param data
	 */
	public void addInfoByString(String tagName, String data) {
	}
	
	/**
	 * override me
	 */
	public void mapIdWithData(){
		
	}
	
	protected Date convertStringToDate(String dateString){
		DateFormat format = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
		Date date;
		try {
			date = format.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			date = null;
		}
		return date;
	}
}