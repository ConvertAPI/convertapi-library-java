package com.convertapi.examples;

import com.convertapi.client.Config;
import com.convertapi.client.ConvertApi;
import com.convertapi.client.model.User;

import static java.lang.System.getenv;

/**
 * Retrieve user information
 * https://www.convertapi.com/doc/user
 */
public class UserInformation {

    public static void main(String[] args) {
        Config.setDefaultSecret(getenv("CONVERTAPI_SECRET"));    //Get your secret at https://www.convertapi.com/a
        User user = ConvertApi.getUser();

        System.out.println("API Key: " + user.ApiKey);
        System.out.println("Secret: " + user.Secret);
        System.out.println("Email: " + user.Email);
        System.out.println("Name: " + user.FullName);
        System.out.println("Status: " + user.Status);
        System.out.println("Active: " + user.Active);
        System.out.println("Seconds left: " + user.SecondsLeft);
    }
}
