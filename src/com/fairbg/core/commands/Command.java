package com.fairbg.core.commands;

import com.fairbg.core.Constants;

/** Базовый класс для команды */
public class Command {

    public int commandType = Constants.CMD_NONE;
    public int player = Constants.PLAYER_NONE;

    @Override
    public String toString() {
        switch (player) {
            case Constants.PLAYER_BLACK:
                return "BLACK";
            case Constants.PLAYER_WHITE:
                return "WHITE";
            default:
                return "NONE";
        }
    }
}
