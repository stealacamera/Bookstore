package dal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class DbTable<T extends Serializable> {
	private final String FILE_NAME;
	private File tableFile;
	
	DbTable(Class<T> model) {
		this(model.getSimpleName());
	}
	
	DbTable(String fileName) {
		String dir = "user_data/data";
		
		try {
			Files.createDirectories(Paths.get(dir));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FILE_NAME = fileName;
		tableFile = new File(dir, FILE_NAME);
	}
			
	@SuppressWarnings("unchecked")
	List<T> readTable() {		
		List<T> data = new ArrayList<T>();
		
		if(tableFile.length() != 0) {
			try(FileInputStream fileInputStr = new FileInputStream(tableFile);
				ObjectInputStream objInputStr = new ObjectInputStream(fileInputStr)	) {				
				data = (ArrayList<T>) (objInputStr.readObject());
				objInputStr.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return data;
	}
	
	void saveToTable(List<T> data) {
		if(data.size() == 0)
			return;
		
		try(FileOutputStream fileOutputStr = new FileOutputStream(tableFile);
			ObjectOutputStream objOutputStr = new ObjectOutputStream(fileOutputStr);) {
			objOutputStr.writeObject(data);
			objOutputStr.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
