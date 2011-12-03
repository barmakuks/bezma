/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fairbg.core;

import com.fairbg.io.bluetooth.BluetoothCommander;
import com.fairbg.core.commands.ICommander;

/**
 * Класс-создатель. Создает и владеет объектами
 * Corel
 * Display
 * BluetoothCommander.
 * Устанавливает связи между этими объектами
 * @author Vitalik
 */
public class Demiurge {
    private Corel _corel = null;
    private ICommander _usb_commander = null;
    private static Demiurge _demiurge = new Demiurge();
    
    private Demiurge(){
        _corel = new Corel();
        _usb_commander = new BluetoothCommander(_corel);
        _corel.setCommander(_usb_commander);
    }
    
    /**
     * Возвращает статический обект Corel
     * @return 
     */
    public static Corel getCorel(){
        return _demiurge._corel;
    }
}
