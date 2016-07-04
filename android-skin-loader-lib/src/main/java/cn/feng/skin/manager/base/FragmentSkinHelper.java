package cn.feng.skin.manager.base;

import android.content.Context;
import android.view.View;

import java.util.List;

import cn.feng.skin.manager.entity.DynamicAttr;
import cn.feng.skin.manager.listener.IDynamicNewView;

/**
 * 作者：aprz on 2016/6/30.
 * 邮箱：aprz512@163.com
 * <p>
 * 简化继承
 */
public class FragmentSkinHelper implements IFragmentSkinHelper, IDynamicNewView {

    private IDynamicNewView mIDynamicNewView;

    @Override
    public void onAttach(Context context) {
        try {
            mIDynamicNewView = (IDynamicNewView) context;
        } catch (ClassCastException e) {
            mIDynamicNewView = null;
        }
    }

    @Override
    public void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
        if (mIDynamicNewView == null) {
            throw new RuntimeException("IDynamicNewView should be implements !");
        } else {
            mIDynamicNewView.dynamicAddView(view, pDAttrs);
        }
    }

}
