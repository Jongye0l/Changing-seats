package com.Jongyeol.ChangeSeat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Downloader extends JFrame {
    JLabel label;
    public Downloader() {
        setTitle("자리 바꾸기 다운로더");
        setSize(600, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        //setBackground(new Color(0, 0, 0, 0));
        setLayout(null);
        label = new JLabel("서버에 접속하는 중");
        label.setBounds(20, 100, 560, 80);
        label.setVisible(true);
        label.setFont(new Font("Korean", Font.BOLD, 20));
        add(label);
        new Thread(() -> {
            setName("Server Connector");
            try {
                Socket socket = new Socket("Jongyeol.kro.kr", 1209);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream());
                out.writeInt(0);
                out.writeInt(-1);
                label.setText("다운로드 중");
                File folder = new File("Changing Seats");
                if(!folder.exists() || !folder.isDirectory()) folder.mkdir();
                int size = in.readInt();
                byte[] datas = new byte[size];
                for(int i = 0; i < size; i++) datas[i] = in.readByte();
                FileOutputStream fout = new FileOutputStream("Changing Seats/main.jar");
                fout.write(datas);
                fout.close();
                size = in.readInt();
                datas = new byte[size];
                for(int i = 0; i < size; i++) datas[i] = in.readByte();
                fout = new FileOutputStream("Changing Seats/updater.jar");
                fout.write(datas);
                fout.close();
                size = in.readInt();
                datas = new byte[size];
                for(int i = 0; i < size; i++) datas[i] = in.readByte();
                fout = new FileOutputStream("Changing Seats/resource.zip");
                fout.write(datas);
                fout.close();
                unzipFile(new File("Changing Seats/resource.zip").toPath(), new File("Changing Seats/resource").toPath());
                in.close();
                out.close();
                label.setText("완료");
            } catch (IOException e) {
                label.setText("서버에 연결할 수 없습니다.");
            }
        }).start();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Runtime.getRuntime().exec("cmd /c java -jar \"Changing Seats/main.jar\"");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                super.windowClosing(e);
            }
        });
    }

    public static void unzipFile(Path sourceZip, Path targetDir) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(sourceZip.toFile()))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                boolean isDirectory = zipEntry.getName().endsWith(File.separator);
                Path newPath = zipSlipProtect(zipEntry, targetDir);
                if (isDirectory) Files.createDirectories(newPath);
                else {
                    if (newPath.getParent() != null && Files.notExists(newPath.getParent())) Files.createDirectories(newPath.getParent());
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Path zipSlipProtect(ZipEntry zipEntry, Path targetDir) throws IOException {
        Path targetDirResolved = targetDir.resolve(zipEntry.getName());
        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) throw new IOException("Bad zip entry: " + zipEntry.getName());
        return normalizePath;
    }
}
