package com.example.dell.appcuxa.MainPage.MainPageViews.Interface;

import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;

public interface ILogicSaveRoom {
    public void saveRoom(RoomSearchItem room);
    public void unSaveRoom(RoomSearchItem room);
    public void backToScreen(RoomSearchItem room);
}
