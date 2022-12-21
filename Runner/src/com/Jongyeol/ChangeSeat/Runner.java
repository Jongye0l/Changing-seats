package com.Jongyeol.ChangeSeat;

import java.io.File;
import java.io.IOException;

public class Runner {
    public static void main(String[] args) throws IOException {
        File file = new File("Changing Seats");
        if(!file.exists() || !file.isDirectory()) new Downloader();
        else Runtime.getRuntime().exec("cmd /c java -jar \"Changing Seats/main.jar\"");
    }
}
