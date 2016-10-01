package com.agile.exit.data;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author exit
 *
 */
public class FamilyData extends BaseData {
	
	public Date marriageDate;
	public Date divorceDate;
	public IndividualData wife;
	public IndividualData husband;
	
	public ArrayList<String> childrenStrings = new ArrayList();
	
	private String husbandString = "";
	private String wifeString = "";
	
	public FamilyData(String id){
		super(id);
	}
	
	public void addInfoByString(String tagName, String data) {
		if(tagName.equals("HUSB")){
			husbandString = data;
		}else if(tagName.equals("WIFE")){
			wifeString = data;
		}else if(tagName.equals("CHIL")){
			childrenStrings.add(data);  
		}else if(tagName.equals("DATE")){
			if( lastTagName.equals("DIV")){
				divorceDate = convertStringToDate(data);
			}else if(lastTagName.equals("MARR") ){
				marriageDate = convertStringToDate(data);
			}
		}
		lastTagName = tagName;
	}
	
	public void mapIdWithData(){
		wife = GEDData.getInstance().getIndividualDataFromId(wifeString);
		husband = GEDData.getInstance().getIndividualDataFromId(husbandString);
	}
}
