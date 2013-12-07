package com.fairbg.bezma.core.from_net.enums;

/**Константы описывают состояния игроков во время игры*/
public enum PlayerStates {
	/// все клавиши не активны (перед началом игры), активны Start и Pos
    None,    
    /// активный игрок еще не выбран
    PlayerNotSelected,
    /// игрок ожидает подтверждения своего хода
    WaitForMoveAccept,
    /// игрок не имеет хода (может только сдаться)
    Inactive,         
    /// игрок имеет кубик стоимости и может бросить кости
    DiceOrDouble,     
    /// игрок вводит первую в партии пару значений костей
    FirstDice,        
    /// игрок вводит значение костей
    Dice,             
    /// игрок получает кости с сервера
    Roll,             
    /// игрок подтверждает Double
    DoubleConfirm,    
    /// игрок принимает повышение ставки с возможностью beaver или пасует
    DoubleAcceptingBeaver,  
    /// игрок принимает повышение ставки или пасует
    DoubleAccepting,  
    /// игрок нажал и должен подтвердить Take
    TakeConfirm,      
    /// при Double игрок нажал должен подтвердить Pass
    PassConfirm,      
    /// игрок принимает обратное повышение ставки или пасует
    BeaverAccepting,  
    /// игрок совершает ход
    MoveMaking,       
    /// игрок совершил неправильный ход
    WrongMoveMade,    
    /// игрок подтверждает ход соперника
    MoveAccepting,    
    /// игрок подтверждает ход соерника и выполняет свой ход
    MoveAcceptingOk,  
    /// игрок подтверждает свой проигрыш
    LastMoveAccepting,
    /// игрок победил в предыдущей   партии
    Winner,           
    /// игрок проиграл в предыдущей партии
    Loser,            
    /// игрок выставляет свои шашки
    InitBoard,        
    /// все клавиши не активны, разрешен только просмотр матча
    Look,             
    /// игрок выбирает как он хочет сдаться
    PassTypeSelect,   
    /// игрок подтверждает обычную сдачу
    PassNormalAccept, 
    /// игрок подтверждает сдачу Gammon
    PassGammonAccept, 
    /// подтверждение сдачи соперника // obsolette
    PassAccept,       
    /// игрок ожидает подтверждения своей сдачи
    WaitForPassAccept,
    /// игрок не может ходить, так как застрял на баре, но ход соперника подтвердить должен
    SkipMoving        
}
