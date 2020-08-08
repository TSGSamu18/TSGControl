package tsg.file;

import java.io.File;
import java.io.IOException;

import tsg.main.TSGControl;

public class FileManager {

	public File save;
	
	public void setup() {
		TSGControl.instance.saveDefaultConfig();
	    save = new File(TSGControl.instance.getDataFolder(), "save.data");
	    if(!save.exists()) {
	    	try {
				save.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}
}
