package com.example.miraclepack;

public class SessionManager
{
    private static boolean isLoggedIn = false;

    public static boolean checkIfLoggedIn()
    {
        return isLoggedIn;
    }

    public static void setLoggedIn(boolean loggedIn)
    {
        isLoggedIn = loggedIn;
    }
}
