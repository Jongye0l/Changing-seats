package com.Jongyeol.ChangeSeat;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class UpdateChecker extends Thread {
    private boolean lastedUpdate;
    private final int version;
    public UpdateChecker(int version) {
        this.version = version;
        start();
    }
    @Override
    public void run() {
        try {
            Socket socket = new Socket("Jongyeol.kro.kr", 1209);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeByte(0);
            out.writeInt(version);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            lastedUpdate = in.readBoolean();
            if(!lastedUpdate) {
                int result = JOptionPane.showConfirmDialog(Main.main, "새로운 업데이트가 존재합니다. 업데이트를 진행할까요?", "업데이트 알림", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION) {
                    out.writeBoolean(true);
                    int size = in.readInt();
                    byte[] datas = new byte[size];
                    in.read(datas, 0, size);
                    FileOutputStream fout = new FileOutputStream("Changing Seats/main2.jar");
                    fout.write(datas);
                    fout.close();
                    Runtime.getRuntime().exec("cmd /c java -jar \"Changing Seats/updater.jar\"");
                    System.exit(0);
                } else {
                    out.writeBoolean(false);
                }
            }
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(Main.main, "업데이트 서버에 접속할 수 없습니다 : " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}
