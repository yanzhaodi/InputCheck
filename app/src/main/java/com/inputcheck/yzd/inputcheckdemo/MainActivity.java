package com.inputcheck.yzd.inputcheckdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yzd.inputcheck.InputCheck;
import com.yzd.inputcheck.annotation.Equal;
import com.yzd.inputcheck.annotation.Match;
import com.yzd.inputcheck.annotation.NotEmpty;

public class MainActivity extends AppCompatActivity {

    @NotEmpty(R.string.hello_empty)
    EditText helloEt;

    @Match(regex = "^1(3|4|5|7|8)\\d{9}$", message = R.string.dont_match)
    EditText matchEt;

    @NotEmpty(R.string.hello_empty)
    EditText equal1Et;

    @NotEmpty(R.string.hello_empty)
    @Equal(target = "equal1Et", message = R.string.dont_equal)
    EditText equal2Et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helloEt = (EditText) findViewById(R.id.et_hello);
        matchEt = (EditText) findViewById(R.id.et_match);
        equal1Et = (EditText) findViewById(R.id.et_equal1);
        equal2Et = (EditText) findViewById(R.id.et_equal2);
    }

    public void confirm(View view) {
        String errorMessage = InputCheck.check(this);

        if (errorMessage != null) {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        // 业务处理
        Toast.makeText(this, "confirm", Toast.LENGTH_SHORT).show();
    }
}
