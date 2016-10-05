package com.agile.exit;

import java.util.ArrayList;

import com.agile.exit.data.FamilyData;
import com.agile.exit.data.GEDData;
import com.agile.exit.data.IndividualData;

public class EkkasitUserStories {
	
	private Boolean isAutoPrint;

	public EkkasitUserStories(){
		this(true);
	}
	public EkkasitUserStories(Boolean isAutoPrint){
		this.isAutoPrint = isAutoPrint;
	}
	
	/**
	 * Sprint1
	 */
	public String getUs29(){
		String message = printHead(" US29 : List all deceased individuals in a GEDCOM file ");
		
		/*for( FamilyData family : GEDData.getInstance().families){
			System.out.println(family.marriageDate);
		}*/
		for( IndividualData individual : GEDData.getInstance().individuals){
			if( individual.dateOfDeath != null ){
				message += "	- "+individual.name + "\n";
			}
		}
		
		autoPrintIfSet(message);
		return message;
	}
	/**
	 * Sprint1
	 */
	public String getUs30(){
		String message = printHead(" US30 : List all living married people in a GEDCOM file ");
		
		for( IndividualData individual : GEDData.getInstance().individuals){
			if( individual.familyAsSpouse != null && individual.dateOfDeath == null ){
				message += "	- "+individual.name + "\n";
			}
		}
		
		/*ArrayList<IndividualData> allMarried = new ArrayList<IndividualData>();
		for( FamilyData family : GEDData.getInstance().families ){
			addIfNotExist( family.husband , allMarried);
			addIfNotExist( family.wife , allMarried);
		}
		*/
		autoPrintIfSet(message);
		return message;
	}
	
	/**
	 * Sprint2
	 */
	public String getUs31(){
		String message = printHead(" US31 : List all living people over 30 who have never been married in a GEDCOM file ");
		
		
		
		
		
		autoPrintIfSet(message);
		return message;
	}
	
	
	
	private void addIfNotExist(IndividualData individualInsert, ArrayList<IndividualData> allMarried)
	{
		boolean isExist = false;
		for( IndividualData individual :  allMarried ){
			if( individual.id().equals( individual.id() ) ){
				isExist = true;
				break;
			}
		}
		
		if( !isExist ){
			allMarried.add(individualInsert);
		}
	}
	
	
	
	/**
	 * Helper
	 */
	
	private void autoPrintIfSet(String message){
		if(isAutoPrint){
			System.out.println(message);
		}
	}
	
	private String printHead(String str ){
		int padding = 6;
		String message = getStarsLine( str.length() + padding*2)+"\n";
		message += getStarsLine(padding) + str + getStarsLine(padding)+"\n";
		message += getStarsLine(str.length() + padding*2) + "\n";
		return message;
	}
	
	private String getStarsLine(int length){
		String line = "";
		for( int i = 0; i <= length-1; i++ ){
			line += "*";
		}
		return line;
	}
}
