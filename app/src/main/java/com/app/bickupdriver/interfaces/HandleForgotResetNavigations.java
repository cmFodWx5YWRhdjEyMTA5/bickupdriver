package com.app.bickupdriver.interfaces;

import android.os.Bundle;

/**
 * Created by fluper-pc on 28/9/17.
 */

public interface HandleForgotResetNavigations {

    public void  callOTPFragment(int changeNumber);
    public void  callResetFragment();
    public void  callForgotFragment(int changeNumber);

    public void  callFragment(int callFragment);

    public void  handleResetpasswordResult(Bundle data);


}
