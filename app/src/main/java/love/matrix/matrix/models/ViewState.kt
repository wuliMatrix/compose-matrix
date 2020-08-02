package com.instagram.clone.models

enum class ViewState (val message: String){
    DEFAULT(""),
    LOADING("Now Loading"),
    EMPTY("Data not Found"),
    SUCCESS("Data has been success"),
    ERROR("Oops, Something is wrong")
}

