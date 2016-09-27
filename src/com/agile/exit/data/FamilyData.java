package com.agile.exit.data;

/**
 * @author exit
 *
 */
public class FamilyData extends BaseData {
	
	public String marriageEvent = "";
	public IndividualData wife;
	public IndividualData husband;
	public String childString = "";
	public String devorceEvent = "";
	public String date = "";
	private String husbandString = "";
	private String wifeString = "";
	
	public FamilyData(String id){
		super(id);
	}
	
	public void addInfoByString(String tagName, String data) {
		if(tagName.equals("MARR") ){
			marriageEvent = data;
		}else if(tagName.equals("HUSB")){
			husbandString = data;
		}else if(tagName.equals("WIFE")){
			wifeString = data;
		}else if(tagName.equals("CHIL")){
			childString = data;
		}else if(tagName.equals("DIV")){
			devorceEvent = data;
		}else if(tagName.equals("DATE")){
			date = data;
		}
		lastTagName = tagName;
	}
	
	public void mapIdWithData(){
		wife = GEDData.getInstance().getIndividualDataFromId(wifeString);
		husband = GEDData.getInstance().getIndividualDataFromId(husbandString);
	}
}
