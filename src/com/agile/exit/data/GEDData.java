package com.agile.exit.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author exit
 *
 */
public class GEDData {
	private static GEDData instance = null;
	
	private String otherData = "";
	
	private BaseData currentBaseData;
	private ArrayList<String> gedStrings = new ArrayList<String>();
	
	public ArrayList<IndividualData> individuals = new ArrayList<IndividualData>();
	public ArrayList<FamilyData> families = new ArrayList<FamilyData>();
	
	public Map<String,IndividualData> individualIdMap = new HashMap<String,IndividualData>();
	public Map<String,FamilyData> familyIdMap = new HashMap<String,FamilyData>();
	
	
	/**
	 * Collect all string first to avoid try catch in the main file.
	 */
	public void addGEDString(String line){
		gedStrings.add(line);
	}
	
	
	/**
	 * - convert string to Data Object (not convert in the addGEDString() to avoid try catch in main
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
}
