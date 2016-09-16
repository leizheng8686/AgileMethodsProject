import java.io.*;
import java.util.*;

public class readGEDCOM {

	// read a GEDCOM file and output as a .txt
	public void read(File proj1) {
		File proj3 = new File("P03.txt");
		File indi = new File("P03INDI.txt");
		File fam = new File("P03FAM.txt");
		BufferedReader reader = null;
		BufferedReader reader1 = null;
		BufferedReader reader2 = null;
		BufferedWriter writer = null;
		BufferedWriter writer1 = null;
		BufferedWriter writer2 = null;
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
			String str = null;
			System.out.println("it works");
			while ((str = reader.readLine()) != null) {
				String elems[] = str.split(" ");
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
						writer1.write(elems[2] + "\r\n");
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
			reader1 = new BufferedReader(new FileReader(proj1));
			String str1 = null;
			while ((str1 = reader1.readLine()) != null) {
				reader2 = new BufferedReader(new FileReader(indi));
				String elems1[] = str1.split(" ");
				if (elems1[0].equals("0")) {
					for (int i = 1; i < elems1.length; i++) {
						if (elems1[i].equals("FAM")) {
							writer2.write(elems1[1] + "\r");
						}
					}
				}
				if (elems1[1].equals("HUSB")) {
					String str2 = null;
					while ((str2 = reader2.readLine()) != null) {
						String elems2[] = str2.split(" ");
						if (elems1[2].equals(elems2[0])) {
							writer2.write(elems2[0] + " ");
							writer2.write(elems2[1] + "\r");
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
							writer2.write(elems3[1] + "\r");
							break;
						}
					}
				}
				reader2.close();
			}
			reader1.close();
			reader2.close();
			writer2.close();
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
		File file = new File("P01-LeiZheng.ged");
		readGEDCOM demo = new readGEDCOM();
		demo.read(file);
	}
}
