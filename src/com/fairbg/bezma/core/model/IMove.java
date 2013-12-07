package com.fairbg.bezma.core.model;

public interface IMove
{
    public void accept(IMoveVisitor visitor);
}
