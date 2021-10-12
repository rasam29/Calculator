package com.github.rasam29.calculator;



import static com.github.rasam29.calculator.PreferenceKt.DARK_MODE_STATUS;
import static com.github.rasam29.calculator.PreferenceKt.SHARED_PREF_NAME;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import com.github.rasam29.calculator.calculation.Calculator;
import com.github.rasam29.calculator.calculation.OnExpressionEntered;

import java.util.ArrayList;
import java.util.List;

import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

public class CalculatorActivity extends AppCompatActivity implements OnExpressionEntered {

    private Calculator calculator;
    private TextView answer;
    private TextView preview;
    private AppCompatButton equalButton;
    private Lazy<SharedPreferences> sharedPref = LazyKt.lazy(() -> getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE));
    private ImageView mode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        calculator = new Calculator(this);
        AppCompatButton clearButton = findViewById(R.id.clear);
        setUpView();

        equalButton.setOnClickListener(view ->{
            preview.setText(calculator.getArgument());
            answer.setText(calculator.calculate());
            calculator.clear();
        });
        clearButton.setOnClickListener(view -> {
            calculator.clear();
            preview.setText("");
            answer.setText("");

        });

        setUpDarkMode();


    }

    private void setUpDarkMode() {
        boolean isDark = sharedPref.getValue().getBoolean(DARK_MODE_STATUS,false);
        
        if (isDark){
            mode.setImageResource(R.drawable.ic__02_moon);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            mode.setOnClickListener(view -> {
                sharedPref.getValue().edit().putBoolean(DARK_MODE_STATUS, false).apply();
                setUpDarkMode();
            });
        }else {
            mode.setImageResource(R.drawable.ic__01_sunny_day);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            mode.setOnClickListener(view -> {
                sharedPref.getValue().edit().putBoolean(DARK_MODE_STATUS, true).apply();
                setUpDarkMode();
            });

        }
    }

    private void setUpView(){
        answer = findViewById(R.id.answer);
        AppCompatButton one = findViewById(R.id.one);
        AppCompatButton two = findViewById(R.id.two);
        AppCompatButton three = findViewById(R.id.three);
        AppCompatButton four = findViewById(R.id.four);
        AppCompatButton five = findViewById(R.id.five);
        AppCompatButton six = findViewById(R.id.six);
        AppCompatButton seven = findViewById(R.id.seven);
        AppCompatButton eight = findViewById(R.id.eight);
        AppCompatButton nine = findViewById(R.id.nine);
        AppCompatButton zero = findViewById(R.id.zero);
        AppCompatButton divide = findViewById(R.id.devision);
        AppCompatButton add = findViewById(R.id.add);
        AppCompatButton multiply = findViewById(R.id.multiply);
        AppCompatButton minus = findViewById(R.id.minus);
        mode = findViewById(R.id.mode);
        equalButton = findViewById(R.id.equals);
        preview = findViewById(R.id.expression);
        List<AppCompatButton> list = new ArrayList<>();
        list.add(one);
        list.add(two);
        list.add(three);
        list.add(four);
        list.add(five);
        list.add(six);
        list.add(seven);
        list.add(eight);
        list.add(nine);
        list.add(zero);
        list.add(multiply);
        list.add(add);
        list.add(minus);
        list.add(divide);
        calculator.setNumbersButton(list);

    }


    @Override
    public void onEntered(@NonNull String expression) {
        answer.setText(expression);
    }
}
