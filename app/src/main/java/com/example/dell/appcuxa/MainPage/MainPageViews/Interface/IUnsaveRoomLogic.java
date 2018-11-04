package com.example.dell.appcuxa.MainPage.MainPageViews.Interface;

import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;
import com.example.dell.appcuxa.ObjectModels.SavedRoom;

public interface IUnsaveRoomLogic {
        public void unSaveRoom(RoomSearchItem room);
        public void backToSavedScreen(RoomSearchItem savedRoom);

}
