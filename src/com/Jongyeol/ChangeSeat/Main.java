package com.Jongyeol.ChangeSeat;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Random;

public class Main extends JFrame implements MouseListener {
    int xCount;
    int yCount;
    int seatHeight;
    int seatWidth;
    int round;
    private final Image screenBoard = new ImageIcon("Changing Seats/resource/screenBoard.jpg").getImage();
    private final Image tv = new ImageIcon("Changing Seats/resource/tv.jpg").getImage();
    private final Image upload_null = new ImageIcon("Changing Seats/resource/upload_null.png").getImage();
    private final Image upload_enter = new ImageIcon("Changing Seats/resource/upload_enter.png").getImage();
    private final Image upload_click = new ImageIcon("Changing Seats/resource/upload_click.png").getImage();
    private Image upload = upload_null;
    private final Image mix_null = new ImageIcon("Changing Seats/resource/mix_null.png").getImage();
    private final Image mix_enter = new ImageIcon("Changing Seats/resource/mix_enter.png").getImage();
    private final Image mix_click = new ImageIcon("Changing Seats/resource/mix_click.png").getImage();
    private Image mix = mix_null;
    private final Image setting_null = new ImageIcon("Changing Seats/resource/setting_null.png").getImage();
    private final Image setting_enter = new ImageIcon("Changing Seats/resource/setting_enter.png").getImage();
    private final Image setting_click = new ImageIcon("Changing Seats/resource/setting_click.png").getImage();
    private Image setting_image = setting_null;
    private final Image download_null = new ImageIcon("Changing Seats/resource/download_null.png").getImage();
    private final Image download_enter = new ImageIcon("Changing Seats/resource/download_enter.png").getImage();
    private final Image download_click = new ImageIcon("Changing Seats/resource/download_click.png").getImage();
    private Image download = download_null;
    private final JFileChooser fileChooser = new JFileChooser();
    private final JFileChooser downloadChooser = new JFileChooser();
    private final ArrayList<Seat> seats = new ArrayList<>();
    private ClickLocation clickedLocation;
    public SeatSelectListener seatSelectListener = new SeatSelectListener();
    static final Main main = new Main();
    public static void main(String[] args) {}
    private boolean released = true;
    private final ArrayList<ClickLocation> clickLocations = new ArrayList<>();
    private final File name;
    final Setting setting = new Setting();
    final UpdateChecker updateChecker = new UpdateChecker(setting.getValue(Settings.Version.name()).getAsInt());
    private final SettingScreen settingScreen = new SettingScreen(setting);
    static final Image hide = new ImageIcon("Changing Seats/resource/click.png").getImage();
    static final Image lock_true = new ImageIcon("Changing Seats/resource/lock_true.png").getImage();
    static final Image lock_false = new ImageIcon("Changing Seats/resource/lock_false.png").getImage();
    public Main() {
        setTitle("자리 바꾸기");
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        //setBackground(new Color(0, 0, 0, 0));
        setLayout(null);
        name = new File("Changing Seats/name.db");
        seatLoad();
        fileChooser.resetChoosableFileFilters();
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return true;
            }

            @Override
            public String getDescription() {
                return ".txt";
            }
        });
        for(FileFilter f : fileChooser.getChoosableFileFilters()) downloadChooser.removeChoosableFileFilter(f);
        downloadChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return true;
            }
            @Override
            public String getDescription() {
                return ".jpg";
            }
        });
        downloadChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return true;
            }
            @Override
            public String getDescription() {
                return ".png";
            }
        });
        // 업로드 버튼 클릭 리스너
        clickLocations.add(new ClickLocation(3600, 20, 180, 180, new ClickLocation.MouseListener() {
            private boolean enter = false;
            @Override
            public void onClick() {
                upload = upload_click;
                new Thread(() -> {
                    fileChooser.showOpenDialog(Main.this);
                    released = true;
                    mouseReleased(new MouseEvent(new Component(){}, 0, 0, 0, 0, 0, 0, false, 0 ));
                    clickedLocation = null;
                    if(fileChooser.getSelectedFile() == null){
                        JOptionPane.showMessageDialog(getParent(), "파일이 지정되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try {
                        FileInputStream inputStream = new FileInputStream(fileChooser.getSelectedFile());
                        String data = new String(inputStream.readAllBytes());
                        String[] datas = data.split("\n");
                        for(int i = 0; i < datas.length; i++) {
                            if(datas[i].equals("")) continue;
                            seats.get(i).setName(datas[i]);
                        }
                        saveData();
                    } catch (FileNotFoundException e) {
                        JOptionPane.showMessageDialog(getParent(), "파일이 존재하지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(getParent(), "파일을 열 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                    } catch (InvalidPathException e) {
                        JOptionPane.showMessageDialog(getParent(), "파일의 위치가 잘못설정되었습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                    } catch (IndexOutOfBoundsException e) {
                        JOptionPane.showMessageDialog(getParent(), "설정된 수보다 사람이 많습니다.", "경고", JOptionPane.WARNING_MESSAGE);
                    }
                }).start();
            }

            @Override
            public void onReleased() {
                if (enter) upload = upload_enter;
                else upload = upload_null;
            }

            @Override
            public void onEnter() {
                enter = true;
                if(upload == upload_null) upload = upload_enter;
            }

            @Override
            public void onExit() {
                enter = false;
                if(upload == upload_enter) upload = upload_null;
            }
        }));
        // 섞기 버튼 클릭 리스너
        clickLocations.add(new ClickLocation(3420, 20, 180, 180, new ClickLocation.MouseListener() {
            private boolean enter = false;
            @Override
            public void onClick() {
                mix = mix_click;
                ArrayList<String> names = new ArrayList<>();
                for(Seat seat : seats) if(!seat.locked) names.add(seat.getName());
                if(setting.getValue(Settings.Repeat.name()).getAsBoolean()) {
                    new Thread(() -> {
                        boolean visible = setting.getValue(Settings.HideLocation.name()).getAsBoolean() && !setting.getValue(Settings.RepeatHide.name()).getAsBoolean();
                        boolean finalVisible = setting.getValue(Settings.HideLocation.name()).getAsBoolean();
                        Random rand = new Random();
                        for(int i = 0; i < setting.getValue(Settings.RepeatNumber.name()).getAsInt(); i++) {
                            ArrayList<Seat> leftSeats = new ArrayList<>();
                            for(Seat seat : seats) if(!seat.locked) leftSeats.add(seat);
                            for(String name : names) {
                                int index = rand.nextInt(leftSeats.size());
                                leftSeats.get(index).setName(name);
                                leftSeats.get(index).setVisibility(!visible);
                                if(i == setting.getValue(Settings.RepeatNumber.name()).getAsInt() - 1) leftSeats.get(index).setVisibility(!finalVisible);
                                leftSeats.remove(index);
                            }
                            try {
                                Thread.sleep(setting.getValue(Settings.RepeatTime.name()).getAsInt());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                JOptionPane.showMessageDialog(getParent(), "시간 지연의 오류가 발생했습니다 : " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }).start();
                } else {
                    ArrayList<Seat> leftSeats = new ArrayList<>(seats);
                    boolean visible = setting.getValue(Settings.HideLocation.name()).getAsBoolean();
                    Random rand = new Random();
                    for(String name : names) {
                        int index = rand.nextInt(leftSeats.size());
                        leftSeats.get(index).setName(name);
                        leftSeats.get(index).setVisibility(!visible);
                        leftSeats.remove(index);
                    }
                }
                saveData();
            }

            @Override
            public void onReleased() {
                if (enter) mix = mix_enter;
                else mix = mix_null;
            }

            @Override
            public void onEnter() {
                enter = true;
                if(mix == mix_null) mix = mix_enter;
            }

            @Override
            public void onExit() {
                enter = false;
                if(mix == mix_enter) mix = mix_null;
            }
        }));
        // 설정 버튼 클릭 리스너
        clickLocations.add(new ClickLocation(3240, 20, 180, 180, new ClickLocation.MouseListener() {
            private boolean enter = false;
            @Override
            public void onClick() {
                setting_image = setting_click;
                settingScreen.showScreen();
            }

            @Override
            public void onReleased() {
                if (enter) setting_image = setting_enter;
                else setting_image = setting_null;
            }

            @Override
            public void onEnter() {
                enter = true;
                if(setting_image == setting_null) setting_image = setting_enter;
            }

            @Override
            public void onExit() {
                enter = false;
                if(setting_image == setting_enter) setting_image = setting_null;
            }
        }));
        // 다운로드 버튼 클릭 리스너
        clickLocations.add(new ClickLocation(3060, 20, 180, 180, new ClickLocation.MouseListener() {
            private boolean enter = false;
            @Override
            public void onClick() {
                download = download_click;
                new Thread(() -> {
                    downloadChooser.showOpenDialog(Main.this);
                    released = true;
                    mouseReleased(new MouseEvent(new Component(){}, 0, 0, 0, 0, 0, 0, false, 0 ));
                    clickedLocation = null;
                    if(downloadChooser.getSelectedFile() == null){
                        JOptionPane.showMessageDialog(getParent(), "파일이 지정되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String extensions[] = downloadChooser.getSelectedFile().getName().split("\\.");
                    String extension = extensions[extensions.length - 1];
                    File file;
                    switch (extension) {
                        case "jpg" :
                        case "png" :
                            file = downloadChooser.getSelectedFile();
                            break;
                        default :
                            file = new File(downloadChooser.getSelectedFile().getPath() + downloadChooser.getFileFilter().getDescription());
                            extension = "jpg";
                    }
                    Image image = createImage(3840, 2098);
                    Graphics2D realGraphics = (Graphics2D) image.getGraphics();
                    printingScreen(realGraphics);
                    BufferedImage screenImage = new BufferedImage(3840, 2098, 1);
                    Graphics2D screenGraphics = (Graphics2D) screenImage.getGraphics();
                    screenGraphics.drawImage(image, 0, 0, 3840, 2098, null);
                    try {
                        ImageIO.write(screenImage, extension, file);
                        JOptionPane.showMessageDialog(getParent(), "저장이 완료되었습니다.\n저장된 위치 : " + file.getPath(), "알림", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(getParent(), "이미지 저장에 오류가 발생하였습니다. : " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                    }
                }).start();
            }

            @Override
            public void onReleased() {
                if (enter) download = download_enter;
                else download = download_null;
            }

            @Override
            public void onEnter() {
                enter = true;
                if(download == download_null) download = download_enter;
            }

            @Override
            public void onExit() {
                enter = false;
                if(download == download_enter) download = download_null;
            }
        }));
        this.addMouseListener(this);
    }
    public void paint(Graphics g) {
        Image screenImage = createImage(3840, 2098);
        Graphics screenGraphics = screenImage.getGraphics();
        try {
            screenDraw((Graphics2D) screenGraphics);
        } catch (Exception e) {
            repaint();
            return;
        }
        g.drawImage(screenImage, 8, 31, this.getWidth() - 8, this.getHeight() - 31, null);
    }
    public void printingScreen(Graphics2D g) {
        g.drawImage(screenBoard, 1520, 0, 800, 400, null);
        g.drawImage(tv, 60, 60, 640, 350, null);
        Image seatImage = createImage(3840, 1698);
        Graphics seatGraphics = seatImage.getGraphics();
        drawSeat((Graphics2D) seatGraphics);
        g.drawImage(seatImage, 0, 400, 3840, 1698, null);
    }
    public void screenDraw(Graphics2D g) {
        paintComponents(g);
        g.drawImage(screenBoard, 1520, 0, 800, 400, null);
        g.drawImage(tv, 60, 60, 640, 350, null);
        g.drawImage(upload, 3600, 20, 180, 180, null);
        g.drawImage(mix, 3420, 20, 180, 180, null);
        g.drawImage(setting_image, 3240, 20, 180, 180, null);
        g.drawImage(download, 3060, 20, 180, 180, null);
        Image seatImage = createImage(3840, 1698);
        try {
            for(ClickLocation clickLocation : clickLocations) {
                if(clickLocation.location((getMousePosition().x - 7) * 3840 / (this.getWidth() - 7), (getMousePosition().y - 31) * 2098 / (this.getHeight() - 31))) {
                    clickLocation.mouseListener.onEnter();
                } else {
                    clickLocation.mouseListener.onExit();
                }
            }
        } catch (NullPointerException ignored) {}
        Graphics seatGraphics = seatImage.getGraphics();
        drawSeat((Graphics2D) seatGraphics);
        g.drawImage(seatImage, 0, 400, 3840, 1698, null);
        try {
            Thread.sleep(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.repaint();
    }
    public void drawSeat(Graphics2D g) {
        seatWidth = (3730 - xCount * 100) / xCount;
        seatHeight = (1648 - yCount * 50) / yCount;
        round = seatWidth / 30;
        for(Seat seat : seats) {
            seat.draw(g);
        }
    }
    public void saveData() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(name);
            String datas = "";
            for(Seat seat : seats) {
                datas += seat.getName() + "\n";
            }
            fileOutputStream.write(datas.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            try {
                name.createNewFile();
                saveData();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(getParent(), "데이터 파일을 생성하는 중 오류가 발생했습니다 : " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(getParent(), "데이터 파일을 저장하는 중 오류가 발생했습니다 : " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void seatLoad() {
        xCount = setting.getValue(Settings.XCount.name()).getAsInt();
        yCount = setting.getValue(Settings.XCount.name()).getAsInt();
        for(int i = 0; i < xCount; i++) for(int j = 0; j < yCount; j++) seats.add(new Seat(i, j, i * yCount + j));
        try {
            FileInputStream inputStream = new FileInputStream(name);
            String data = new String(inputStream.readAllBytes());
            String[] datas = data.split("\n");
            for(int i = 0; i < datas.length; i++) {
                if(datas[i].equals("")) continue;
                seats.get(i).setName(datas[i]);
            }
        } catch (FileNotFoundException e) {
            try {
                name.createNewFile();
            } catch (IOException ex) {
                new Thread(() -> JOptionPane.showMessageDialog(getParent(), "데이터 파일을 생성하는 중 오류가 발생했습니다 : " + e.getMessage() , "오류", JOptionPane.ERROR_MESSAGE)).start();
            }
        } catch (IOException e) {
            new Thread(() -> JOptionPane.showMessageDialog(getParent(), "파일을 여는 중 오류가 발생하였습니다 : " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE)).start();
        } catch (IndexOutOfBoundsException e) {
            new Thread(() -> JOptionPane.showMessageDialog(getParent(), "설정된 수보다 사람이 많습니다 : " + e.getMessage(), "경고", JOptionPane.WARNING_MESSAGE)).start();
        }
        setting.loadLock(seats);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if(released) {
            released = false;
            for(ClickLocation clickLocation : clickLocations) {
                if(clickLocation.location((e.getX() - 7) * 3840 / (this.getWidth() - 7), (e.getY() - 31) * 2098 / (this.getHeight() - 31))) {
                    clickedLocation = clickLocation;
                    clickLocation.mouseListener.onClick();
                    break;
                }
            }
            for(Seat seat : seats) {
                if(seat.location((e.getX() - 7) * 3840 / (this.getWidth() - 7), (e.getY() - 31) * 2098 / (this.getHeight() - 31) - 400)) {
                    if(seat.lockLocation((e.getX() - 7) * 3840 / (this.getWidth() - 7), (e.getY() - 31) * 2098 / (this.getHeight() - 31) - 400)) seat.onLockClick();
                    else seat.onClick();
                    break;
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        released = true;
        if(clickedLocation != null) {
            clickedLocation.mouseListener.onReleased();
            clickedLocation = null;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}