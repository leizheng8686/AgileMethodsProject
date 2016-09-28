package LZ;

import java.io.*;

public class LeiUserStories {
	
	public LeiUserStories(){
		File input = new File("files_LZ/validInfos_lz.txt");
		File output = new File("files_LZ/errorsReport_lz.txt");
		BufferedReader reader = null;
		BufferedWriter writer = null;
		
		try{
			reader = new BufferedReader(new FileReader(input));
			writer = new BufferedWriter(new FileWriter(output));
			
		} catch(IOException e) {
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
	//Sprint 1
	//US08 : Birth before marriage of parents
	public void US08(File input){
		
	}
}
