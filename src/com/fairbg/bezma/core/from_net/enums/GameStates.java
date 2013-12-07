package com.fairbg.bezma.core.from_net.enums;

/**Константы описываю текущее состояние игры*/
public enum GameStates {
    /// Ожидание начала партии
    None,      
    /// Ожидание следующей партии
    NextGame,         
    /// в начале партии выбор активного игрока
    PlayerSelecting,  
    /// Ожидание кнопки Roll
    Roll,             
    /// Ожидание ввода броска
    Dice,             
    /// Ожидание ввода первого в партии броска
    FirstDice,        
    /// Ожидание подтверждения увеличения ставки с возможностью beaver
    DoubleAcceptingBeaver,
    /// Ожидание подтверждения Double
    DoubleConfirm,    
    /// Ожидание подтверждения увеличения ставки без beaver
    DoubleAccepting,  
    /// Ожидание подтверждения Take
    TakeConfirm,      
    /// Ожидание подтверждения Double-Pass
    PassConfirm,      
    /// Ожидание подтверждения повторного увеличения ставки
    BeaverAccepting,  
    /// Ожидание хода
    MoveMaking,       
    /// Ожидание подтверждения хода
    MoveAccepting,    
    /// Подтверждение хода, при пропуске своего из-за засады на баре
    SkipMoveAccepting,
    /// Ожидание подтверждения хода кнопкой Ok
    MoveAcceptingOk,  
    /// Ожидание подтверждения последнего хода в партии
    LastMoveAccepting,
    /// Ожидание выбора типа сдачи
    PassTypeSelecting,
  //Ожидание подтверждение обычной сдачи
    PassNormalAccepting,
  //Ожидание подтверждение сдачи Gammon
    PassGammonAccepting,
    /// Ожидание подтверждения сдачи соперника    /// Obsolette
    PassAccepting,    
    /// Процесс расстановки шашек
    InitBoard,        
    /// Игра завершена, разрешен только просмотр матча
    LookMode          
}
