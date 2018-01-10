package mquanz.gamemasterhelper;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

abstract class FileSaverLoader {

	 static void saveData(String s, CampaignInformation campaignInformation) throws java.io.FileNotFoundException {
		String path = s + "\\" + campaignInformation.name + ".dat";
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
			out.writeObject(campaignInformation);
			//GUILog.createLog("Serialized data was saved to " + path);
		} catch (java.io.FileNotFoundException i) {
			throw new java.io.FileNotFoundException();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	 static CampaignInformation loadData(String s) {
		if (s.endsWith(".dat")) {
			CampaignInformation campaignInformation;
			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(s))) {

				campaignInformation = (CampaignInformation) in.readObject();

			} catch (IOException i) {
				i.printStackTrace();
				return null;
			} catch (ClassNotFoundException c) {
				System.out.println("Game class not found");
				c.printStackTrace();
				return null;
			}
			return campaignInformation;
		} else
			return null;

	}
}
