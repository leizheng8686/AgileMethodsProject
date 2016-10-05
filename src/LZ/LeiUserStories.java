package LZ;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.agile.exit.data.*;

public class LeiUserStories {
	
	private Boolean isAutoPrint;

	public LeiUserStories(){
		this(true);
	}
	public LeiUserStories(Boolean isAutoPrint){
		this.isAutoPrint = isAutoPrint;
	}

	//Sprint 1
	/*
	 * US08 : Birth before marriage of parents
	 * Child should be born after marriage of parents (and before their divorce)
	 */
	public String US08(){
		SimpleDateFormat bartDateFormat = new SimpleDateFormat
  				("MM/dd/yyyy");
		String message = printHead(" US08 : Birth before marriage of parents ");
		for (IndividualData individual : GEDData.getInstance().individuals) {
			if (individual.birth != null && individual.familyAsChild != null) {
				if( individual.familyAsChild.marriageDate != null &&
						(individual.birth.compareTo(individual.familyAsChild.marriageDate) < 0)){
					message += "ERROR: INDIVIDUAL: US08: "+individual.id()+": Birth date: "
							+ bartDateFormat.format(individual.birth) + " before Marriage date of parents : " 
							+ bartDateFormat.format(individual.familyAsChild.marriageDate) + "\n";
				}
				if(	individual.familyAsChild.divorceDate != null &&
						(individual.birth.compareTo(individual.familyAsChild.divorceDate) > 0)){
					message += "ERROR: INDIVIDUAL: US08: "+individual.id()+": Birth date: "
							+ bartDateFormat.format(individual.birth) + " after Divorce date of parents : " 
							+ bartDateFormat.format(individual.familyAsChild.divorceDate) + "\n";
				}
			}
		}

		autoPrintIfSet(message);
		return message;
	}
	
	/*
	 * US10 : Marriage after 14
	 * Marriage should be at least 14 years after birth of both spouses
	 */
	public String US10(){
		SimpleDateFormat bartDateFormat = new SimpleDateFormat
  				("MM/dd/yyyy");
		String message = printHead(" US10 : Marriage after 14 ");
		for (FamilyData family : GEDData.getInstance().families) {
			if (family.marriageDate != null) {
				if(family.husband.birth != null && getAge(family.husband.birth, family.marriageDate) < 14){
					message += "ERROR: FAMILY: US10: "+family.husband.id()+": Marriage before 14 - Birth date: "
							+ bartDateFormat.format(family.husband.birth) + " Marriage date: "
							+ bartDateFormat.format(family.marriageDate) + "\n";
				}
				if(family.wife.birth != null && getAge(family.wife.birth, family.marriageDate) < 14){
					message += "ERROR: FAMILY: US10: "+family.wife.id()+": Marriage before 14 - Birth date: "
							+ bartDateFormat.format(family.wife.birth) + " Marriage date: "
							+ bartDateFormat.format(family.marriageDate) + "\n";
				}
			}
		}
		autoPrintIfSet(message);
		return message;
	}
	
	/*
	 * US11 : No bigamy
	 * Marriage should not occur during marriage to another spouse
	 */
	public String US11(){
		SimpleDateFormat bartDateFormat = new SimpleDateFormat
  				("MM/dd/yyyy");
		String message = printHead(" US10 : Marriage after 14 ");
		for (FamilyData family : GEDData.getInstance().families) {
			if (family.marriageDate != null) {
				if(family.husband.birth != null && getAge(family.husband.birth, family.marriageDate) < 14){
					message += "ERROR: FAMILY: US10: "+family.husband.id()+": Marriage before 14 - Birth date: "
							+ bartDateFormat.format(family.husband.birth) + " Marriage date: "
							+ bartDateFormat.format(family.marriageDate) + "\n";
				}
				if(family.wife.birth != null && getAge(family.wife.birth, family.marriageDate) < 14){
					message += "ERROR: FAMILY: US10: "+family.wife.id()+": Marriage before 14 - Birth date: "
							+ bartDateFormat.format(family.wife.birth) + " Marriage date: "
							+ bartDateFormat.format(family.marriageDate) + "\n";
				}
			}
		}
		autoPrintIfSet(message);
		return message;
	}
	
	/*
	 * US12 : Parents not too old
	 * Mother should be less than 60 years older than her children 
	 * and father should be less than 80 years older than his children
	 */
	public String US12(){
		SimpleDateFormat bartDateFormat = new SimpleDateFormat
  				("MM/dd/yyyy");
		String message = printHead(" US12 : Parents not too old ");
		for (FamilyData family : GEDData.getInstance().families) {
			if (family.children != null) {
				for(IndividualData indi : family.children)
					if(indi.birth != null){
						if(family.husband.birth != null && getAge(family.husband.birth, indi.birth) >= 80)
							message += "ERROR: INDIVIDUAL: US12: "+family.husband.id()+": Father is too older - Birth date: "
									+ bartDateFormat.format(family.husband.birth) + " to give birth to the Child : "
									+ indi.id() + " Birth: " + bartDateFormat.format(indi.birth) + "\n";
						if(family.wife.birth != null && getAge(family.husband.birth, indi.birth) >= 60)
							message += "ERROR: INDIVIDUAL: US12: "+family.wife.id()+": Mother is too older - Birth date: "
									+ bartDateFormat.format(family.wife.birth) + " to give birth to the Child : "
									+ indi.id() + " Birth: " + bartDateFormat.format(indi.birth) + "\n";
					}
			}
		}
		autoPrintIfSet(message);
		return message;
	}
	
	//calculate the age from birthday to now
	public  int getAge(Date birthDay) {  
        Calendar cal = Calendar.getInstance();  
  
        if (cal.before(birthDay)) {  
            throw new IllegalArgumentException(  
                    "The birthDay is in the future.It's unbelievable!");  
        }  
        int yearNow = cal.get(Calendar.YEAR);  
        int monthNow = cal.get(Calendar.MONTH);  
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);  
        cal.setTime(birthDay);  
  
        int yearBirth = cal.get(Calendar.YEAR);  
        int monthBirth = cal.get(Calendar.MONTH);  
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);  
  
        int age = yearNow - yearBirth;  
  
        if (monthNow <= monthBirth) {  
            if (monthNow == monthBirth) {  
                if (dayOfMonthNow < dayOfMonthBirth) age--;  
            }else{  
                age--;  
            }  
        }  
        return age;  
    } 
	
	//calculate the age from birthday to that day
	public  int getAge(Date birthDay, Date thatDay) {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(thatDay);
        
        if (cal.before(birthDay)) {  
            throw new IllegalArgumentException(  
                    "The birthDay is after the day!");  
        }  
        int yearNow = cal.get(Calendar.YEAR);  
        int monthNow = cal.get(Calendar.MONTH);  
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);  
        cal.setTime(birthDay);  
  
        int yearBirth = cal.get(Calendar.YEAR);  
        int monthBirth = cal.get(Calendar.MONTH);  
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);  
  
        int age = yearNow - yearBirth;  
  
        if (monthNow <= monthBirth) {  
            if (monthNow == monthBirth) {  
                if (dayOfMonthNow < dayOfMonthBirth) age--;  
            }else{  
                age--;  
            }  
        }  
        return age;  
    } 
	
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
