package com.agile.exit.data;

import java.util.ArrayList;
import java.util.Date;


/**
 * @author exit
 *
 */
public class IndividualData extends BaseData {
	public String name = "";
	public String givnName = "";
	public String surnName = "";
	public String sex = "";
	public Date birth;
	public Date dateOfDeath;
	public FamilyData familyAsChild;
	public ArrayList<FamilyData> familiesAsSpouse = new ArrayList<FamilyData>();
	private String familyAsChildString = "";
	private ArrayList<String> familiesAsSpouseString = new ArrayList<String>();
	
	public IndividualData(String id){
		super(id);
	}
	
	public void addInfoByString(String tagName, String data) {
//		System.out.println(tagName);
		if(tagName.equals("NAME")){
			name = data;
		}if(tagName.equals("GIVN")){
			givnName = data;
		}if(tagName.equals("SURN")){
			surnName = data;
		}else if(tagName.equals("SEX")){
			sex = data;
		}else if(tagName.equals("DATE") ){
			if( lastTagName.equals("BIRT")){
				if(birth!=null){
					errors.add(DataError.ERROR_MULTIPLE_BIRTH);
				}
				birth = convertStringToDate(data);
			}else if( lastTagName.equals("DEAT") ){
				dateOfDeath = convertStringToDate(data);
			}
		}else if(tagName.equals("FAMC")){
			familyAsChildString = data;
		}else if(tagName.equals("FAMS")){
			familiesAsSpouseString.add(data);
		}
		lastTagName = tagName;
	}
	
	public void mapIdWithData(){
		familyAsChild = GEDData.getInstance().getFamilyDataFromId(familyAsChildString);
		for( String familyAsSpouseString : familiesAsSpouseString ){
			familiesAsSpouse.add( GEDData.getInstance().getFamilyDataFromId(familyAsSpouseString) );
		}
	}
	
	// retrieve the spouse's id for this individual, or and empty string if no match
	public String getSpouseId(){
		String spouseId = "";
		for(FamilyData family : familiesAsSpouse) {
			if(null != family.husband && null != family.wife)
			{
				if(id().compareTo(family.husband.id()) == 0 )
				{
					spouseId = family.wife.id();
				}
				else if(id().compareTo(family.wife.id()) == 0 )
				{
					spouseId = family.husband.id();
				}
			}
		}
		return spouseId;
	}
	
}
