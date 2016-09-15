import java.io.*;
import java.util.*;

public class readGEDCOM {

	// read a GEDCOM file and output as a .txt
	public void read(File proj1) {
		File proj3 = new File("P03.txt");
		BufferedReader reader = null;
		BufferedWriter writer = null;
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
			String str = null;
			System.out.println("it works");
			while ((str = reader.readLine()) != null) {
				String elems[] = str.split(" ");
				if (elems[0].equals("0")) {
					for (int i = 1; i < elems.length; i++) {
						if (list0.contains(elems[i])) {
							writer.write(str + "\r\n");
						}
					}
				}
				if (elems[0].equals("1")) {
					if (list1.contains(elems[1])) {
						writer.write(str + "\r\n");
						
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

	public static void main(String args[]) {
		// File tags = new File("Tags.txt");
		File file = new File("P01-LeiZheng.ged");
		readGEDCOM demo = new readGEDCOM();
		demo.read(file);
	}
}
