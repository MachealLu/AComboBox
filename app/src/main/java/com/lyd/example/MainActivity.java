package com.lyd.example;

import android.content.Context;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.lyd.box.ComboBox;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ComboBox box1;
    private Switch mSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        box1 = findViewById(R.id.box);
        mSwitch = findViewById(R.id.open_switch);
        List<String> menu = new ArrayList<>();
        menu.add("测试菜单1");
        menu.add("测试菜单2");
        menu.add("测试菜单3");
        menu.add("测试菜单4");
        menu.add("测试菜单5");
        menu.add("测试菜单6");
        box1.setDataSet(menu);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    mSwitch.setText("编辑模式");
                } else {
                    mSwitch.setText("选择模式");
                }
                box1.setEditable(b);
            }
        });
    }


    class Person {

        private String name;
        private String surname;

        Person(String name, String surname) {
            this.name = name;
            this.surname = surname;
        }

        String getName() {
            return name;
        }

        String getSurname() {
            return surname;
        }

        @Override
        public String toString() {
            return name + " " + surname;
        }
    }
}