package models.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public interface ListIO {
	@SuppressWarnings("unchecked")
	static <T extends Serializable> ArrayList<T> readFromFile(String fileName) {
		File booksFile = new File(fileName);
		ArrayList<T> data = null;
		
		if(booksFile.length() != 0) {		
			try(FileInputStream stream = new FileInputStream(booksFile)) {
				ObjectInputStream read = new ObjectInputStream(stream);
				data = (ArrayList<T>)(read.readObject());
				read.close();
			} catch(IOException | ClassNotFoundException ex) {
				System.out.println(ex);
			}
		}
		
		return data;
	}
	
	static <T extends Serializable> void writeToFile(String fileName, ArrayList<T> objects) {
		File booksFile = new File(fileName);
		
		try(FileOutputStream stream = new FileOutputStream(booksFile)) {
			ObjectOutputStream write = new ObjectOutputStream(stream);
			write.writeObject(objects);
			write.close();
		} catch(IOException ex) {
			System.out.println(ex);
		}
	}
}
