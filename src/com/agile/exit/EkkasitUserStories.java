package com.agile.exit;

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
		
		autoPrintIfSet(message);
		return message;
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
