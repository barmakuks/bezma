package com.fairbg.bezma.unit_tests;

import com.fairbg.bezma.core.Configuration;

public class TestConfiguration extends Configuration
{
    private static final long serialVersionUID = 1L;

    @Override
    public String getDefaultUserDirectory()
    {
//        File sdCard = Environment.getExternalStorageDirectory();
//        return sdCard.getAbsolutePath() + "/bezma-files";
        return "bezma-files";
    }

    @Override
    public String getUnfinishedMatchPath()
    {
        return "/home/vitalii/development/java/bezma/.unfinished";
    }
    
}
