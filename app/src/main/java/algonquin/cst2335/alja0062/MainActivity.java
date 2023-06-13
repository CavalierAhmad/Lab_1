package algonquin.cst2335.alja0062;

import androidx.appcompat.app.AppCompatActivity;
import algonquin.cst2335.alja0062.databinding.ActivityMainBinding;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding widgets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // this part to make binding work
        widgets = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(widgets.getRoot());


        TextView text = widgets.text;
        EditText edit = widgets.edit;
        Button butt = widgets.btn;

        butt.setOnClickListener(clk -> {
            String password = edit.getText().toString();
            boolean valid = checkPasswordComplexity(password);
            if (valid){text.setText("Your password meets all the requirement");}
                else {text.setText("You shall not pass!");}
        });

    }

    /**
     *
     * @param pass The String object that we are checking
     * @return Returns true if the password is complex enough, false if otherwise
     */
    boolean checkPasswordComplexity(String pass){
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        char[] chars = pass.toCharArray();
        for (char c : chars){
            if (Character.isUpperCase(c))  {foundUpperCase=true;} else
            if (Character.isLowerCase(c))  {foundLowerCase=true;} else
            if (Character.isDigit(c))      {foundNumber   =true;} else
            if ("#$%^&*!@?".contains(""+c)){foundSpecial  =true;} else
            {return false;}
        }

        if(!foundUpperCase)
        {
            Toast.makeText(this,"Missing an upper case letter",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!foundLowerCase)
        {
            Toast.makeText(this,"Missing a lower case letter",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!foundNumber)
        {
            Toast.makeText(this,"Missing a number",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!foundSpecial)
        {
            Toast.makeText(this,"Missing a special character",Toast.LENGTH_LONG).show();
            return false;
        }
        else {return true;}
    }
}