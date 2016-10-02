package JF;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.agile.exit.data.FamilyData;
import com.agile.exit.data.GEDData;
import com.agile.exit.data.IndividualData;

public class jfUserStory {

	private Boolean isAutoPrint;

	public jfUserStory() {
		this(true);
	}

	public jfUserStory(Boolean isAutoPrint) {
		this.isAutoPrint = isAutoPrint;
	}

	/**
	 * Sprint1
	 */
	public String getUs01() {
		Date now = new Date();
		SimpleDateFormat bartDateFormat = new SimpleDateFormat
  				("MM/dd/yyyy");
		String message = printHead(" US01 : Dates before current date ");
		for (IndividualData individual : GEDData.getInstance().individuals) {
			if (individual.dateOfDeath != null
					&& individual.dateOfDeath.compareTo(now) > 0) {
				message += "ERROR: INDIVIDUAL: US01: "+individual.id()+": Death date: "
						+ bartDateFormat.format(individual.dateOfDeath) + " occurs in the future"
						+ "\n";
			}
			if (individual.birth != null && individual.birth.compareTo(now) > 0) {
				message += "ERROR: INDIVIDUAL: US01: "+individual.id()+": Birth date: "
						+ bartDateFormat.format(individual.birth) + " occurs in the future" + "\n";
			}
		}
		for (FamilyData family : GEDData.getInstance().families) {
			if (family.marriageDate != null
					&& family.marriageDate.compareTo(now) > 0) {
				message += "ERROR: FAMILY: US01: "+family.id()+": Marriage date£º "
						+ bartDateFormat.format(family.marriageDate) + " occurs in the future" + "\n";
			}
			if (family.divorceDate != null
					&& family.divorceDate.compareTo(now) > 0) {
				message += "ERROR: FAMILY: US01: "+family.id()+": Divorce date£º "
						+ bartDateFormat.format(family.divorceDate) + " occurs in the future" + "\n";
			}
		}

		autoPrintIfSet(message);
		return message;
	}

	/**
	 * Sprint1
	 */

	public String getUs02() {
		SimpleDateFormat bartDateFormat = new SimpleDateFormat
  				("MM/dd/yyyy");
		String message = printHead(" US02 : Birth before marriage ");
		for (FamilyData family : GEDData.getInstance().families) {
			for (IndividualData individual : GEDData.getInstance().individuals) {
				if (individual.name.equals(family.husband.name)) {
					if (family.marriageDate.compareTo(individual.birth) < 0) {
						message += "ERROR: FAMILY: US02: "+family.id()+": Husband's birth date"
								+ bartDateFormat.format(individual.birth)
								+ " following marriage date"
								+ bartDateFormat.format(family.marriageDate) + "\n";
					}
				}
				if (individual.name.equals(family.wife.name)) {
					if (family.marriageDate.compareTo(individual.birth) < 0) {
						message += "ERROR: FAMILY: US02: "+family.id()+": Wife's birth date"
								+ bartDateFormat.format(individual.birth)
								+ " following marriage date"
								+ bartDateFormat.format(family.marriageDate) + "\n";
					}
				}
			}
		}
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
