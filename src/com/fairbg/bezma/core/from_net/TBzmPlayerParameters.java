package com.fairbg.bezma.core.from_net;

public class TBzmPlayerParameters {
    public String Name;
    public int Id = 0;
    public byte GameWins;
    public byte Points;
    public void Clear()
    {
        GameWins = 0; Points = 0; Name = ""; Id = 0;
    }
    public TBzmPlayerParameters()
    {
        Clear();
    }
    public void ClearResuts()
    {
        GameWins = 0; Points = 0;
    }
    public void Copy(TBzmPlayerParameters aCopy) 
    {
        Name = aCopy.Name;
        GameWins = aCopy.GameWins;
        Points = aCopy.Points;
        Id = aCopy.Id;
    }

}
