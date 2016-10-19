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
		String message = printHead(" US25 : Unique first names in families ");
		String debugMsg = "";
		
		GEDData gedData = GEDData.getInstance();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/DD/YYYY");
		
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
					
					debugMsg += "name1 = "+compareChild1_data.name+", name2 = "+compareChild2_data.name+"\n"
							 +"bdate1 = "+simpleDateFormat.format(compareChild1_data.birth)
							 +", bdate2 = "+simpleDateFormat.format(compareChild2_data.birth)+"\n";
					
					// if children names and birthday's are the same, print error
					if( (compareChild1_data.name.compareTo(compareChild2_data.name) == 0) &&
						(compareChild1_data.birth.compareTo(compareChild2_data.birth) == 0)	)
					{
						message += "ERROR: INDIVIDUAL: US25: "+compareChild1_data.id()
								+": same name and birthdate as sibling "
								+compareChild2_data.id()+", Name: "+compareChild1_data.name
								+ ", Birth date: "+simpleDateFormat.format(compareChild1_data.birth)+"\n";
					}
					
					
					
				}
			}
		}
		autoPrintIfSet(message);
		// uncomment for debug
		// autoPrintIfSet(debugMsg);
		return message;
	}

	/**
	 * Sprint1
	 * 
	 * US26: Corresponding entries
	 *       All family roles (spouse, child) specified in an individual record 
	 *       should have corresponding entries in those family records, and all 
	 *       individual roles (spouse, child) specified in family records should 
	 *       have corresponding entries in those individual's records
	 */

	public String US26() {
		String message = printHead(" US26 : Corresponding entries ");
		String debugMsg = "";
		
		GEDData gedData = GEDData.getInstance();
		
		// Look at each individual
		for (IndividualData individual : gedData.individuals) {
			
			// if listed as a spouse
			// EXIT : if( individual.familyAsSpouse != null)
			if( individual.familiesAsSpouse.size()>0 && individual.familiesAsSpouse.get(0) != null)
			{
				String familySpouseId = individual.familiesAsSpouse.get(0).id();
				debugMsg += "Individual is spouse in family " + familySpouseId + "\n";
				boolean familyVerified = false;
				
				for (FamilyData family : gedData.families) {
					// if family matches indicated
					if( family.id().compareTo(familySpouseId) == 0 )
					{
						// if listed as husband or wife
						if( ((family.husband != null) && (family.husband.id().compareTo(individual.id()) == 0)) ||
							((family.wife != null) && (family.wife.id().compareTo(individual.id()) == 0)) )
						{
							familyVerified = true;
							break;
						}
					}
				}
				
				// if family record did not list individual as spouse
				if( false == familyVerified )
				{
					// print error
					message += "ERROR: INDIVIDUAL: " + individual.id() + " indicates spouse in family "
							+ familySpouseId + ", but family record does not list individual as spouse\n";
				}
			}
			
			// if listed as a child
			if( individual.familyAsChild != null)
			{
				String familyChildId = individual.familyAsChild.id();
				debugMsg += "Individual is child in family " + familyChildId + "\n";
				boolean familyVerified = false;
				
				for (FamilyData family : gedData.families) {
					// if family matches indicated
					if( family.id().compareTo(familyChildId) == 0 ){
						// for every child in matching family
						for(String childId : family.childrenStrings) {
							debugMsg += "   childId/indivId = " + childId + "/" + individual.id() + "\n";
							// if listed as one of the children
							if( childId.compareTo(individual.id()) == 0 )
							{
								familyVerified = true;
								break;
							}
						}
					}
				}
				
				// if family record did not list individual as child
				if( false == familyVerified )
				{
					// print error
					message += "ERROR: INDIVIDUAL: " + individual.id() + " indicates child in family "
							+ familyChildId + ", but family record does not list individual as child\n";
				}
			}
		
		}
		
		// look at each family
		for (FamilyData family : gedData.families) {
			
			// if husband listed
			if(family.husband != null)
			{
				// get husband's individual record
				String husbandId = family.husband.id();
				IndividualData husbandData = gedData.getIndividualDataFromId(husbandId);
				
				// if husband's individual record does not list him as a spouse of this family
				// EXIT : if( (husbandData.familyAsSpouse == null) ||(husbandData.familyAsSpouse.id().compareTo(family.id()) != 0) ){
				if( ( husbandData.familiesAsSpouse.size() <= 0 || husbandData.familiesAsSpouse.get(0).id().compareTo(family.id()) != 0) )
				{
					// print error
					message += "ERROR: FAMILY: " + family.id() + " lists individual " + husbandId
							+ " as husband, but individual record does not list as spouse of this family\n";
				}
			}
			
			// if wife listed
			if(family.wife != null)
			{
				// get wife's individual record
				String wifeId = family.wife.id();
				IndividualData wifeData = gedData.getIndividualDataFromId(wifeId);
				
				// if wife's individual record does not list her as a spouse of this family
				//EXIT : if( (wifeData.familyAsSpouse == null) || (wifeData.familyAsSpouse.id().compareTo(family.id()) != 0) )
				if( ( wifeData.familiesAsSpouse.size() <= 0 || wifeData.familiesAsSpouse.get(0).id().compareTo(family.id()) != 0) )
				{
					// print error
					message += "ERROR: FAMILY: " + family.id() + " lists individual " + wifeId
							+ " as wife, but individual record does not list as spouse of this family\n";
				}
			}
			
			// check all children
			for(String childId : family.childrenStrings) {
				// get child's individual record
				IndividualData childData = gedData.getIndividualDataFromId(childId);
				
				// if child's individual record does not list them as a child of this family
				if( (childData.familyAsChild == null) ||
					(childData.familyAsChild.id().compareTo(family.id()) != 0) )
				{
					// print error
					message += "ERROR: FAMILY: " + family.id() + " lists individual " + childId
							+ " as child, but individual record does not list as child of this family\n";
				}
			}
			
		}
		
		autoPrintIfSet(message);
		// uncomment for debug
		//autoPrintIfSet(debugMsg);
		return message;
	}
	
	
	/**
	 * Sprint 2
	 * 
	 * US17: No marriages to descendants
	 *       Parents should not marry any of their descendants
	 */

	public String US17() {
		String message = printHead(" US17 :  No marriages to descendants ");
		String debugMsg = "";
		
		GEDData gedData = GEDData.getInstance();
		
		boolean isMarriedToDescendant = false;
		
		// Look at each individual
		for(IndividualData individual : gedData.individuals) {
			
			String spouseId = individual.getSpouseId();
			
			debugMsg += "Indiv " + individual.id() + ", spouse = " + spouseId + "\n";
			
			// if spouse found (not empty string)
			if( spouseId.compareTo("") != 0 )
			{
				isMarriedToDescendant = isIdInDescendantTree(individual.id(), spouseId);
			}
			
			if( true == isMarriedToDescendant )
			{
				// print error
				message += "ERROR: INDIVIDUAL: " + individual.id() + " is married to " + spouseId
						+ ", who is a descendant\n";
			}
		}
		
		autoPrintIfSet(message);
		// uncomment for debug
		//autoPrintIfSet(debugMsg);
		return message;
	}
	
	/**
	 * Sprint 2
	 * 
	 * US18: Siblings should not marry
	 *       Siblings should not marry one another
	 */

	public String US18() {
		String message = printHead(" US18 :  Siblings should not marry ");
		String debugMsg = "";
		
		GEDData gedData = GEDData.getInstance();
		
		// Look at each individual
		for(IndividualData individual : gedData.individuals) {
			
			String spouseId = individual.getSpouseId();
			if(spouseId.compareTo("") != 0)
			{
				IndividualData spouseIndiv = gedData.getIndividualDataFromId(spouseId);
				
				// if if individual and spouse have the same familyAsChild ID
				if( null != individual.familyAsChild && null != spouseIndiv.familyAsChild)
				{
					if( individual.familyAsChild.id().compareTo(spouseIndiv.familyAsChild.id()) == 0 )
					{
						// print error
						message += "ERROR: INDIVIDUAL: " + individual.id() + " is married to " + spouseId
								+ ", who is a sibling\n";
					}
				}
			}
		}
		
		autoPrintIfSet(message);
		// uncomment for debug
		//autoPrintIfSet(debugMsg);
		return message;
	}
	
	// Function to determine if search ID (searchId) is preset within the descendant tree of source ID (sourceId)
	int MAX_DESCENDANT_TREE_RECURSION_DEPTH = 10; // Will traverse down 10 generations max
	
	private boolean isIdInDescendantTree(String searchId, String sourceId)
	{
		return isIdInDescendantTree(searchId, sourceId, 0);
	}
	
	// Recursive function for above - passes in recursive depth to know when max is reached.
	private boolean isIdInDescendantTree(String searchId, String sourceId, int recursiveDepth)
	{
		//System.out.println("isIdInDescendantTree: searchId: " + searchId 
		//		+ ", sourceId: " + sourceId + "\n");

		boolean idInDescendantTree = false;
		
		// get individual record for sourceId
		IndividualData sourceIndivData = GEDData.getInstance().getIndividualDataFromId(sourceId);
		
		for(FamilyData family : sourceIndivData.familiesAsSpouse) {
			// loop through each child for this family
			for(String childId : family.childrenStrings) {
				// if child matches searchId
				if(childId.compareTo(searchId) == 0)
				{
					// match found, return true
					idInDescendantTree = true;
				}
				else
				{
					// only search further if not past maximum recursive depth
					if( MAX_DESCENDANT_TREE_RECURSION_DEPTH > recursiveDepth )
					{
						// match not found, continue down this child's decendant tree
						idInDescendantTree = isIdInDescendantTree(searchId, childId, recursiveDepth + 1);
					}
				}
				
				if(true == idInDescendantTree)
				{
					// match found, break out of loop
					break;
				}
			} // for(String childId : family.childrenStrings
			
			if(true == idInDescendantTree)
			{
				// match found, break out of loop
				break;
			}
		} // for(FamilyData family : sourceIndivData.familiesAsSpouse)
		
		return idInDescendantTree;
		
	} // private boolean isIdInDescendantTree(String searchId, String sourceId, int recursiveDepth)

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
