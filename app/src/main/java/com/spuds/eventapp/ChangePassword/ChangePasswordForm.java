package com.spuds.eventapp.ChangePassword;

import android.widget.EditText;

/**
 * Created by qtmluong on 5/12/2016.
 */

/*---------------------------------------------------------------------------
   Class Name: ChangePasswordForm
   Description: Class that contains the forms that the user inputs when
            changing password.
---------------------------------------------------------------------------*/
public class ChangePasswordForm {

    private String email;
    private String current;
    private String next;
    private String confirm;

    public ChangePasswordForm(String user_email, EditText current_pw, EditText next_pw, EditText confirm_pw){
        email = user_email;
        current = current_pw.getText().toString();
        next = next_pw.getText().toString();
        confirm = confirm_pw.getText().toString();
    }

    /*---------------------------------------------------------------------------
     Function Name: allFilled
     Description: Checks if all fields have an input
     Input: None
     Output: true on all filled, false if not
    ---------------------------------------------------------------------------*/
    public boolean allFilled(){
        return (current.length()>0 && next.length()>0 && confirm.length()>0);
    }

    /*---------------------------------------------------------------------------
     Function Name: matchingPw
     Description: checks if the next and confirm fields are the same
     Input: None
     Output: true if passwords match, false if not
    ---------------------------------------------------------------------------*/
    public boolean matchingPw(){
        return next.equals(confirm);
    }

    /*---------------------------------------------------------------------------
     Function Name: diffPw
     Description: Checks if the current password is different from the next
     Input: None
     Output: true if different, false if not
    ---------------------------------------------------------------------------*/
    public boolean diffPw(){
        return !current.equals(next);
    }

    //Getter methods
    public String getEmail(){ return email; }
    public String getCurrent(){
        return current;
    }
    public String getNext(){
        return next;
    }
}
