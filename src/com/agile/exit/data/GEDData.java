package com.agile.exit.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author exit
 *http://www.familyecho.com
 *u: JustMadcat
 *p: 123456abc
 */
public class GEDData {
	private static GEDData instance = null;
	
	private String otherData = "";
	
	private BaseData currentBaseData;
	private ArrayList<String> gedStrings = new ArrayList<String>();
	
	public ArrayList<IndividualData> individuals = new ArrayList<IndividualData>();
	public ArrayList<FamilyData> families = new ArrayList<FamilyData>();
	
	private Map<String,IndividualData> individualIdMap = new HashMap<String,IndividualData>();
	private Map<String,FamilyData> familyIdMap = new HashMap<String,FamilyData>();
	
	
	/**
	 * Collect all string first to avoid try catch in the main file.
	 */
	public void addGEDString(String line){
		gedStrings.add(line);
	}
	
	
	/**
	 * - convert string to Data Object (not convert in the addGEDString() to avoid try catch in main)
	 * - map family and individual id to real Data Object
	 */
	public void convertStringToObject()
	{
		for( String gedString : gedStrings ){
			addNewElement(gedString);
		}
		for( IndividualData individual : individuals){
			individual.mapIdWithData();
		}
		for( FamilyData family : families){
			family.mapIdWithData();
		}
		//System.out.println(otherData);
		
		/*for(int i=0;i<=individuals.size()-1;i++){
			System.out.println(individuals.get(i).id() +" , "+individuals.get(i).birth);
		}
		for(int i=0;i<=families.size()-1;i++){
			System.out.println(families.get(i).id());
		}*/
	}
	

	public IndividualData getIndividualDataFromId(String id){
		return individualIdMap.get(id);
	}
	public FamilyData getFamilyDataFromId(String id){
		return familyIdMap.get(id);
	}
	
	
	
	
	
	
	/**
	 * Convert string to BaseData
	 * Have to call in sequence of each line in GED file.
	 */
	private void addNewElement(String rawString){
		String elements[] = rawString.split(" ");
		if (elements[0].equals("0")) {
			//create data of INDI and FAM, that is always 3 elements
			if( elements.length >= 3 ){
				if (elements[2].equals("INDI")) {
					currentBaseData = new IndividualData(elements[1]); 
					individuals.add( (IndividualData)currentBaseData );
					individualIdMap.put(currentBaseData.id(), (IndividualData) currentBaseData);
				}else if(elements[2].equals("FAM")) {
					currentBaseData = new FamilyData(elements[1]);
					families.add( (FamilyData) currentBaseData);
					familyIdMap.put(currentBaseData.id(), (FamilyData) currentBaseData);
				}else{
					otherData += rawString+"\n";
				}
			}
		}else if ( Integer.parseInt( elements[0] ) >= 1) {
			//put information to each data 
			if( currentBaseData != null ){
				if( elements.length >= 3 ){
					String datas[] = Arrays.copyOfRange(elements, 2, elements.length);
					currentBaseData.addInfoByString(elements[1], String.join(" ", datas));
				}else{
					currentBaseData.addInfoByString(elements[1], "");
				}
			}else{
				otherData += rawString+"\n";
			}
		}
	}
	
	
	
	
	
	public static GEDData getInstance() {
	      if(instance == null) {
	         instance = new GEDData();
	      }
	      return instance;
	   }
	
	/**
	 * Print all Individuals
	 */
	public void printIndividuals() {
		String msg = getPrintFormattedIndividuals();
		System.out.println(msg);
	}
	
	public String getPrintFormattedIndividuals() {
		String printFormattedMsg = "******************\n"
            		      + "* Individuals\n\n"
            	  	      + "ID\t\tName\n";
            	  	  
		for (IndividualData individual : individuals) {
			
			printFormattedMsg += individual.id() + "\t\t" + individual.name + "\n";
		}
		
		return printFormattedMsg;
	}
	
	/**
	 * Print all Families
	 */
	public void printFamilies() {
		String msg = getPrintFormattedFamilies();
		System.out.println(msg);
	}
	
	public String getPrintFormattedFamilies() {
		
		String printFormattedMsg = "******************\n"
  		      					 + "* Families\n\n";
		
            	  	  
		for (FamilyData family : families) {
			
			StringBuffer famTableHeader = new StringBuffer("                                                                                                                                                                                                                                                                                                                                                                                                                                                             ");
	        StringBuffer famTableInfo   = new StringBuffer("                                                                                                                                                                                                                                                                                                                                                                                                                                                             ");
	        famTableHeader.insert(0, "Family ID");
	        famTableHeader.insert(15, "Husband ID");
	        famTableHeader.insert(30, "Husband Name");
	        famTableHeader.insert(45, "Wife ID");
	        famTableHeader.insert(60, "Wife Name");
	        

	        famTableInfo.insert(0, family.id());
	        
	        if(null != family.husband)
	        {
	        	famTableInfo.insert(15, family.husband.id());
	        	famTableInfo.insert(30, family.husband.name);
	        }
	        
	        if(null != family.wife)
	        {
	        	famTableInfo.insert(45, family.wife.id());
	        	famTableInfo.insert(60, family.wife.name);
	        }
	        
	        int index = 60;
	        int childNumber = 1;
//	        if( family.children.size()>0){
//	        IndividualData child = family.children.get(0);
//	        System.out.println("+++++++++++++++++++++++");
//	        System.out.println(child.name);
//	        System.out.println("+++++++++++++++++++++++");
	        for(int i=0; i<= family.children.size()-1; i++ )
	        {
	        	IndividualData child = family.children.get(i);
	        	index += 15;
	        	famTableHeader.insert(index, "Child" + childNumber + " ID");
	        	famTableInfo.insert(index, child.id());
	        	
	        	index += 15;
	        	famTableHeader.insert(index, "Child" + childNumber + " Name");
	        	famTableInfo.insert(index, child.name);
	        	
	        	childNumber++;
	        }
	        
			printFormattedMsg += famTableHeader + "\n" + famTableInfo + "\n\n";
		}
		
		return printFormattedMsg;
	}
	
}
