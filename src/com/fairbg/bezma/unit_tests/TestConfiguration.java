package com.fairbg.bezma.unit_tests;

import java.nio.file.Paths;

import com.fairbg.bezma.core.Configuration;

public class TestConfiguration extends Configuration
{
    private static final long serialVersionUID = 1L;

    @Override
    public String getDefaultUserDirectory()
    {
        return Paths.get(System.getProperty("user.home"), "bezma-files").toString();
//        return new JFileChooser().getFileSystemView().getHomeDirectory().toString();
    }
}
