package com.blueman.scanwithit.qrcode.ui.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blueman.scanwithit.qrcode.models.UserData;

import java.util.Calendar;

public class HomeViewModel extends ViewModel {


    private MutableLiveData<String> mText;
    private String currentMessage;


    public HomeViewModel() {
        mText = new MutableLiveData<>();

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay < 12){
            currentMessage = "Beautiful day! Good Morning, ";
        }else if(timeOfDay < 16){
            currentMessage = "Have an awesome Afternoon, ";
        }else if(timeOfDay < 21){
            currentMessage = "Have a great Evening, ";
        }else {
            currentMessage = "It was a beautiful day. Good Night, ";
        }
        mText.setValue(currentMessage);
    }

    public LiveData<String> getText() {
        return mText;
    }
}