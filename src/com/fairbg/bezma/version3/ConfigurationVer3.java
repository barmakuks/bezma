package com.fairbg.bezma.version3;

import java.io.File;

import android.os.Environment;

import com.fairbg.bezma.core.Configuration;

/** This configuration is used in BEZMA ver. 3*/
public class ConfigurationVer3 extends Configuration
{
    private static final long serialVersionUID = -2509360009329099420L;

    public UserSettings getUserSettings()
    {
		return m_UserSettings;
    }

    public void setUserSettings(UserSettings settings)
    {
		m_UserSettings = settings;
    }
    
	UserSettings m_UserSettings;
	
	@Override
	public String getDefaultUserDirectory()
	{
        File sdCard = Environment.getExternalStorageDirectory();
        return sdCard.getAbsolutePath() + "/bezma-files";
	}
//    private String m_deviceMAC = "00:12:6F:22:46:2C"; 
//    private String m_deviceMAC = "00:12:6F:22:45:98";

    @Override
    public String getUnfinishedMatchPath()
    {
        File sdCard = Environment.getExternalStorageDirectory();
        return sdCard.getAbsolutePath() + "/bezma-files/.unfunished";
    }
    
}