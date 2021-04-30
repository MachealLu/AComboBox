package com.lyd.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lyd.box.ComboBox;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context mContext;

     ComboBox spinner, spinner1;
     Button btn1, btn2, btn3;

     boolean  edit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        spinner = findViewById(R.id.box);
        spinner1 = findViewById(R.id.box2);
        btn1 = findViewById(R.id.btn1);

        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);

        spinner.setSingleLine();
        setupDefault();
        setupTintedWithCustomClass();
        setupXml();

    }


    private void setupXml() {
//        NiceSpinner spinner = findViewById(R.id.niceSpinnerXml);
//        spinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
//            @Override
//            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
//                String item = parent.getItemAtPosition(position).toString();
//                Toast.makeText(TestSpinnerActivity.this, "Selected: " + item, Toast.LENGTH_SHORT).show();
//            }
//        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //spinner.addItem("测试菜单");
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit = !edit;
                spinner.setEnabled(edit);
                spinner.setFocusable(edit);
                spinner.setFocusableInTouchMode(edit);
                //spinner.requestFocus();

                spinner1.setEnabled(edit);
                spinner1.setFocusable(edit);
                spinner1.setFocusableInTouchMode(edit);
            }
        });
    }

    private void setupTintedWithCustomClass() {
//        spinner.setBackground(null);
        List<String> people = new ArrayList<>();

        people.add("Stark");
        people.add("Rogers");
        people.add("Banner");
        people.add("Bannerssssssssssssssssssssss");
        people.add("Bannesssfsfswwfsfsfsfevehashar");
        people.add("Bannpbpaohfpaohg[]sljiojnmuioihhjer");
        people.add("Banner");

//        SpinnerTextFormatter textFormatter = new SimpleSpinnerTextFormatter() {
//            @Override
//            public Spannable format(String text) {
//                return new SpannableString(person.getName() + " " + person.getSurname());
//            }
//
//        };

//        spinner.setSpinnerTextFormatter(textFormatter);
//        spinner.setSelectedTextFormatter(textFormatter);
//        spinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
//            @Override
//            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
//                Person person = (Person) spinner.getSelectedItem();
//                Toast.makeText(mContext, "Selected: " + person.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
        spinner.setDataSet(people);
    }

    private void setupDefault() {
//        NiceSpinner spinner = findViewById(R.id.nice_spinner);
//        spinner.setLines(1);
//        spinner.setEllipsize(TextUtils.TruncateAt.END);
//        List<String> dataset = new LinkedList<>(Arrays.asList("测试惨淡选择条目", "Two", "Three", "测试惨淡选择条222目", "Five"));
//        spinner.attachDataSource(dataset);
//        spinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
//            @Override
//            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
//                String item = parent.getItemAtPosition(position).toString();
//                Toast.makeText(mContext, "Selected: " + item, Toast.LENGTH_SHORT).show();
//            }
//        });
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