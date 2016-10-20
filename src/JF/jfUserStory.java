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
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String message = printHead(" US01 : Dates before current date ");
		for (IndividualData individual : GEDData.getInstance().individuals) {
			if (individual.dateOfDeath != null
					&& individual.dateOfDeath.compareTo(now) > 0) {
				message += "ERROR: INDIVIDUAL: US01: " + individual.id()
						+ ": Death date: "
						+ bartDateFormat.format(individual.dateOfDeath)
						+ " occurs in the future" + "\n";
			}
			if (individual.birth != null && individual.birth.compareTo(now) > 0) {
				message += "ERROR: INDIVIDUAL: US01: " + individual.id()
						+ ": Birth date: "
						+ bartDateFormat.format(individual.birth)
						+ " occurs in the future" + "\n";
			}
		}
		for (FamilyData family : GEDData.getInstance().families) {
			if (family.marriageDate != null
					&& family.marriageDate.compareTo(now) > 0) {
				message += "ERROR: FAMILY: US01: " + family.id()
						+ ": Marriage date�� "
						+ bartDateFormat.format(family.marriageDate)
						+ " occurs in the future" + "\n";
			}
			if (family.divorceDate != null
					&& family.divorceDate.compareTo(now) > 0) {
				message += "ERROR: FAMILY: US01: " + family.id()
						+ ": Divorce date�� "
						+ bartDateFormat.format(family.divorceDate)
						+ " occurs in the future" + "\n";
			}
		}

		autoPrintIfSet(message);
		return message;
	}

	/**
	 * Sprint1
	 */

	public String getUs02() {
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String message = printHead(" US02 : Birth before marriage ");
		for (FamilyData family : GEDData.getInstance().families) {
			for (IndividualData individual : GEDData.getInstance().individuals) {
				if (individual.name.equals(family.husband.name)) {
					if (family.marriageDate != null
							&& individual.birth != null
							&& family.marriageDate.compareTo(individual.birth) < 0) {
						message += "ERROR: FAMILY: US02: " + family.id()
								+ ": Husband's birth date"
								+ bartDateFormat.format(individual.birth)
								+ " following marriage date"
								+ bartDateFormat.format(family.marriageDate)
								+ "\n";
					}
				}
				if ((null != family.wife)
						&& (individual.name.equals(family.wife.name))) {
					if (family.marriageDate != null
							&& individual.birth != null
							&& family.marriageDate.compareTo(individual.birth) < 0) {
						message += "ERROR: FAMILY: US02: " + family.id()
								+ ": Wife's birth date: "
								+ bartDateFormat.format(individual.birth)
								+ " following marriage date: "
								+ bartDateFormat.format(family.marriageDate)
								+ "\n";
					}
				}
			}
		}
		autoPrintIfSet(message);
		return message;
	}

	/**
	 * Sprint2
	 */
	public String getUs03() {
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String message = printHead(" US03 : Birth before death ");
		for (IndividualData individual : GEDData.getInstance().individuals) {
			if (individual.dateOfDeath != null && individual.birth != null
					&& individual.dateOfDeath.compareTo(individual.birth) < 0) {
				message += "ERROR: INDIVIDUAL: US03: " + individual.id()
						+ ": Death date: "
						+ bartDateFormat.format(individual.dateOfDeath)
						+ " occurs before birth date:"
						+ bartDateFormat.format(individual.birth) + "\n";
			}
		}
		autoPrintIfSet(message);
		return message;
	}

	public String getUs04() {
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String message = printHead(" US04 : Marriage before divorce ");
		for (FamilyData family : GEDData.getInstance().families) {
			if (family.marriageDate != null && family.divorceDate != null
					&& family.marriageDate.compareTo(family.divorceDate) > 0) {
				message += "ERROR: FAMILY: US04: " + family.id()
						+ ": divorce date:"
						+ bartDateFormat.format(family.divorceDate)
						+ " occur before marriage date"
						+ bartDateFormat.format(family.marriageDate) + "\n";
			}
		}
		autoPrintIfSet(message);
		return message;
	}

	/**
	 * Sprint3
	 */

	public String getUs05() {
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String message = printHead(" US05 : Marriage before death ");
		for (FamilyData family : GEDData.getInstance().families) {
			if (family.marriageDate != null
					&& family.husband.dateOfDeath != null
					&& family.wife.dateOfDeath != null) {
				if (family.marriageDate.compareTo(family.husband.dateOfDeath) > 0) {
					message += "ERROR: FAMILY: US05: " + family.id()
							+ ": marriage date:"
							+ bartDateFormat.format(family.marriageDate)
							+ " occur after husband death date"
							+ bartDateFormat.format(family.husband.dateOfDeath)
							+ "\n";
				} else if (family.marriageDate
						.compareTo(family.wife.dateOfDeath) > 0) {
					message += "ERROR: FAMILY: US05: " + family.id()
							+ ": marriage date:"
							+ bartDateFormat.format(family.marriageDate)
							+ " occur after wife death date"
							+ bartDateFormat.format(family.wife.dateOfDeath)
							+ "\n";
				}
			}
		}
		autoPrintIfSet(message);
		return message;
	}

	public String getUs06() {
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String message = printHead(" US06 : Divorce before death ");
		for (FamilyData family : GEDData.getInstance().families) {
			if (family.divorceDate != null
					&& family.husband.dateOfDeath != null
					&& family.wife.dateOfDeath != null) {
				if (family.divorceDate.compareTo(family.husband.dateOfDeath) > 0) {
					message += "ERROR: FAMILY: US05: " + family.id()
							+ ": Divorce date:"
							+ bartDateFormat.format(family.divorceDate)
							+ " occur after husband death date"
							+ bartDateFormat.format(family.husband.dateOfDeath)
							+ "\n";
				} else if (family.divorceDate
						.compareTo(family.wife.dateOfDeath) > 0) {
					message += "ERROR: FAMILY: US05: " + family.id()
							+ ": Divorce date:"
							+ bartDateFormat.format(family.divorceDate)
							+ " occur after wife death date"
							+ bartDateFormat.format(family.wife.dateOfDeath)
							+ "\n";
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
