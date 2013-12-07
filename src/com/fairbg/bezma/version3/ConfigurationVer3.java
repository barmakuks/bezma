package com.fairbg.bezma.version3;

import com.fairbg.bezma.core.Configuration;

/** Конфигурация используемая в 3-й версии BEZMA */
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
//    private String m_deviceMAC = "00:12:6F:22:46:2C"; 
//    private String m_deviceMAC = "00:12:6F:22:45:98";
    
}