package com.agile.exit.data;

import java.util.Date;

/**
 * @author exit
 *
 */
public class IndividualData extends BaseData {
	public String name = "";
	public String sex = "";
	public Date birth;
	public Date dateOfDeath;
	public FamilyData familyAsChild;
	public FamilyData familyAsSpouse;
	private String familyAsChildString = "";
	private String familyAsSpouseString = "";
	
	public IndividualData(String id){
		super(id);
	}
	
	public void addInfoByString(String tagName, String data) {
//		System.out.println(tagName);
		if(tagName.equals("NAME")){
			name = data;
		}else if(tagName.equals("SEX")){
			sex = data;
		}else if(tagName.equals("DATE") ){
			if( lastTagName.equals("BIRT")){
				birth = convertStringToDate(data);
			}else if( lastTagName.equals("DEAT") ){
				dateOfDeath = convertStringToDate(data);
			}
		}else if(tagName.equals("FAMC")){
			familyAsChildString = data;
		}else if(tagName.equals("FAMS")){
			familyAsSpouseString = data;
		}
		lastTagName = tagName;
	}
	
	public void mapIdWithData(){
		familyAsChild = GEDData.getInstance().getFamilyDataFromId(familyAsChildString);
		familyAsSpouse = GEDData.getInstance().getFamilyDataFromId(familyAsSpouseString);
	}
	
}
