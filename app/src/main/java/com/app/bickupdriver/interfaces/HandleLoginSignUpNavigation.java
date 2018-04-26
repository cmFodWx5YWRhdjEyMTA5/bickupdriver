package com.app.bickupdriver.interfaces;

/**
 * Created by fluper-pc on 18/9/17.
 */

public interface HandleLoginSignUpNavigation {
    public void performSignIn();
    public void performSignUp();
    public void callSignupFragment();
    public void callSigninFragment();
    public void callForgotAndReset(int chechForgotReset,int changenumber);
    public void callMainActivity();

}
