package com.Jongyeol.ChangeSeat;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class SettingScreen extends JFrame {
    private final Setting setting;
    private JCheckBox setting1, setting2, setting5;
    private JTextField setting3, setting4, setting6, setting7;
    public SettingScreen(Setting setting) {
        this.setting = setting;
        setTitle("자리 바꾸기 설정");
        setSize(500, 700);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ArrayList<Setting.Changing> list = new ArrayList<>();
                list.add(new Setting.Changing(Settings.HideLocation.name(), setting1.isSelected()));
                list.add(new Setting.Changing(Settings.Repeat.name(), setting2.isSelected()));
                try {
                    int i = Integer.parseInt(setting3.getText());
                    if(i >= 0) list.add(new Setting.Changing(Settings.RepeatNumber.name(), i));
                } catch (NumberFormatException ignored) {}
                try {
                    int i = Integer.parseInt(setting4.getText());
                    if(i >= 0) list.add(new Setting.Changing(Settings.RepeatTime.name(), i));
                } catch (NumberFormatException ignored) {}
                list.add(new Setting.Changing(Settings.RepeatHide.name(), setting5.isSelected()));
                try {
                    int i = Integer.parseInt(setting6.getText());
                    if(i > 0) list.add(new Setting.Changing(Settings.XCount.name(), i));
                } catch (NumberFormatException ignored) {}
                try {
                    int i = Integer.parseInt(setting7.getText());
                    if(i > 0) list.add(new Setting.Changing(Settings.YCount.name(), i));
                } catch (NumberFormatException ignored) {}
                setting.changeValue(list);
                setting3.setText(setting.getValue(Settings.RepeatNumber.name()).getAsInt() + "");
                setting4.setText(setting.getValue(Settings.RepeatTime.name()).getAsInt() + "");
                setting6.setText(setting.getValue(Settings.XCount.name()).getAsInt() + "");
                setting7.setText(setting.getValue(Settings.YCount.name()).getAsInt() + "");
            }
        });
        createItems();
    }
    public void createItems() {
        setting1 = new JCheckBox("자리 가리기");
        setting1.setBounds(0, 0, 90, 30);
        setting1.setContentAreaFilled(true);
        setting1.setSelected(setting.getValue(Settings.HideLocation.name()).getAsBoolean());
        add(setting1);
        setting2 = new JCheckBox("반복");
        setting2.setBounds(0, 30, 50, 30);
        setting2.setContentAreaFilled(true);
        setting2.setSelected(setting.getValue(Settings.Repeat.name()).getAsBoolean());
        add(setting2);
        JLabel setting3_1 = new JLabel("횟수");
        setting3_1.setBounds(0, 60, 30, 30);
        add(setting3_1);
        setting3 = new JTextField(setting.getValue(Settings.RepeatNumber.name()).getAsInt() + "");
        setting3.setBounds(30, 60, 90, 30);
        add(setting3);
        JLabel setting4_1 = new JLabel("지연 시간(ms)");
        setting4_1.setBounds(0, 90, 85, 30);
        add(setting4_1);
        setting4 = new JTextField(setting.getValue(Settings.RepeatTime.name()).getAsInt() + "");
        setting4.setBounds(85, 90, 90, 30);
        add(setting4);
        setting5 = new JCheckBox("마지막만 가리기");
        setting5.setBounds(0, 120, 120, 30);
        setting5.setContentAreaFilled(true);
        setting5.setSelected(setting.getValue(Settings.RepeatHide.name()).getAsBoolean());
        add(setting5);
        JLabel setting6_1 = new JLabel("책상 가로 수");
        setting6_1.setBounds(0, 150, 85, 30);
        add(setting6_1);
        setting6 = new JTextField(setting.getValue(Settings.XCount.name()).getAsInt() + "");
        setting6.setBounds(85, 150, 90, 30);
        add(setting6);
        JLabel setting7_1 = new JLabel("책상 세로 수");
        setting7_1.setBounds(0, 180, 85, 30);
        add(setting7_1);
        setting7 = new JTextField(setting.getValue(Settings.YCount.name()).getAsInt() + "");
        setting7.setBounds(85, 180, 90, 30);
        add(setting7);
        JLabel setting7_2 = new JLabel("책상 가로/세로 수는 프로그램을 제시작시 적용됩니다.");
        setting7_2.setBounds(0, 210, 300, 30);
        add(setting7_2);
        JLabel setting7_3 = new JLabel("자리데이터가 손상될 수 있으니 주의해주세요!");
        setting7_3.setBounds(0, 230, 300, 30);
        add(setting7_3);
    }
    public void showScreen() {
        setVisible(true);
    }
}
