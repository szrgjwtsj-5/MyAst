package com.whx.myast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testIf(233);
    }

    private void test(int str) {
        // this is a comment
        switch(str) {
            case 1:
                int a = str + 2;
                System.out.println(a);
                break;
            case 2:
                int b = str + 233;
                System.out.println(b);
                break;
            default:
                System.out.println(str);
                break;
        }
    }

    private void testIf(int num) {
        // test if
        if (num == 233) {
            Toast.makeText(this, "hhh", Toast.LENGTH_SHORT).show();
        } else if (num == 666) {
            System.out.println(num);
        } else {
            Toast.makeText(this, "hhh", Toast.LENGTH_SHORT).show();
        }

    }
}
