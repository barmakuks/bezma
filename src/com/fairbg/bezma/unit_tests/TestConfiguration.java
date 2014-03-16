package com.fairbg.bezma.unit_tests;

import java.io.File;

import android.os.Environment;

import com.fairbg.bezma.core.Configuration;

public class TestConfiguration extends Configuration
{
    private static final long serialVersionUID = 1L;

    @Override
    public String getDefaultUserDirectory()
    {
        File sdCard = Environment.getExternalStorageDirectory();
        return sdCard.getAbsolutePath() + "/bezma-files";
    }
}
