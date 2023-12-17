package dal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class DbTable<T extends Serializable> {
	private final String FILE_NAME;
	private File tableFile;
	
	DbTable(String dataDirPath, Class<T> model) {
		this(dataDirPath, model.getSimpleName());
	}
	
	DbTable(String dataDirPath, String fileName) {
		try {
			Files.createDirectories(Paths.get(dataDirPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		FILE_NAME = fileName;
		tableFile = new File(dataDirPath, FILE_NAME);
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
		if(data.size() == 0) {
			try(PrintWriter writer = new PrintWriter(tableFile)) {
				writer.print("");
			} catch (FileNotFoundException e) {
				// Error won't be thrown
				e.printStackTrace();
			}
			
			return;
		}
		
		try(FileOutputStream fileOutputStr = new FileOutputStream(tableFile);
			ObjectOutputStream objOutputStr = new ObjectOutputStream(fileOutputStr)) {
			objOutputStr.writeObject(data);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
