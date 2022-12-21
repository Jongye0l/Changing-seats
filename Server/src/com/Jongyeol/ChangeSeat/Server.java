package com.Jongyeol.ChangeSeat;

import com.Jongyeol.MultiplyServer.ServerTemplete;

import java.io.*;
import java.net.Socket;

public class Server implements ServerTemplete {
    File versionFile = new File("ChangeSeat/version.txt");
    @Override
    public void run(Socket socket) {
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            FileInputStream verIn = new FileInputStream(versionFile);
            int version = in.readInt();
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            int finalVersion = Integer.parseInt(new String(verIn.readAllBytes()));
            verIn.close();
            boolean b = finalVersion <= version;
            if(version == -1) {
                FileInputStream inp = new FileInputStream("ChangeSeat/main.jar");
                byte[] bytes = inp.readAllBytes();
                out.writeInt(bytes.length);
                out.write(bytes, 0, bytes.length);
                inp.close();
                inp = new FileInputStream("ChangeSeat/updater.jar");
                bytes = inp.readAllBytes();
                out.writeInt(bytes.length);
                out.write(bytes, 0, bytes.length);
                inp.close();
                inp = new FileInputStream("ChangeSeat/resource.zip");
                bytes = inp.readAllBytes();
                out.writeInt(bytes.length);
                out.write(bytes, 0, bytes.length);
                inp.close();
                in.close();
                out.close();
                return;
            }
            out.writeBoolean(b);
            if(b && in.readBoolean()) {
                FileInputStream inp = new FileInputStream("ChangeSeat/main.jar");
                byte[] bytes = inp.readAllBytes();
                out.writeInt(bytes.length);
                out.write(bytes);
                inp.close();
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getId() {
        return 0;
    }
}
