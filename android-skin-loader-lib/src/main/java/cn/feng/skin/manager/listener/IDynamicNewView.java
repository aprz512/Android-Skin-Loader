package cn.feng.skin.manager.listener;

import android.view.View;

import java.util.List;

import cn.feng.skin.manager.entity.DynamicAttr;

public interface IDynamicNewView {
    void dynamicAddView(View view, List<DynamicAttr> pDAttrs);
}
