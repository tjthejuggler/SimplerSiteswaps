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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    EditText editTextUserInput;
    TextView textViewResults;
    Button buttonGenerate;
    Button buttonSettings;

    Integer numberOfSiteswapsPerObjectNumber = 10;

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
                    //POSSIBLE SETTINGS:
                    //  -Number of objects to use, this can be a list, like 2,3,4,5
                    //      OR   just make a setting that is include siteswaps with same number of objects as the target,
                    //              and no matter what include 2 and higher up to the point that is the target siteswap

                    //todo: watch tutorial on using the menu, and make my own
                    //todo:instead of printing valid or not valid, use a randomizer to make
                    //todo:    other siteswaps, we can a lso use the functions we already have to verify them, and we can make some globals
                    //// TODO: 9/29/2018  that are our settings which can eventually be user defined
                    // TODO: 9/29/2018 2b siteswaps can use 1s, but only if they need to
                    // TODO: 9/29/2018 practice siteswaps should have at least 2 digits exactly sequencially the same as the input siteswap
                    //if 645 is input, then to make practices, we cycle through coming up with siteswaps that
                    //  begin with 64, 45, and 56, we want to come up with 2b siteswaps for each of those

                    //TO MAKE 2b SITESWAPS:
                    //we take the target, get all of its 2-length components, and then figure out what must be put after
                    //  them to make symetrical siteswaps. the the component adds up to an odd number, then we will need to also use a 1
                    //  also, some even number siteswaps may need a 2 in order to make them symetric, for instance 531 has 53002
                    //  it also has 312, and it has 150

                    //TO MAKE 3B SITESWAPS:
                    //  get all the 3 length compontents, if the component % 3 = 0, try adding the appropriate number of 0s to the end of it
                    //      to make it divide out to 3 balls(this may be best done with numberOfObjectsForSiteswap)
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
        if (numberOfObjectsForSiteswap(siteswapAsAnArrayOfInts)==-1){
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
    public int numberOfObjectsForSiteswap(List<Integer> siteswapAsAnArrayOfInts){
        int numberOfObjects = -1;
        int sumOfSiteswapInts = 0;
        for (int i: siteswapAsAnArrayOfInts) {
            sumOfSiteswapInts += i;
        }
        if (sumOfSiteswapInts % siteswapAsAnArrayOfInts.size() == 0) {
            numberOfObjects = sumOfSiteswapInts/siteswapAsAnArrayOfInts.size();
        }
        return numberOfObjects;
    }
    public boolean siteswapHasCollisions(List<Integer> siteswapAsAnArrayOfInts){
        Boolean hasCollisions = false;
        List<Integer> arrayOfModResults = new ArrayList<>();
         for (int i = 0; i < siteswapAsAnArrayOfInts.size(); i++) {
             arrayOfModResults.add((siteswapAsAnArrayOfInts.get(i) + i)%siteswapAsAnArrayOfInts.size());
         }
        Set<Integer> setOfModResults = new HashSet<>(arrayOfModResults);
        if(setOfModResults.size() < arrayOfModResults.size()){
            hasCollisions = true;
        }
        return hasCollisions;
    }
}
