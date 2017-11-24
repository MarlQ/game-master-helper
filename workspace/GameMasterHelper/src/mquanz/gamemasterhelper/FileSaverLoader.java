package mquanz.gamemasterhelper;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class FileSaverLoader {

	public static void saveData(String s, GeneralInformation generalInformation) throws java.io.FileNotFoundException {
		String path = s + "\\" + generalInformation.name + ".dat";
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
			out.writeObject(generalInformation);
			//GUILog.createLog("Serialized data was saved to " + path);
		} catch (java.io.FileNotFoundException i) {
			throw new java.io.FileNotFoundException();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	public static GeneralInformation loadData(String s) {
		if (s.endsWith(".dat")) {
			GeneralInformation generalInformation;
			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(s))) {

				generalInformation = (GeneralInformation) in.readObject();

			} catch (IOException i) {
				i.printStackTrace();
				return null;
			} catch (ClassNotFoundException c) {
				System.out.println("Game class not found");
				c.printStackTrace();
				return null;
			}
			return generalInformation;
		} else
			return null;

	}
}
