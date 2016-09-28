package LZ;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class extractInfos {
	public void extractGEDCOM(File file){
		File output = new File("files_LZ/validInfos_lz.txt");
		BufferedReader reader = null;
		BufferedWriter writer = null;
		//add tags for different level
		List<String> list0 = new ArrayList<>();
		list0.add("INDI"); //0
		list0.add("FAM");  //1
		List<String> list1 = new ArrayList<>();
		//individual info
		list1.add("NAME"); //0
		list1.add("SEX");  //1
		list1.add("BIRT"); //2
		list1.add("DEAT"); //3
		//family info
		list1.add("MARR"); //4
		list1.add("DIV");  //5
		list1.add("HUSB"); //6
		list1.add("WIFE"); //7
		list1.add("CHIL"); //8
		List<String> list2 = new ArrayList<>();
		list2.add("DATE");

		try{
			reader = new BufferedReader(new FileReader(file));
			writer = new BufferedWriter(new FileWriter(output));
			
			String str = null;
			
			//Individuals
			writer.write("INDIVIDUALS\r\nID\tName\t\tGender\tBirth\t\tAlive\tDeath\r\n");
			while ((str = reader.readLine()) != null) {
				String elems[] = str.split(" ");
				if(elems.length > 2){
					if (elems[0].equals("0")) {
						//get individual info
						if (elems[2].equals("INDI")) {
							StringBuilder sb = new StringBuilder();
							//add ID
							sb.append(elems[1]+ "\t");   
							// add full name
							str = reader.readLine();
							String[] name = str.split(" ");   
							sb.append(name[2] + " " + name[3] + "\t");  
							//skip invalid info
							str = reader.readLine();  
							str = reader.readLine();
							str = reader.readLine();
							str = reader.readLine();
							// add sex
							String[] sex = str.split(" ");
							sb.append(sex[2] + "\t");  
							// add DOB
							str = reader.readLine();
							str = reader.readLine();
							String[] dob = str.split(" ");
							sb.append(Month_EnToNum(dob[3]) + "/" + dob[2] + "/" + dob[4] + "\t");
							// add DOD
							str = reader.readLine();
							if(str.startsWith("1 DEAT")){
								str = reader.readLine();
								String[] dod = str.split(" ");
								sb.append("False\t" + Month_EnToNum(dod[3]) + "/" + dod[2] + "/" + dod[4] + "\t");
							}else{
								sb.append("True\tNone");
							}
							//System.out.println(sb.toString());  //test
							writer.write(sb.toString() + "\r\n");
						}
						//get family info
						if (elems[2].equals("FAM")) {
							
						}
					}
				}
			}
			writer.newLine();
			reader = new BufferedReader(new FileReader(file));
			//Families
			writer.write("FAMILIES\r\nID\tMarried\t\tDivorced\tHusband\tWife\tChildren\r\n");
			while ((str = reader.readLine()) != null) {
				System.out.println(str);  //test
				String elems[] = str.split(" ");
				if(elems.length > 2){
					if (elems[0].equals("0")) {
						//get family info
						if (elems[2].equals("FAM")) {
							StringBuilder sb = new StringBuilder();
							//add ID
							sb.append(elems[1]+ "\t");   
							// store husband id
							str = reader.readLine();
							String[] husbId = str.split(" ");   
							// store wife id
							str = reader.readLine();  
							String[] wifeId = str.split(" ");
							// store children ids
							String chilId = new String();
							str = reader.readLine();
							while(str.startsWith("1 CHIL")){
								String[] chil = str.split(" ");
								chilId += chil[2]+" ";
								str = reader.readLine();
							}
							// add married date
							str = reader.readLine();
							String[] marr = str.split(" ");
							sb.append(Month_EnToNum(marr[3]) + "/" + marr[2] + "/" + marr[4] + "\t");
							// add divorced date
							str = reader.readLine();
							if(str.startsWith("1 DIV")){
								str = reader.readLine();
								String[] div = str.split(" ");
								sb.append( Month_EnToNum(div[3]) + "/" + div[2] + "/" + div[4] + "\t");
							}else{
								sb.append("None\t\t");
							}
							// add husband, wife, children IDs
							sb.append(husbId[2] + "\t" + wifeId[2] + "\t" + chilId);
							//System.out.println(sb.toString());  //test
							writer.write(sb.toString() + "\r\n");
						}

					}
				}
			}
			reader.close();
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null || writer != null) {
				try {
					reader.close();
					writer.close();
				} catch (IOException e1) {
				}
			}
		}
	}
	
	public int Month_EnToNum(String s){
		if(s.equals("JAN"))
			return 1;
		if(s.equals("FEB"))
			return 2;
		if(s.equals("MAR"))
			return 3;
		if(s.equals("APR"))
			return 4;
		if(s.equals("MAY"))
			return 5;
		if(s.equals("JUN"))
			return 6;
		if(s.equals("JUL"))
			return 7;
		if(s.equals("AUG"))
			return 8;
		if(s.equals("SEP"))
			return 9;
		if(s.equals("OCT"))
			return 10;
		if(s.equals("NOV"))
			return 11;
		if(s.equals("DEC"))
			return 12;
		return 0;
	}
	
	public static void main(String args[]){
		File input = new File("files_LZ/test092616.ged");
		extractInfos ei = new extractInfos();
		ei.extractGEDCOM(input);
		System.out.println("Successful!");
	}
}
