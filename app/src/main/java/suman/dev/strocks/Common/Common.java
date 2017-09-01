package suman.dev.strocks.Common;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by suman on 6/8/17.
 */

public class Common {

    private Context mContext;
    private ProgressDialog pd;

    public Common(Context context){
        mContext = context;
    }

    public void ShowPleaseWait()
    {
        if(pd!=null)
            return;
        pd= new ProgressDialog(mContext);
        pd.setTitle("Please wait...");
        pd.setCancelable(false);
        pd.show();
    }
    public void HidePleaseWait()
    {
        if(pd!=null)
            pd.dismiss();
    }

}
