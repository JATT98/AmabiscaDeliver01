package com.example.amabiscadeliver.Connect;


import android.content.Context;

public class ManagementPhone {

    private Context context;

    public ManagementPhone(Context context) {
        this.context = context;
    }

    public void clickNumber(ClickButtonListener clickButtonListener) {
        clickButtonListener.Click();
    }


}
