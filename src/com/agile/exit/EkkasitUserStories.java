package com.agile.exit;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

import com.agile.exit.data.FamilyData;
import com.agile.exit.data.GEDData;
import com.agile.exit.data.IndividualData;

public class EkkasitUserStories extends BaseUserStories{
	

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
		
		//This code should have some bugs when the family does have child.
		/*for( IndividualData individual : GEDData.getInstance().individuals){
			if( individual.familyAsSpouse != null && individual.dateOfDeath == null ){
				message += "	- "+individual.name + "\n";
			}
		}*/
		
		//
		ArrayList<IndividualData> allMarried = new ArrayList<IndividualData>();
		for( FamilyData family : GEDData.getInstance().families ){
			System.out.println(" __ "+family.marriageDate);
			if( family.marriageDate != null ){
				addIfNotExist( family.wife , allMarried);
			}
		}
		for( IndividualData individual : allMarried){
			message += "	- "+individual.name + "\n";
		}
		
		autoPrintIfSet(message);
		return message;
	}
	
	/**
	 * Sprint2
	 */
	public String getUs31(){
		String message = printHead(" US31 : List all living people over 30 who have never been married in a GEDCOM file ");
		
		ArrayList<IndividualData> allMarried = new ArrayList<IndividualData>();
		for( FamilyData family : GEDData.getInstance().families ){
			System.out.println(" __ "+family.marriageDate);
			if( family.marriageDate != null ){
				addIfNotExist( family.wife , allMarried);
			}
		}
		for( IndividualData individual : allMarried){
			//ChronoUnit.YEARS.between(individual.birth, new Date() );
			message += "	- "+individual.name + "\n";
		}
		
		
		autoPrintIfSet(message);
		return message;
	}
	
	
	
	private void addIfNotExist(IndividualData individualInsert, ArrayList<IndividualData> allMarried)
	{
		boolean isExist = false;
		for( IndividualData individual :  allMarried ){
			if( individual.id().equals( individualInsert.id() ) ){
				isExist = true;
				break;
			}
		}
		
		if( !isExist ){
			allMarried.add(individualInsert);
		}
	}
	
	
	
	
}
