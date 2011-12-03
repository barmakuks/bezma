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
public class CommandFactory {

    public static Command parse(byte[] buffer) {
        if (buffer[0] == 2) {
            if (buffer[1] == 'P' && buffer[2] == 'M') {
                CommandPosition cmd = new CommandPosition();                
                cmd.getPosition().setCubePosition(Constants.CUBE_CRAWFORD);
                int index = 0;
                int i = 2;
                int number = 0;
                final int LEN = buffer.length;
                do {
                    i++;
                    if (buffer[i] >= '0' && buffer[i] <= '9') {
                        number = number * 10 + Integer.parseInt(Character.toString((char) (buffer[i])));
                    } else {

                        switch (index) {
                            case 0:
                            case 16:
                            case 17:
                                break;
                            case 14: // нажата кнопка у белого игрока
                                if (number != 0) {
                                    cmd.player = Constants.PLAYER_WHITE;
                                }
                                break;
                            case 15: // нажата кнопка у черного игрока
                                if (number != 0) {
                                    cmd.player = Constants.PLAYER_BLACK;
                                }
                                break;
                            case 13: // положение фишек на баре
                                int cnt = 0;
                                if((number & 0x1) != 0){ // кубик стоимости у черного игрока
                                    cmd.getPosition().setCubePosition(Constants.CUBE_BLACK);
                                }
                                if((number & 1024) != 0){ // кубик стоимости у белого игрока
                                    cmd.getPosition().setCubePosition(Constants.CUBE_WHITE);
                                }
                                if((number & 80) != 0){ // кубик стоимости в центре
                                    cmd.getPosition().setCubePosition(Constants.CUBE_CENTER);
                                }
                                // бар белых фишек ближе к смотрящему
                                for (int m = 2; m <= 8; m = m << 1) { // три поля для фишек на белом баре
                                    if ((number & m) != 0) {
                                        cnt++;
                                    }
                                }
                                cmd.getPosition().setChecker(24, cnt, Constants.PLAYER_NONE);
                                // бар черных фишек дальше от смотрящего
                                cnt = 0;
                                for (int m = 128; m <= 512; m = m << 1) { // три поля для фишек на черном баре
                                    if ((number & m) != 0) {
                                        cnt++;
                                    }
                                }
                                cmd.getPosition().setChecker(25, -cnt, Constants.PLAYER_NONE);

                                break;
                            default: // положения фишек на доске
                                cnt = 0;
                                for (int m = 1; m < 32; m = m << 1) {
                                    if ((number & m) != 0) {
                                        cnt++;
                                    }
                                }
                                cmd.getPosition().setChecker(index - 1 + 12, cnt, Constants.PLAYER_NONE);
                                cnt = 0;
                                for (int m = 32; m <= 1024; m = m << 1) {
                                    if ((number & m) != 0) {
                                        cnt++;
                                    }
                                }
                                cmd.getPosition().setChecker(12 - index , cnt, Constants.PLAYER_NONE);
                                break;
                        }

                        index++;
                        number = 0;
                    }

                } while (i < LEN && buffer[i] != 3);

                if (cmd.player != Constants.PLAYER_NONE) {
                    return cmd;
                } else {
                    return null;
                }
            }
        }
        return null;
    }
}
