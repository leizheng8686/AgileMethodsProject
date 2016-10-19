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
				if(family.husband.birth != null && getYearsDiff(family.husband.birth, family.marriageDate) < 14){
					message += "ERROR: FAMILY: US10: "+family.husband.id()+": Marriage before 14 - Birth date: "
							+ bartDateFormat.format(family.husband.birth) + " Marriage date: "
							+ bartDateFormat.format(family.marriageDate) + "\n";
				}
				if(family.wife.birth != null && getYearsDiff(family.wife.birth, family.marriageDate) < 14){
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
		String message = printHead(" US11 : No bigamy ");
		for (IndividualData individual : GEDData.getInstance().individuals) {
			if (individual.familiesAsSpouse != null && individual.familiesAsSpouse.size() > 1) {
				for( int i = 0; i < individual.familiesAsSpouse.size() - 1; i++){
					//the former with marriage date and divorce date
					if(individual.familiesAsSpouse.get(i).marriageDate != null 
							&& individual.familiesAsSpouse.get(i).divorceDate != null){
						for(int j = i + 1; j < individual.familiesAsSpouse.size(); j++){
							if(individual.familiesAsSpouse.get(j).marriageDate != null){
								if(individual.familiesAsSpouse.get(i).marriageDate.compareTo(individual.familiesAsSpouse.get(j).marriageDate) < 0
										&& individual.familiesAsSpouse.get(i).divorceDate.compareTo(individual.familiesAsSpouse.get(j).marriageDate) > 0)
									if(individual.sex.equals("M"))
										message += "ERROR: INDIVIDUAL: US11: " + individual.id() + " is bigamous with "
												+ individual.familiesAsSpouse.get(i).wife.id() + " (Marriage: " 
												+ bartDateFormat.format(individual.familiesAsSpouse.get(i).marriageDate) + " - "
												+ bartDateFormat.format(individual.familiesAsSpouse.get(i).divorceDate) + ")"+ " and " 
												+ individual.familiesAsSpouse.get(j).wife.id() + " (Marriage: " 
												+ bartDateFormat.format(individual.familiesAsSpouse.get(j).marriageDate) + " - "
												+ (individual.familiesAsSpouse.get(j).divorceDate != null?
														bartDateFormat.format(individual.familiesAsSpouse.get(j).divorceDate) : " ") + ")"+ "\n";
									else
										message += "ERROR: INDIVIDUAL: US11: " + individual.id() + " is bigamous with "
												+ individual.familiesAsSpouse.get(i).husband.id() + " (Marriage: " 
												+ bartDateFormat.format(individual.familiesAsSpouse.get(i).marriageDate) + " - "
												+ bartDateFormat.format(individual.familiesAsSpouse.get(i).divorceDate) + ")"+ " and " 
												+ individual.familiesAsSpouse.get(j).husband.id() + " (Marriage: " 
												+ bartDateFormat.format(individual.familiesAsSpouse.get(j).marriageDate) + " - "
												+ (individual.familiesAsSpouse.get(j).divorceDate != null?
														bartDateFormat.format(individual.familiesAsSpouse.get(j).divorceDate) : " ") + ")"+ "\n";
							}else if(individual.familiesAsSpouse.get(j).divorceDate != null){
								if(individual.familiesAsSpouse.get(i).marriageDate.compareTo(individual.familiesAsSpouse.get(j).divorceDate) < 0
										&& individual.familiesAsSpouse.get(i).divorceDate.compareTo(individual.familiesAsSpouse.get(j).divorceDate) > 0)
									if(individual.sex.equals("M"))
										message += "ERROR: INDIVIDUAL: US11: " + individual.id() + " is bigamous with "
												+ individual.familiesAsSpouse.get(i).wife.id() + " (Marriage: " 
												+ bartDateFormat.format(individual.familiesAsSpouse.get(i).marriageDate) + " - "
												+ bartDateFormat.format(individual.familiesAsSpouse.get(i).divorceDate) + ")"+ " and " 
												+ individual.familiesAsSpouse.get(j).wife.id() + " (Marriage:   - "
												+ bartDateFormat.format(individual.familiesAsSpouse.get(j).divorceDate) + ")"+ "\n";
									else
										message += "ERROR: INDIVIDUAL: US11: " + individual.id() + " is bigamous with "
												+ individual.familiesAsSpouse.get(i).husband.id() + " (Marriage: " 
												+ bartDateFormat.format(individual.familiesAsSpouse.get(i).marriageDate) + " - "
												+ bartDateFormat.format(individual.familiesAsSpouse.get(i).divorceDate) + ")"+ " and " 
												+ individual.familiesAsSpouse.get(j).husband.id() + " (Marriage:   - " 
												+ bartDateFormat.format(individual.familiesAsSpouse.get(j).divorceDate) + ")"+ "\n";
							}
						}
					}// only marriage date
					else if(individual.familiesAsSpouse.get(i).marriageDate != null){
						for(int j = i + 1; j < individual.familiesAsSpouse.size(); j++){
							if(individual.familiesAsSpouse.get(j).marriageDate != null 
									&& individual.familiesAsSpouse.get(j).divorceDate != null){
								if(individual.familiesAsSpouse.get(j).marriageDate.compareTo(individual.familiesAsSpouse.get(i).marriageDate) < 0
										&& individual.familiesAsSpouse.get(j).divorceDate.compareTo(individual.familiesAsSpouse.get(i).marriageDate) > 0)
									if(individual.sex.equals("M"))
										message += "ERROR: INDIVIDUAL: US11: " + individual.id() + " is bigamous with "
												+ individual.familiesAsSpouse.get(i).wife.id() + " (Marriage: " 
												+ bartDateFormat.format(individual.familiesAsSpouse.get(i).marriageDate) + " -  ) and " 
												+ individual.familiesAsSpouse.get(j).wife.id() + " (Marriage: "
												+ bartDateFormat.format(individual.familiesAsSpouse.get(j).marriageDate) + " - " 
												+ bartDateFormat.format(individual.familiesAsSpouse.get(j).divorceDate) + ")"+ "\n";
									else
										message += "ERROR: INDIVIDUAL: US11: " + individual.id() + " is bigamous with "
												+ individual.familiesAsSpouse.get(i).husband.id() + " (Marriage: " 
												+ bartDateFormat.format(individual.familiesAsSpouse.get(i).marriageDate) + " -  ) and " 
												+ individual.familiesAsSpouse.get(j).husband.id() + " (Marriage: "
												+ bartDateFormat.format(individual.familiesAsSpouse.get(j).marriageDate) + " - " 
												+ bartDateFormat.format(individual.familiesAsSpouse.get(j).divorceDate) + ")"+ "\n";
							}
						}
					}// only divorce date
					else if(individual.familiesAsSpouse.get(i).divorceDate != null){
						for(int j = i + 1; j < individual.familiesAsSpouse.size(); j++){
							if(individual.familiesAsSpouse.get(j).marriageDate != null 
									&& individual.familiesAsSpouse.get(j).divorceDate != null){
								if(individual.familiesAsSpouse.get(j).marriageDate.compareTo(individual.familiesAsSpouse.get(i).divorceDate) < 0
										&& individual.familiesAsSpouse.get(j).divorceDate.compareTo(individual.familiesAsSpouse.get(i).divorceDate) > 0)
									if(individual.sex.equals("M"))
										message += "ERROR: INDIVIDUAL: US11: " + individual.id() + " is bigamous with "
												+ individual.familiesAsSpouse.get(i).wife.id() + " (Marriage:   - " 
												+ bartDateFormat.format(individual.familiesAsSpouse.get(i).divorceDate) + ") and " 
												+ individual.familiesAsSpouse.get(j).wife.id() + " (Marriage: "
												+ bartDateFormat.format(individual.familiesAsSpouse.get(j).marriageDate) + " - " 
												+ bartDateFormat.format(individual.familiesAsSpouse.get(j).divorceDate) + ")"+ "\n";
									else
										message += "ERROR: INDIVIDUAL: US11: " + individual.id() + " is bigamous with "
												+ individual.familiesAsSpouse.get(i).husband.id() + " (Marriage:   - " 
												+ bartDateFormat.format(individual.familiesAsSpouse.get(i).divorceDate) + ") and " 
												+ individual.familiesAsSpouse.get(j).husband.id() + " (Marriage: "
												+ bartDateFormat.format(individual.familiesAsSpouse.get(j).marriageDate) + " - " 
												+ bartDateFormat.format(individual.familiesAsSpouse.get(j).divorceDate) + ")"+ "\n";
							}
						}
					}
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
						if(family.husband.birth != null && getYearsDiff(family.husband.birth, indi.birth) >= 80)
							message += "ERROR: INDIVIDUAL: US12: Father : "+family.husband.id()+" (Birth: " 
									+ bartDateFormat.format(family.husband.birth) + ") is too old to give birth to the Child : "
									+ indi.id() + " (Birth: " + bartDateFormat.format(indi.birth) + ")\n";
<<<<<<< Updated upstream
						if(family.wife != null && family.wife.birth != null && getAge(family.wife.birth, indi.birth) >= 60)
=======
						if(family.wife.birth != null && getYearsDiff(family.wife.birth, indi.birth) >= 60)
>>>>>>> Stashed changes
							message += "ERROR: INDIVIDUAL: US12: Mother : "+family.wife.id()+" (Birth: " 
									+ bartDateFormat.format(family.wife.birth) + ") is too old to give birth to the Child : "
									+ indi.id() + " (Birth: " + bartDateFormat.format(indi.birth) + ")\n";
					}
			}
		}
		autoPrintIfSet(message);
		return message;
	}
	
	/* TODO
	 * US13 : Siblings spacing
	 * Birth dates of siblings should be more than 8 months apart or less than 2 days apart
	 */
	public String US13(){
		SimpleDateFormat bartDateFormat = new SimpleDateFormat
  				("MM/dd/yyyy");
		String message = printHead(" US13 : Siblings spacing ");
		
		autoPrintIfSet(message);
		return message;
	}
	
	/* TODO
	 * US14 : Multiple births less than 5
	 * No more than five siblings should be born at the same time
	 */
	public String US14(){
		SimpleDateFormat bartDateFormat = new SimpleDateFormat
  				("MM/dd/yyyy");
		String message = printHead(" US14 : Multiple births less than 5 ");
		
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
	public  int getYearsDiff(Date birthDay, Date thatDay) {  
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
	
//	public int getDaysDiff(Date date1, Date date2) { 
//	    
//		java.util.Calendar calst = java.util.Calendar.getInstance();   
//		java.util.Calendar caled = java.util.Calendar.getInstance();
//		if(date1.compareTo(date2) < 0){
//			calst.setTime(date1);   
//			caled.setTime(date2);   
//		}else{
//			calst.setTime(date2);   
//			caled.setTime(date1);
//		}
//		//set time   
//		calst.set(java.util.Calendar.HOUR_OF_DAY, 0);   
//		calst.set(java.util.Calendar.MINUTE, 0);   
//		calst.set(java.util.Calendar.SECOND, 0);   
//		caled.set(java.util.Calendar.HOUR_OF_DAY, 0);   
//		caled.set(java.util.Calendar.MINUTE, 0);   
//		caled.set(java.util.Calendar.SECOND, 0);   
//		//get days difference   
//		int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst   
//		        .getTime().getTime() / 1000)) / 3600 / 24;   
//		 
//		return days;   
//   } 
	
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
	
	public static void main(String args[]){
		LeiUserStories l = new LeiUserStories();
		
//		System.out.println(l.getDaysDiff(date1, date2));
	}
}
