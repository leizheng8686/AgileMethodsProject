package com.agile.exit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class BaseUserStories {
	protected Boolean isAutoPrint;
	
	
	
	
	public LocalDate getCurrentLocalDateTime(){
		LocalDateTime now = LocalDateTime.now();
		LocalDate localTodayDate = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth() );
		return localTodayDate;
	}
	
	public LocalDate getCurrentDateTimeByDate(Date date){
		Calendar calendarBirth = Calendar.getInstance();
		calendarBirth.setTime(date);
		
		LocalDate localIndividualDate = LocalDate.of( 
				calendarBirth.get(Calendar.YEAR), 
				calendarBirth.get(Calendar.MONTH)+1, 
				calendarBirth.get(Calendar.DAY_OF_MONTH) 
			);
		return localIndividualDate;
	}
	
	public LocalDate getThisYearAniversary(Date date){
		LocalDateTime now = LocalDateTime.now();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		LocalDate localIndividualDate = LocalDate.of( 
				now.getYear(), 
				calendar.get(Calendar.MONTH)+1, 
				calendar.get(Calendar.DAY_OF_MONTH)
			);
		return localIndividualDate;
	}
	
	
	
	/**
	 * Helper
	 */
	
	protected void autoPrintIfSet(String message){
		if(isAutoPrint){
			System.out.println(message);
		}
	}
	
	protected String printHead(String str ){
		int padding = 6;
		String message = getStarsLine( str.length() + padding*2)+"\n";
		message += getStarsLine(padding) + str + getStarsLine(padding)+"\n";
		message += getStarsLine(str.length() + padding*2) + "\n";
		return message;
	}
	
	protected String getStarsLine(int length){
		String line = "";
		for( int i = 0; i <= length-1; i++ ){
			line += "*";
		}
		return line;
	}
	
	protected void print(String string ){
		System.out.println(string);
	}
}
