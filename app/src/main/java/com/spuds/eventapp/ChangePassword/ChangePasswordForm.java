package com.spuds.eventapp.ChangePassword;

import android.widget.EditText;

/**
 * Created by qtmluong on 5/12/2016.
 */
public class ChangePasswordForm {

    private String email;
    private String current;
    private String next;
    private String confirm;

    public ChangePasswordForm(String user_email, EditText current_pw, EditText next_pw, EditText confirm_pw){
        email = user_email;
        current = current_pw.toString();
        next = next_pw.toString();
        confirm = confirm_pw.toString();
    }

    public boolean allFilled(){
        if(current.length()>0 && next.length()>0 && confirm.length()>0){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean matchingPw(){
        return next.equals(confirm);
    }

    public String getEmail(){ return email; }

    public String getCurrent(){
        return current;
    }

    public String getNext(){
        return next;
    }
}
