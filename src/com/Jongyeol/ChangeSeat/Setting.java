package com.Jongyeol.ChangeSeat;

import com.google.gson.*;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.Jongyeol.ChangeSeat.Main.main;

public class Setting {
    private final int version = 0;
    private final File setting;
    private JsonObject object;
    public Setting() {
        File folder = new File("Changing Seats");
        if(!folder.exists() || !folder.isDirectory()) folder.mkdir();
        setting = new File("Changing Seats/setting.db");
        if(!setting.isFile()) {
            try {
                setting.createNewFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(main, "데이터 파일을 생성하는 중 오류가 발생했습니다 : " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
        try {
            boolean save = false;
            FileInputStream inputStream = new FileInputStream(setting);
            try {
                object = JsonParser.parseString(new String(inputStream.readAllBytes())).getAsJsonObject();
            } catch (IllegalStateException e) {
                object = new JsonObject();
                save = true;
            }
            inputStream.close();
            if(object.get(Settings.HideLocation.name()) == null) {
                object.addProperty(Settings.HideLocation.name(), false);
                save = true;
            }
            if(object.get(Settings.Repeat.name()) == null) {
                object.addProperty(Settings.Repeat.name(), false);
                save = true;
            }
            if(object.get(Settings.RepeatNumber.name()) == null) {
                object.addProperty(Settings.RepeatNumber.name(), 0);
                save = true;
            }
            if(object.get(Settings.RepeatTime.name()) == null) {
                object.addProperty(Settings.RepeatTime.name(), 0);
                save = true;
            }
            if(object.get(Settings.RepeatHide.name()) == null) {
                object.addProperty(Settings.RepeatHide.name(), false);
                save = true;
            }
            if(object.get(Settings.Locks.name()) == null) {
                object.add(Settings.Locks.name(), new Gson().toJsonTree(new ArrayList<>()));
                save = true;
            }
            if(object.get(Settings.XCount.name()) == null) {
                object.addProperty(Settings.XCount.name(), 5);
                save = true;
            }
            if(object.get(Settings.YCount.name()) == null) {
                object.addProperty(Settings.YCount.name(), 5);
                save = true;
            }
            if(object.get(Settings.Version.name()) == null || object.get(Settings.Version.name()).getAsInt() != version) {
                object.addProperty(Settings.Version.name(), version);
                save = true;
            }
            if(save) save();
        } catch (FileNotFoundException ignored) {
        } catch (IOException e) {
            JOptionPane.showMessageDialog(main, "파일을 여는 중 오류가 발생했습니다 : " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void loadLock(List<Seat> seats) {
        JsonArray list = object.get(Settings.Locks.name()).getAsJsonArray();
        boolean change = false;
        for(int i = 0; i < list.size(); i++) {
            try {
                seats.get(list.get(i).getAsInt()).locked = true;
            } catch (IndexOutOfBoundsException e) {
                list.remove(i);
                change = true;
            }
        }
        if(change) save();
    }
    public void lockingSeat(int id, boolean b) {
        JsonArray list = object.get(Settings.Locks.name()).getAsJsonArray();
        if(b) {
            list.add(id);
        } else {
            list.remove(new Gson().toJsonTree(id));
        }
        save();
    }

    public void changeValue(List<Changing> list) {
        for(Changing change : list) {
            switch (change.valueId) {
                case 1 :
                    object.addProperty(change.key, change.value_1);
                    break;
                case 2 :
                    object.addProperty(change.key, change.value_2);
                    break;
                case 3 :
                    object.addProperty(change.key, change.value_3);
                    break;
            }
        }
        save();
    }
    private void save() {
        try {
            FileOutputStream outputStream = new FileOutputStream(setting);
            outputStream.write(object.toString().getBytes());
            outputStream.close();
        } catch (FileNotFoundException ignored) {
        } catch (IOException e) {
            JOptionPane.showMessageDialog(main, "파일을 여는 중 오류가 발생했습니다 : " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public JsonElement getValue(String key) {
        return object.get(key);
    }
    public static class Changing {
        private final String key;
        private String value_1;
        private final int valueId;
        private int value_2;
        private boolean value_3;
        public Changing(String key, String value) {
            this.key = key;
            this.value_1 = value;
            valueId = 1;
        }
        public Changing(String key, int value) {
            this.key = key;
            this.value_2 = value;
            valueId = 2;
        }
        public Changing(String key, boolean value) {
            this.key = key;
            this.value_3 = value;
            valueId = 3;
        }
    }
}
