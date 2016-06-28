package cn.feng.skin.manager.entity;

import android.view.View;

public abstract class SkinAttr {

    protected static final String RES_TYPE_NAME_COLOR = "color";
    protected static final String RES_TYPE_NAME_DRAWABLE = "drawable";

    /**
     * name of the attr, ex: background or textSize or textColor
     * <p/>
     * 属性名 例如：android:textSize="18sp" 的textSize
     */
    public String attrName;

    /**
     * id of the attr value refered to, normally is [2130745655]
     * <p/>
     * android:textColor="@color/color_new_item_synopsis"
     * color_new_item_synopsis 对应的id
     */
    public int attrValueRefId;

    /**
     * entry name of the value , such as [app_exit_btn_background]
     * <p/>
     * android:textColor="@color/color_new_item_synopsis"
     * color 的名字 --- color_new_item_synopsis
     */
    public String attrValueRefName;

    /**
     * type of the value , such as color or drawable
     * <p/>
     * 资源的类型
     */
    public String attrValueTypeName;

    /**
     * Use to apply view with new TypedValue
     *
     * @param view
     */
    public abstract void apply(View view);

    @Override
    public String toString() {
        return "SkinAttr \n[\nattrName=" + attrName + ", \n"
                + "attrValueRefId=" + attrValueRefId + ", \n"
                + "attrValueRefName=" + attrValueRefName + ", \n"
                + "attrValueTypeName=" + attrValueTypeName
                + "\n]";
    }
}
