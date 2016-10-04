// readGEDCOM.java

import java.io.*;
import java.util.*;

import JF.jfUserStory;
import LZ.LeiUserStories;
import SS.ssonntagUserStories;

import com.agile.exit.EkkasitUserStories;
import com.agile.exit.data.GEDData;

public class readGEDCOM {

	// read a GEDCOM file and output as a .txt
	public void read(File proj1) {
		File proj3 = new File("P03.txt");
		File indi = new File("P03INDI.txt");
		File fam = new File("P03FAM.txt");
        File famTable = new File("P03FAMtable.txt");
		File output = new File("P03output.txt");
		BufferedReader reader = null;
		BufferedReader reader1 = null;
		BufferedReader reader2 = null;
		BufferedReader reader3 = null;
		BufferedReader reader4 = null;
		BufferedWriter writer = null;
		BufferedWriter writer1 = null;
		BufferedWriter writer2 = null;
		BufferedWriter writer3 = null;
        BufferedWriter writerFamTable = null;
		List<String> list0 = new ArrayList<>();
		list0.add("INDI");
		list0.add("FAM");
		list0.add("HEAD");
		list0.add("TRLR");
		list0.add("NOTE");
		List<String> list1 = new ArrayList<>();
		list1.add("NAME");
		list1.add("SEX");
		list1.add("BIRT");
		list1.add("DEAT");
		list1.add("FAMC");
		list1.add("FAMS");
		list1.add("MARR");
		list1.add("HUSB");
		list1.add("WIFE");
		list1.add("CHIL");
		list1.add("DIV");
		List<String> list2 = new ArrayList<>();
		list2.add("DATE");
		try {
			reader = new BufferedReader(new FileReader(proj1));
			writer = new BufferedWriter(new FileWriter(proj3));
			writer1 = new BufferedWriter(new FileWriter(indi));
			writer2 = new BufferedWriter(new FileWriter(fam));
            writerFamTable = new BufferedWriter(new FileWriter(famTable));
			String str = null;
            
            // Print ID/Name Header for print of individuals
            writer1.write("******************\r\n");
            writer1.write("* Individuals\r\n\n");
            writer1.write("ID\t\tName\r\n");
			while ((str = reader.readLine()) != null) {
				String elems[] = str.split(" ");
				
				//EKKASIT data
				GEDData.getInstance().addGEDString(str);
				
				if (elems[0].equals("0")) {
					for (int i = 1; i < elems.length; i++) {
						if (list0.contains(elems[i])) {
							writer.write(str + "\r\n");
						}
						if (elems[i].equals("INDI")) {
							writer1.write(elems[1] + " ");
						}

					}
				}
				if (elems[0].equals("1")) {
					if (list1.contains(elems[1])) {
						writer.write(str + "\r\n");
					}
					if (elems[1].equals("NAME")) {
						writer1.write("\t" + elems[2] + "\r\n");
					}
				}
				if (elems[0].equals("2")) {
					if (list2.contains(elems[1])) {
						writer.write(str + "\r\n");
					}
				}
			}
			reader.close();
			writer.close();
			writer1.close();

            // Print Header for print of families
            writerFamTable.write("\r\n******************\r\n");
            writerFamTable.write("* Families\r\n\r\n");
            
            // string array to save family entries for table-formatted print
            String familyId = "";
            String husbandData[] = {"", ""};
            String wifeData[] = {"", ""};
            StringBuffer famTableHeader = new StringBuffer("                                                ");
            StringBuffer famTableInfo   = new StringBuffer("                                                ");
            int ID_INDEX = 0;
            int NAME_INDEX = 1;
            boolean firstFamily = true;
            famTableHeader.insert(0, "Family ID");
            famTableHeader.insert(15, "Husband ID");
            famTableHeader.insert(30, "Husband Name");
            famTableHeader.insert(45, "Wife ID");
            famTableHeader.insert(60, "Wife Name");

			reader1 = new BufferedReader(new FileReader(proj1));
			String str1 = null;
			while ((str1 = reader1.readLine()) != null) {
				reader2 = new BufferedReader(new FileReader(indi));
				String elems1[] = str1.split(" ");
				if (elems1[0].equals("0")) {
					for (int i = 1; i < elems1.length; i++) {
						if (elems1[i].equals("FAM")) {
							writer2.write(elems1[1] + "\r");

                            if( false == firstFamily ) {
                                famTableInfo.insert(0, familyId);
                                famTableInfo.insert(15, husbandData[ID_INDEX]);
                                famTableInfo.insert(30, husbandData[NAME_INDEX]);
                                famTableInfo.insert(45, wifeData[ID_INDEX]);
                                famTableInfo.insert(60, wifeData[NAME_INDEX]);

                                writerFamTable.write(famTableHeader.toString() + "\r\n");
                                writerFamTable.write(famTableInfo.toString() + "\r\n");

                            }

                            firstFamily = false;
                            familyId = elems1[1];
                            husbandData[ID_INDEX] = "";
                            husbandData[NAME_INDEX] = "";
                            wifeData[ID_INDEX] = "";
                            wifeData[NAME_INDEX] = "";
                            famTableInfo   = new StringBuffer("                                                ");
						}
					}
				}
				if (elems1[1].equals("HUSB")) {
					String str2 = null;
					while ((str2 = reader2.readLine()) != null) {
						String elems2[] = str2.split(" ");
						if (elems1[2].equals(elems2[0])) {
							writer2.write(elems2[0] + " ");
							writer2.write(elems2[1] + "\r\n");
                            husbandData[ID_INDEX] = elems2[0];
                            husbandData[NAME_INDEX] = elems2[1];
							break;
						}
					}
				}
				if (elems1[1].equals("WIFE")) {
					String str3 = null;
					while ((str3 = reader2.readLine()) != null) {
						String elems3[] = str3.split(" ");
						if (elems1[2].equals(elems3[0])) {
							writer2.write(elems3[0] + " ");
							writer2.write(elems3[1] + "\r\n");
                            wifeData[ID_INDEX] = elems3[0];
                            wifeData[NAME_INDEX] = elems3[1];  
							break;
						}
					}
				}
				reader2.close();
			}

            // print last family
            famTableInfo.insert(0, familyId);
            famTableInfo.insert(15, husbandData[ID_INDEX]);
            famTableInfo.insert(30, husbandData[NAME_INDEX]);
            famTableInfo.insert(45, wifeData[ID_INDEX]);
            famTableInfo.insert(60, wifeData[NAME_INDEX]);
            
            writerFamTable.write(famTableHeader.toString() + "\r\n");
            writerFamTable.write(famTableInfo.toString() + "\r\n");

			reader1.close();
			reader2.close();
			writer2.close();
            writerFamTable.close();
            
            // Open Individual and Table-formatted Family file, merge into output
			reader3 = new BufferedReader(new FileReader(indi));
			reader4 = new BufferedReader(new FileReader(famTable));
			writer3 = new BufferedWriter(new FileWriter(output));
			String str4 = null;
			while ((str4 = reader3.readLine()) != null) {
				writer3.write(str4 + "\r");
			}
			String str5 = null;
			while ((str5 = reader4.readLine()) != null) {
				writer3.write(str5 + "\r");
			}
			reader3.close();
			writer3.close();
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
		
		GEDData.getInstance().convertStringToObject();
		
		// (easier) print of Individuals to console output
		GEDData gedData = GEDData.getInstance();
		gedData.printIndividuals();
		
		// (easer) print of families to console output
		gedData.printFamilies();
		
		//EKKASIT data
		EkkasitUserStories ekkasitUserStories = new EkkasitUserStories();
		ekkasitUserStories.getUs29();
		ekkasitUserStories.getUs30();
		
		//jf data
		jfUserStory jf =new jfUserStory();
		jf.getUs01();
		jf.getUs02();
		
		//lz data
		LeiUserStories lz = new LeiUserStories();
		lz.US08();
		lz.US10();
		
		// ss data
		ssonntagUserStories ss = new ssonntagUserStories();
		ss.US25();
		ss.US26();
	}

	public static void main(String args[]) {
		File file = new File("P01-LeiZheng.ged");
		readGEDCOM demo = new readGEDCOM();
		demo.read(file);
	}
}
