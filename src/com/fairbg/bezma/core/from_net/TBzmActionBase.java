package com.fairbg.bezma.core.from_net;

import com.fairbg.bezma.core.from_net.enums.Actions;

public abstract class TBzmActionBase {
    protected Actions m_ActionType;
    protected String m_PositionString = "";
    public abstract String[] ToSgfString();
    public abstract String ToJFString();
    public abstract boolean ParseSgfString(String aStr);
}
