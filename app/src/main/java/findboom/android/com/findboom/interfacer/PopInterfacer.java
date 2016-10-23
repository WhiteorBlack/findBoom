package findboom.android.com.findboom.interfacer;

import android.os.Bundle;

/**
 * Created by Administrator on 2016/9/7.
 */
public interface PopInterfacer {
    void OnDismiss(int flag);

    void OnConfirm(int flag, Bundle bundle);

    void OnCancle(int flag);
}
