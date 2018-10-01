package com.openjuggle.simplersiteswaps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.math.MathUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    TextView textViewResults;
    FloatingActionButton buttonAdd;
    final Context c = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewResults = (TextView) findViewById(R.id.textView_results);
        buttonAdd = (FloatingActionButton) findViewById(R.id.add_button);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.input_dialog, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);
                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                if (inputIsValid(userInputDialogEditText.getText().toString())) {
                                    fillResultsTextViewWithResults(userInputDialogEditText.getText().toString());

                                }
                                    else {textViewResults.setText("Not Valid");}
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();}
                                });
                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
            }
        });
    }
    public void fillResultsTextViewWithResults(String userInput){

        String resultsString = "";
        String userInputDoubled = userInput+userInput;
        List<String> listOfComponents = new ArrayList<>();
        for (int charToBeginComponentAt = 0; charToBeginComponentAt<userInput.length();charToBeginComponentAt++){
            for (int componentLength = 2; componentLength < userInput.length();componentLength++){
                String componentToPossiblyAddToList = userInputDoubled.substring(charToBeginComponentAt,charToBeginComponentAt+componentLength);
                Log.d(TAG, "fillResultsTextViewWithResults: "+componentToPossiblyAddToList);
                //componentToPossiblyAddToList = shiftHighestDigitToFirstPosition(componentToPossiblyAddToList);
                if (!listOfComponents.contains(componentToPossiblyAddToList)) {
                    listOfComponents.add(componentToPossiblyAddToList);
                }
            }
        }

        //      ONCE we have all the components, then we check to see if any of them are valid siteswaps, if they are valid siteswaps, we add them to
        //          a list of valid siteswaps,

        for (int i = 0; i<listOfComponents.size();i++) {
            if (siteswapIsValid(listOfComponents.get(i))) {
                resultsString += listOfComponents.get(i);
                resultsString += "\n";
            }

            //      AFTER THAT, we go through each one and put all combinations of the numbers in the target siteswap(and 0,2), and check to see if each of those
            //          is a valid siteswap, adding them to a list of valid siteswaps as we go.



        }


        //      WE CAN sort them into different lists based on their number of objects afterwards.








        textViewResults.setText(resultsString);
    }

    public String shiftHighestDigitToFirstPosition(String stringToShift){
        String shiftedString;
        List<String> listOfAllPossibleOrders = new ArrayList<>();
        for (int i = 0; i<stringToShift.length();i++) {
            listOfAllPossibleOrders.add(stringToShift.substring(i)+stringToShift.substring(0,i));
        }
        Collections.sort(listOfAllPossibleOrders, String.CASE_INSENSITIVE_ORDER);

        shiftedString = listOfAllPossibleOrders.get(stringToShift.length()-1);

        return shiftedString;
    }

    public boolean inputIsValid(String userInput) {
        return (userInputIsNotEmpty(userInput) && siteswapIsValid(userInput));
    }
    public boolean userInputIsNotEmpty(String userInput) {
        Boolean isNotEmpty = true;
        if (userInput.matches("")) {
            Toast.makeText(this, "You did not enter a siteswap", Toast.LENGTH_SHORT).show();
            isNotEmpty = false;}
        return isNotEmpty;
    }
    public boolean siteswapIsValid(String siteswapToCheck){ //checks the validity of the siteswap by checking for collisions
        Boolean isValid = true;
        List<Integer> siteswapAsAnArrayOfInts = convertSiteswapToArrayOfInts(siteswapToCheck);
        Boolean hasCollisions = false;
        List<Integer> arrayOfModResults = new ArrayList<>();
        for (int i = 0; i < siteswapAsAnArrayOfInts.size(); i++) {
            arrayOfModResults.add((siteswapAsAnArrayOfInts.get(i) + i) % siteswapAsAnArrayOfInts.size());}
        Set<Integer> setOfModResults = new HashSet<>(arrayOfModResults);
        if(setOfModResults.size() < arrayOfModResults.size()){hasCollisions = true;}
        if (hasCollisions || siteswapAsAnArrayOfInts.size()==0){isValid = false;}
        return isValid;
    }
    public List<Integer> convertSiteswapToArrayOfInts(String siteswapToCheck){
        List<Integer> siteswapAsArrayOfInts = new ArrayList<>();
        for (char c : siteswapToCheck.toCharArray()) {
            if (Character.isDigit(c)) {siteswapAsArrayOfInts.add(Character.getNumericValue(c));}
            else if (Character.isLetter(c)) {siteswapAsArrayOfInts.add(c - 'a' + 10);}
        }
        return siteswapAsArrayOfInts;
    }
    public int numberOfObjectsForSiteswap(List<Integer> siteswapAsAnArrayOfInts){
        int numberOfObjects = -1;
        int sumOfSiteswapInts = 0;
        for (int i: siteswapAsAnArrayOfInts) {
            sumOfSiteswapInts += i;}
        if (sumOfSiteswapInts % siteswapAsAnArrayOfInts.size() == 0) {
            numberOfObjects = sumOfSiteswapInts/siteswapAsAnArrayOfInts.size();}
        return numberOfObjects;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int menuItemThatWasSelected = item.getItemId();
        if (menuItemThatWasSelected == R.id.action_settings){
            Context context = MainActivity.this;
            String message = "Settings clicked";
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }


}

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
//STEP BY STEP:
//  Split siteswap into 2 digit components
//  For each component:
//      add them together to determine if even or odd,
//      IF EVEN - divide by 2, subtract 2, add that many 0s, then add a 2
//      IF ODD, AND LOW DIGIT ODD/FIRST  - add as many 0s as needed to put a 1 at the catch of the lower number, follow that by a 2,
//                          follow that with a 02 as many times as needed to make a 2b siteswap
//      IF ODD, AND LOW DIGIT ODD/SECOND  - add as many 0s as needed to put a 1 at the catch of the lower number, follow that by a 2,
//                          follow that with a 02 as many times as needed to make a 2b siteswap, then add another 2 on the end
//  if ODD, add on the number of 0s needed to be able to put a catch

//

//TO MAKE 3B SITESWAPS:
//  get all the 3 length compontents, if the component % 3 = 0, try adding the appropriate number of 0s to the end of it
//      to make it divide out to 3 balls(this may be best done with numberOfObjectsForSiteswap)
