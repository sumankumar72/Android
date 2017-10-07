package suman.dev.strocks.Common;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import java.util.List;

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


    public static boolean isDownloadManagerAvailable(Context context) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setClassName("com.android.providers.downloads.ui","com.android.providers.downloads.ui.DownloadList");
            List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);

            return list.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

}
