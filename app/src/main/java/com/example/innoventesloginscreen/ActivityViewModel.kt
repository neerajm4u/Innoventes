package com.example.innoventesloginscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ActivityViewModel: ViewModel() {
     var isAllDataValid : MutableLiveData<ArrayList<Boolean>> = MutableLiveData()

}