package com.convertapi.examples;

import com.convertapi.Config;
import com.convertapi.ConversionResult;
import com.convertapi.ConvertApi;
import com.convertapi.Param;
import com.convertapi.model.User;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Retrieve user information
 * https://www.convertapi.com/doc/user
 */

public class UserInformation {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Config.setDefaultSecret("YOUR API SECRET");    //Get your secret at https://www.convertapi.com/a
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