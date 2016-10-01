package SS;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.agile.exit.data.FamilyData;
import com.agile.exit.data.GEDData;
import com.agile.exit.data.IndividualData;


public class ssonntagUserStories {

	private Boolean isAutoPrint;

	public ssonntagUserStories() {
		this(true);
	}

	public ssonntagUserStories(Boolean isAutoPrint) {
		this.isAutoPrint = isAutoPrint;
	}

	/**
	 * Sprint1
	 * 
	 * US25: Unique first names in families
	 *       No more than one child with the same name 
	 *       and birth date should appear in a family
	 */
	public String US25() {
		Date now = new Date();
		SimpleDateFormat bartDateFormat = new SimpleDateFormat
  				("MM/dd/yyyy");
		String message = printHead(" US25 : Unique first names in families ");
		String debugMsg = "";
		
		GEDData gedData = GEDData.getInstance();
		
		for (FamilyData family : gedData.families) {
				debugMsg += "Family ID " + family.id() + "\n";
			for(String childId : family.childrenStrings) {
				debugMsg += "  child ID = " + childId + "\n";
				
				// Child to compare with remainging children
				String compareChild1_Id = childId;
				int compareChild1_index = family.childrenStrings.indexOf(childId);
				debugMsg += "  child1 index = " + compareChild1_index + "\n";
				
				// Loop through remaining children (higher index into childrenStrings)
				for(int compareChild2_index = compareChild1_index + 1;  compareChild2_index < family.childrenStrings.size(); compareChild2_index++)
				{
					debugMsg += "    child2 index = " + compareChild2_index + "\n";
					
					IndividualData compareChild1_data = gedData.getIndividualDataFromId(family.childrenStrings.get(compareChild1_index));
					IndividualData compareChild2_data = gedData.getIndividualDataFromId(family.childrenStrings.get(compareChild2_index));
					
					debugMsg += "name1 = "+compareChild1_data.name+", name2 = "+compareChild2_data.name+"\n";
					debugMsg += "bdate1 = "+compareChild1_data.birth+", bdate2 = "+compareChild2_data.birth+"\n";
					
					// if children names and birthday's are the same, print error
					if( (compareChild1_data.name.compareTo(compareChild2_data.name) == 0) &&
						(compareChild1_data.birth.compareTo(compareChild2_data.birth) == 0)	)
					{
						message += "ERROR: INDIVIDUAL: US25: "+compareChild1_data.id()
								+": same name and birthdate as sibling "
								+compareChild2_data.id()+", Name: "+compareChild1_data.name
								+ "Birth date: "+compareChild1_data.birth+"\n";
					}
					
					
					
				}
			}
		}
		autoPrintIfSet(message);
		// uncomment for debug
		//autoPrintIfSet(debugMsg);
		return message;
	}

	/**
	 * Sprint1
	 */

	public String US26() {
		String message = printHead(" US26 : Corresponding entries ");
		autoPrintIfSet(message);
		return message;
	}

	/**
	 * Helper
	 */

	private void autoPrintIfSet(String message) {
		if (isAutoPrint) {
			System.out.println(message);
		}
	}

	private String printHead(String str) {
		int padding = 6;
		String message = getStarsLine(str.length() + padding * 2) + "\n";
		message += getStarsLine(padding) + str + getStarsLine(padding) + "\n";
		message += getStarsLine(str.length() + padding * 2) + "\n";
		return message;
	}

	private String getStarsLine(int length) {
		String line = "";
		for (int i = 0; i <= length - 1; i++) {
			line += "*";
		}
		return line;
	}
}
