package SS;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
	
	
	/**
	 * Sprint 3
	 * 
	 * US19: First cousins should not marry
	 *       First cousins should not marry one another
	 */

	public String US19() {
		String message = printHead(" US19 :  First cousins should not marry ");
		String debugMsg = "";
		
		GEDData gedData = GEDData.getInstance();
		
		
		// Look at each Family
		for (FamilyData family : gedData.families) 
		{	
			// for each child in family
			for(String childId : family.childrenStrings) 
			{		
				// if husband is listed as child in family
				if(null != family.husband && null != family.husband.familyAsChild)
				{
					// for each of husband's siblings
					for(String husbandSiblingId : family.husband.familyAsChild.childrenStrings) 
					{		
						// for each of husband's sibling's family
						for(FamilyData husbandSiblingsFamily : gedData.getIndividualDataFromId(husbandSiblingId).familiesAsSpouse)
						{
							// get husband's sibling's children (cousins)
							for(String cousinId : husbandSiblingsFamily.childrenStrings) 
							{
								
								IndividualData childIndiv = gedData.getIndividualDataFromId(childId);

								// for all of families in which child is listed as spouse, check if married to 1st cousin
								for(FamilyData childFamilyAsSpouse : childIndiv.familiesAsSpouse) 
								{			
									if(null != childFamilyAsSpouse.husband && null != childFamilyAsSpouse.wife )  
									{
										
										if(childFamilyAsSpouse.husband.id().compareTo(cousinId) == 0 ||
										   childFamilyAsSpouse.wife.id().compareTo(cousinId) == 0)
										{
											if( childId.compareTo(cousinId) != 0)
											{
												// print error
												message += "ERROR: INDIVIDUAL: " + childId + " is married to 1st cousin " + cousinId + "\n";
											}
										}
									} // if(null != childFamilyAsSpouse.husband() && null != childFamilyAsSpouse.wife() )  									
								} // for(FamilyData childFamilyAsSpouse : childIndiv.familiesAsSpouse) 
							} // for(String cousinId : husbandSiblingsFamily.childrenStrings) 
						} // for(FamilyData husbandSiblingsFamily : gedData.getIndividualDataFromId(husbandSiblingId).familiesAsSpouse)
					} // for(String husbandSiblingId : family.husband.familyAsChild.childrenStrings) 
				} // if(null != family.husband && null != family.husband.familyAsChild)
			} // for(String childId : family.childrenStrings) 
		} // for (FamilyData family : gedData.families) 
		
		
		autoPrintIfSet(message);
		// uncomment for debug
		//autoPrintIfSet(debugMsg);
		return message;
	}
	
	/**
	 * Sprint 3
	 * 
	 * US21: Correct gender for role
	 *       Husband in family should be male and wife in family should be female
	 */

	public String US21() {
		String message = printHead(" US21 :  Correct gender for role ");
		String debugMsg = "";
		
		GEDData gedData = GEDData.getInstance();
		
		// Look at each Family
		for (FamilyData family : gedData.families) {
			
			// if husband is listed and sex is female ("F")
			if( null != family.husband && family.husband.sex.compareTo("F") == 0)
			{
				// print error
				message += "ERROR: INDIVIDUAL: Husband " + family.husband.id() + " in family " + family.id()
						+ "is indicated as female\n";
			}
			
			// if wife is listed and sex is female ("M")
			if( null != family.wife && family.wife.sex.compareTo("M") == 0)
			{
				// print error
				message += "ERROR: INDIVIDUAL: Wife " + family.wife.id() + " in family " + family.id()
						+ "is indicated as male\n";
			}
			
		}
		
		autoPrintIfSet(message);
		// uncomment for debug
		//autoPrintIfSet(debugMsg);
		return message;
	}
	
	/**
	 * Sprint 4
	 * 
	 * US22: All individual IDs should be unique and all family
	 *       IDs should be unique
	 */

	public String US22() {
		String message = printHead(" US22 :  Unique IDs");
		String debugMsg = "";
		
		GEDData gedData = GEDData.getInstance();
		
		Map<String, String> individualMap = new HashMap<String, String>();
		
		// Look at all individuals
		for(IndividualData individual : gedData.individuals) {
			if(null != individualMap.get(individual.id()))
			{
				// already mapped, so duplicate
				message += "ERROR: INDIVIDUAL: ID " + individual.id() + " is duplicated, names: " 
						+ individualMap.get(individual.id()) + ", " + individual.name + "\n";
			}
			else
			{
				// map new individual ID
				individualMap.put(individual.id(), individual.name);
			}
		}
		
		Map<String, String> familyMap = new HashMap<String, String>();
		
		// Look at all families
		for(FamilyData family : gedData.families) {
			if(null != familyMap.get(family.id()))
			{
				// already mapped, so duplicate
				message += "ERROR: FAMILY: ID " + family.id() + " is duplicated\n";
			}
			else
			{
				// map new individual ID
				familyMap.put(family.id(), family.id());
			}
		}
		
		autoPrintIfSet(message);
		// uncomment for debug
		//autoPrintIfSet(debugMsg);
		return message;
	}
	
	/**
	 * Sprint 4
	 * 
	 * US23: No more than one individual with the same name and 
	 *       birth date should appear in a GEDCOM file
	 */

	public String US23() {
		String message = printHead(" US23 :  Unique name and birth date");
		String debugMsg = "";
		
		GEDData gedData = GEDData.getInstance();
		
		// store individual data based on name key
		Map<String, IndividualData> individualMap = new HashMap<String, IndividualData>();
		
		// Look at all individuals
		for(IndividualData individual : gedData.individuals) {
			
			// if exact name already exists in map
			if(null != individualMap.get(individual.name))
			{
				// if birth date is also the same
				if(0 == individual.birth.compareTo(individualMap.get(individual.name).birth))
				{
					IndividualData indiv2 = individualMap.get(individual.name);
					
					// name and birth date match, so print error
					message += "ERROR: INDIVIDUAL: ID " + individual.id() + " and ID " + indiv2.id()
							+ " have the same name and birth date: " + individual.name + "\n";
				}
			}
			else
			{
				// map new name
				individualMap.put(individual.name, individual);
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
