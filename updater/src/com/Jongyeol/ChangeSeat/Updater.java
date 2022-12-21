package com.Jongyeol.ChangeSeat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

public class Updater {
    public static void main(String[] args) throws IOException, InterruptedException {
        File file = new File("Changing Seats/main2.jar");
        File file2 = new File("Changing Seats/main.jar");
        Thread.sleep(1000);
        Files.copy(file.toPath(), file2.toPath());
        Runtime.getRuntime().exec("cmd /c java -jar \"Changing Seats/main.jar\"");
        System.exit(0);
    }
}
