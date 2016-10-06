package com.agile.exit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;

import com.agile.exit.data.DataError;
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
			if( family.marriageDate != null ){
				addIfNotExist( family.wife , allMarried);
				addIfNotExist( family.husband , allMarried);
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
		
		ArrayList<IndividualData> individualNotMarriedOver30 = new ArrayList<IndividualData>();
		
		for( IndividualData allIndividual : GEDData.getInstance().individuals){
			boolean isMarry = false;
			//check married
			for( FamilyData family : GEDData.getInstance().families ){
				if( family.wife.id() == allIndividual.id() || family.husband.id() == allIndividual.id() ){
					isMarry = true;
					break;
				}
			}
			
			//check over 30
			if( !isMarry ){
				Calendar calendarBirth = Calendar.getInstance();
				calendarBirth.setTime(allIndividual.birth);
				
				LocalDateTime now = LocalDateTime.now();
				LocalDate localTodayDate = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth() );
				LocalDate localIndividualDate = LocalDate.of( 
						calendarBirth.get(Calendar.YEAR), 
						calendarBirth.get(Calendar.MONTH)+1, 
						calendarBirth.get(Calendar.DAY_OF_MONTH) 
					);
				
				long year = ChronoUnit.YEARS.between(localIndividualDate, localTodayDate );
				if( year >= 30 ){
					addIfNotExist(allIndividual,individualNotMarriedOver30);
				}
			}
		}
		
		for( IndividualData individual : individualNotMarriedOver30){
			message += "	- "+individual.name + "\n";
		}
		
		autoPrintIfSet(message);
		return message;
	}
	
	public String getUs32(){
		String message = printHead(" US32	List all multiple births in a GEDCOM file ");
		for( IndividualData individual : GEDData.getInstance().individuals){
			for( String error : individual.errors ){
				if( error.equals( DataError.ERROR_MULTIPLE_BIRTH ) ){
					message += "ERROR: INDIVIDUAL: US32: "+individual.id()+": has multiple births. \n";
					break;
				}
			}
		}
		autoPrintIfSet(message);
		return message;
	}
	
	
	private void addIfNotExist(IndividualData individualInsert, ArrayList<IndividualData> individuals)
	{
		boolean isExist = false;
		for( IndividualData individual :  individuals ){
			if( individual.id().equals( individualInsert.id() ) ){
				isExist = true;
				break;
			}
		}
		
		if( !isExist ){
			individuals.add(individualInsert);
		}
	}
	
	
	
	
}
