package com.techrev.analytics

class Analytics () {

    companion object {
        fun authenticateUser(mAuthKey : String) : String{
            println("User auth key: $mAuthKey");
            return "Your auth key $mAuthKey is authenticated successfully!"
        }
    }

}