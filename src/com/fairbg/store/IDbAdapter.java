/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fairbg.store;

/**
 *
 * @author Vitalik
 */
public interface IDbAdapter {

    /**
     * закрывает базу данных
     */
    void close();

    /**
     * получаем из БД параметро типа Boolean
     */
    boolean getBoolean(String key, boolean defaultValue);

    /**
     * получаем из БД параметро типа Integer
     */
    int getInteger(String key, int defaultValue);

    /**
     * получаем из БД параметро типа String
     */
    String getString(String key, String defaultValue);

    /**
     * открывает базу данных
     */
    IDbAdapter open();

    /**
     * записывает в БД параметр типа String
     */
    void putValue(String key, String value);

    /**
     * записывает в БД параметр типа Boolean
     */
    void putValue(String key, boolean value);

    /**
     * записывает в БД параметр типа Integer
     */
    void putValue(String key, int value);
    
}
