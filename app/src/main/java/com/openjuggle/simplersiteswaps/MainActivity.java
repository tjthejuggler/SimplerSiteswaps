package com.openjuggle.simplersiteswaps;

import android.support.v4.math.MathUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editTextUserInput;
    TextView textViewResults;
    Button buttonGenerate;
    Button buttonSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUserInput = (EditText) findViewById(R.id.editText_siteswap);
        textViewResults = (TextView) findViewById(R.id.textView_results);
        buttonGenerate = (Button) findViewById(R.id.button_generate);
        buttonSettings = (Button) findViewById(R.id.button_settings);

        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputIsValid()) {
                    textViewResults.setText("Valid");
                } else {
                    textViewResults.setText("Not Valid");
                }
            }
        });
    }
    public boolean inputIsValid() {
        Boolean isValid = true;
        String userInput = editTextUserInput.getText().toString();
        if (userInputIsEmpty(userInput)) {
            isValid = false;
        }
        if (isValid) {
            if (!siteswapIsValid(userInput)) {
                isValid = false;
            }else{
                isValid = true;
            }
        }
        return isValid;
    }
    public boolean userInputIsEmpty(String userInput) {
        Boolean isEmpty = false;
        if (userInput.matches("")) {
            Toast.makeText(this, "You did not enter a siteswap", Toast.LENGTH_SHORT).show();
            isEmpty = true;
        }
        return isEmpty;
    }
    public boolean siteswapIsValid(String siteswapToCheck){
        Boolean isValid = true;
        List<Integer> siteswapAsAnArrayOfInts = convertSiteswapToArrayOfInts(siteswapToCheck);
        if (!siteswapHasWholeNumberOfObjects(siteswapAsAnArrayOfInts)){
            isValid = false;
        }
        if (isValid){
            if (siteswapHasCollisions(siteswapAsAnArrayOfInts)){
                isValid = false;
            }
        }
        return isValid;
    }
    public List<Integer> convertSiteswapToArrayOfInts(String siteswapToCheck){
        List<Integer> siteswapAsArrayOfInts = new ArrayList<>();
        for (char c : siteswapToCheck.toCharArray()) {
            if (Character.isDigit(c)) {
                siteswapAsArrayOfInts.add(Character.getNumericValue(c));
            } else if (Character.isLetter(c)) {
                siteswapAsArrayOfInts.add(c - 'a' + 10);
            }
        }
        return siteswapAsArrayOfInts;
    }
    public boolean siteswapHasWholeNumberOfObjects(List<Integer> siteswapAsAnArrayOfInts){
        Boolean hasWholeNumberOfObjects = true;
        int sumOfSiteswapInts = 0;
        for (int i: siteswapAsAnArrayOfInts) {
            sumOfSiteswapInts += i;
        }
        if (sumOfSiteswapInts % siteswapAsAnArrayOfInts.size() != 0) {
            hasWholeNumberOfObjects = false;
        }
        return hasWholeNumberOfObjects;
    }
    public boolean siteswapHasCollisions(List<Integer> siteswapAsAnArrayOfInts){
        Boolean hasCollisions = false;
        return hasCollisions;
    }
}
