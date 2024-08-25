package com.neelamani.basiccalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    // Variables to store values for calculation
    String valOne;
    String valTwo;

    // Buttons for the calculator operations and numbers
    Button buttonClear;
    Button buttonEqual;
    Button buttonClearOne;

    Button buttonOne;
    Button buttonTwo;
    Button buttonThree;
    Button buttonFour;
    Button buttonFive;
    Button buttonSix;
    Button buttonSeven;
    Button buttonEight;
    Button buttonNine;
    Button buttonZero;
    Button buttonDot;

    Button buttonAddition;
    Button buttonSubtract;
    Button buttonDivision;
    Button buttonMultiply;

    // Stores the currently selected operator and whether an operator is selected
    String selectedOperator;
    Boolean isOperatorSelected = false;

    // TextView to display the result and expression
    TextView resultDisplay;

    // StringBuilder to build and hold the current expression
    StringBuilder expression = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enables edge-to-edge display mode
        setContentView(R.layout.activity_main);

        // Initialize
        resultDisplay = findViewById(R.id.resultDisplay);
        buttonClear = findViewById(R.id.buttonClear);
        buttonEqual = findViewById(R.id.buttonEqual);
        buttonClearOne = findViewById(R.id.buttonClearOne);

        buttonOne = findViewById(R.id.buttonOne);
        buttonTwo = findViewById(R.id.buttonTwo);
        buttonThree = findViewById(R.id.buttonThree);
        buttonFour = findViewById(R.id.buttonFour);
        buttonFive = findViewById(R.id.buttonFive);
        buttonSix = findViewById(R.id.buttonSix);
        buttonSeven = findViewById(R.id.buttonSeven);
        buttonEight = findViewById(R.id.buttonEight);
        buttonNine = findViewById(R.id.buttonNine);
        buttonZero = findViewById(R.id.buttonZero);
        buttonDot = findViewById(R.id.buttonDot);

        buttonAddition = findViewById(R.id.buttonAddition);
        buttonSubtract = findViewById(R.id.buttonSubtract);
        buttonDivision = findViewById(R.id.buttonDivision);
        buttonMultiply = findViewById(R.id.buttonMultiply);

        // Set click listeners for number buttons to append the value to the expression
        buttonOne.setOnClickListener( appendExpression("1") );
        buttonTwo.setOnClickListener( appendExpression("2") );
        buttonThree.setOnClickListener( appendExpression("3") );
        buttonFour.setOnClickListener( appendExpression("4") );
        buttonFive.setOnClickListener( appendExpression("5") );
        buttonSix.setOnClickListener( appendExpression("6") );
        buttonSeven.setOnClickListener( appendExpression("7") );
        buttonEight.setOnClickListener( appendExpression("8") );
        buttonNine.setOnClickListener( appendExpression("9") );
        buttonZero.setOnClickListener( appendExpression("0") );

        // Set click listeners for operator buttons
        buttonAddition.setOnClickListener( operatorHandler("+") );
        buttonSubtract.setOnClickListener( operatorHandler("-") );
        buttonMultiply.setOnClickListener( operatorHandler("*") );
        buttonDivision.setOnClickListener( operatorHandler("/") );

        // Set click listener for the equal button to calculate the result
        buttonEqual.setOnClickListener(calculateResult());

        // Set click listener for the dot button to add a decimal point
        buttonDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentDisplay = resultDisplay.getText().toString();

                if (!currentDisplay.endsWith(".") && !getLastNumber().contains(".")) {
                    expression.append(".");
                    resultDisplay.setText(expression.toString());
                }
            }
        });

        // Set click listener for the clear button to reset everything
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expression.setLength(0);
                resultDisplay.setText("");
                valOne = null;
                valTwo = null;
                selectedOperator = "";
                isOperatorSelected = false;
            }
        });

        // Set click listener for the clear one button to remove the last character
        buttonClearOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expression.length() > 0) {
                    expression.deleteCharAt(expression.length() - 1);
                    resultDisplay.setText(expression.toString());
                }
            }
        });

    }

    // Method to handle when the last clicked operator is pressed again
    public void lastClickedOperator() {
        String currentDisplay = resultDisplay.getText().toString();

        if (currentDisplay.endsWith(" ")) {
            if (expression.length() > 0) {
                expression.deleteCharAt(expression.length() - 2);
                resultDisplay.setText(expression.toString());
            }
        }
    }

    // Helper method to check if the last character in the expression is an operator
    private boolean isLastCharacterOperator() {
        if (expression.length() == 0) return false;

        char lastChar = expression.charAt(expression.length() - 1);
        return lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/';
    }


    // Method to append numbers to the expression
    private View.OnClickListener appendExpression(String value) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOperatorSelected) {
                    resultDisplay.setText("");
                    isOperatorSelected = false;
                }
                expression.append(value);
                resultDisplay.setText(expression.toString());
            }
        };
    }

    // Method to handle operator button clicks
    private View.OnClickListener operatorHandler(String strOperator) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the expression is empty or the last character is an operator
                if (expression.length() == 0 || isLastCharacterOperator()) {
                    return; // Do nothing if the first input is an operator or if the last character is an operator
                }

                if (valOne != null && !valOne.isEmpty()) {
                    valTwo = getLastNumber(); // Store the last entered number in valTwo
                    calculation();  // Calculate the result of the previous operation
                    lastClickedOperator(); // Handle consecutive operator clicks
                }
                valOne = resultDisplay.getText().toString();  // Store the result back into valOne

                selectedOperator = strOperator;
                isOperatorSelected = true;

                // Append the operator to the full expression
                expression.append(" ").append(strOperator).append(" ");
                resultDisplay.setText(expression.toString());
            }
        };
    }

    // Method to handle the equal button click and calculate the final result
    private View.OnClickListener calculateResult() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valOne != null && !valOne.isEmpty()) {
                    valTwo = getLastNumber();
                    calculation();

                    // Optionally clear or reset the expression
                    expression.setLength(0);
                    expression.append(resultDisplay.getText().toString());
                    selectedOperator = "";
                }
            }
        };
    }

    // Method to perform the calculation based on the selected operator
    private void calculation() {
        if (!valOne.isEmpty() && !valTwo.isEmpty() && !selectedOperator.isEmpty()) {
            double valOneDouble = Double.parseDouble(valOne); // Convert valOne to double
            double valTwoDouble = Double.parseDouble(valTwo); // Convert valTwo to double
            double result = 0.0;
            boolean error = false;

            // Perform the calculation based on the operator
            switch (selectedOperator) {
                case "+":
                    result = valOneDouble + valTwoDouble;
                    break;
                case "-":
                    result = valOneDouble - valTwoDouble;
                    break;
                case "*":
                    result = valOneDouble * valTwoDouble;
                    break;
                case "/":
                    if (valTwoDouble > 0) {
                        result = valOneDouble / valTwoDouble;
                    } else {
                        error = true;
                    }
                    break;
                default:
                    error = true; // Handle unknown operators
            }

            // Display error if there's an issue with the calculation
            if (error) {
                resultDisplay.setText("Error");
            } else {
                resultDisplay.setText(Double.toString(result)); // Display the result
            }
        }
    }

    // Method to get the last entered number in the expression
    private String getLastNumber() {
        // Find the last occurrence of an operator in the expression
        int lastOperatorIndex = Math.max(
                expression.lastIndexOf("+"),
                Math.max(
                        expression.lastIndexOf("-"),
                        Math.max(
                                expression.lastIndexOf("*"),
                                expression.lastIndexOf("/")
                        )
                )
        );

        // Get the substring after the last operator, which is the last number
        return expression.substring(lastOperatorIndex + 1).trim();
    }
}
