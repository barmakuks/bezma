/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fairbg.core.commands;

import com.fairbg.core.Constants;
import com.fairbg.core.Position;

/**
 *
 * @author Vitalik
 */
public class CommandPosition extends Command{
    private Position _position = new Position();
    public CommandPosition(){
        commandType = Constants.CMD_POSITION;
    }
    public Position getPosition(){
        return _position;
    }
}
