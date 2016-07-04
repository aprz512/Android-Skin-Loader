package cn.feng.skin.manager.base;

import android.view.View;

import cn.feng.skin.manager.listener.IDynamicNewView;
import cn.feng.skin.manager.listener.ISkinUpdate;

/**
 * 作者：aprz on 2016/7/4.
 * 邮箱：aprz512@163.com
 */
public interface IActivitySkinHelper extends ISkinUpdate, IDynamicNewView {

    void onCreate();

    void onResume();

    void onDestroy();

    void dynamicAddView(View view, String attrName, int attrValueResId);

    void enableResponseOnSkinChanging(boolean enable);

}
