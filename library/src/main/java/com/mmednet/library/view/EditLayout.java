package com.mmednet.library.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmednet.library.R;
import com.mmednet.library.table.assign.VoiceTable;
import com.mmednet.library.util.StringUtils;
import com.mmednet.library.view.edit.EditView;
import com.mmednet.library.view.edit.IEditBox;
import com.mmednet.library.view.edit.IEditText;
import com.mmednet.library.view.edit.ISelectBox;
import com.mmednet.library.view.edit.OnItemEditListener;
import com.mmednet.library.view.edit.ViewFactory;
import com.mmednet.library.view.watcher.CursorTextWatcher;
import com.mmednet.library.view.watcher.LimitTextWatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Title:EditLayout
 * <p>
 * Description:多功能编辑框（编辑、复选、单选、下拉选）
 * </p>
 * Author Jming.L
 * Date 2017/9/22 15:32
 */
public class EditLayout extends LinearLayout implements VoiceTable {

    public static final int TYPE_EDITBOX = 0;       // 编辑框
    public static final int TYPE_TEXTBOX = 1;       // 文本框
    public static final int TYPE_RADIOBOX = 2;      // 单选框
    public static final int TYPE_CHECKBOX = 3;      // 复选框
    public static final int TYPE_SPINNER = 4;       // 下拉框
    public static final int TYPE_SINGLEBOX = 5;     // 单选框
    public static final int TYPE_MULTIBOX = 6;      // 多选框

    private OnItemEditListener mClickListener;

    private LinearLayout mTitleLayout;
    private TextView mTitleView;                    //标题控件
    private ImageView mRequiredView;                //必填控件
    private EditView mEditView;                     //编辑控件
    private Attribute mAttribute;

    public static class Attribute {
        public int viewType;                //编辑框类型
        public String title;                //标题
        public boolean editable;            //是否可以编辑
        public int limit;                   //限制的小数位数
        public float maxValue;              //输入框最大值
        public float minValue;              //输入框最小值
        public boolean isRequired;          //是否必填项

        public int requiredDrawable;        //必填星号

        public int horizontalSpacing;       //选择框横向内间距
        public int verticalSpacing;         //选择框纵向内间距

        public int leftSpace;               //编辑框左外间距
        public int topSpace;                //编辑框上外间距
        public int rightSpace;              //编辑框右外间距
        public int bottomSpace;             //编辑框下外间距

        public int titleSize;               //标题尺寸
        public int titleColor;              //标题颜色

        public int textSize;                //输出框文本尺寸
        public int textColor;               //输入框文本颜色
        public int hintColor;               //提示款颜色

        public String[] hints = {};         //待选文本
        public String[] texts = {};         //已选文本

        public int editTextBackground;      //文本框背景
        public int editBoxBackground;       //选择框背景
        public int selectBoxBackground;     //选择框背景

        public int editBoxHintColor;        //选择框待选色
        public int editBoxTextColor;        //选择框文本颜色

        public int inputType;               //输入框输入类型

        public int leftDrawable;            //左资源图片
        public String leftText;             //左资源文本
        public int rightDrawable;           //右资源图片
        public String rightText;            //右资源文本
    }

    public EditLayout(Context context) {
        this(context, null);
    }

    public EditLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mAttribute = new Attribute();
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        Resources.Theme theme = context.getTheme();
        TypedArray array = theme.obtainStyledAttributes(attrs, R.styleable.EditLayout, R.attr.editLayoutStyle, R.style.EditLayout);

        if (array != null) {
            //小数位数
            mAttribute.limit = array.getInt(R.styleable.EditLayout_limit, -1);
            //最大值
            mAttribute.maxValue = array.getFloat(R.styleable.EditLayout_maxValue, Float.MAX_VALUE);
            //最小值
            mAttribute.minValue = array.getFloat(R.styleable.EditLayout_maxValue, 0);
            //是否必填
            mAttribute.isRequired = array.getBoolean(R.styleable.EditLayout_required, false);

            //必填星号样式
            mAttribute.requiredDrawable = array.getResourceId(R.styleable.EditLayout_requiredDrawable, 0);

            //标题内容
            mAttribute.title = array.getString(R.styleable.EditLayout_titleText);

            //标题颜色
            mAttribute.titleColor = array.getColor(R.styleable.EditLayout_titleColor, Color.BLACK);

            //标题大小
            mAttribute.titleSize = (int) array.getDimension(R.styleable.EditLayout_titleSize, 24);

            //控件类型（先设置控件类型，否则会报空指针）
            mAttribute.viewType = array.getInt(R.styleable.EditLayout_viewType, TYPE_EDITBOX);

            //背景资源
            mAttribute.editTextBackground = array.getResourceId(R.styleable.EditLayout_editTextBackground, 0);
            mAttribute.editBoxBackground = array.getResourceId(R.styleable.EditLayout_editBoxBackground, 0);
            mAttribute.selectBoxBackground = array.getResourceId(R.styleable.EditLayout_selectBoxBackground, 0);

            //提示内容
            //String[] hints = null;
            String h = array.getString(R.styleable.EditLayout_hintText);
            if (h != null) {
                mAttribute.hints = h.split("\\|");
            }

            //正文内容
            //String[] texts = null;
            String t = array.getString(R.styleable.EditLayout_text);
            if (t != null) {
                mAttribute.texts = t.split("\\|");
            }

            //正文尺寸
            mAttribute.textSize = (int) array.getDimension(R.styleable.EditLayout_textSize, 24);

            //提示内容尺寸
            mAttribute.hintColor = array.getColor(R.styleable.EditLayout_hintColor, Color.GRAY);

            //正文颜色
            mAttribute.textColor = array.getColor(R.styleable.EditLayout_textColor, Color.BLACK);

            //存在EditBox属性则使用EditBox属性，不存在则使用默认属性
            mAttribute.editBoxHintColor = array.getColor(R.styleable.EditLayout_editBoxHintColor, -1);
            mAttribute.editBoxTextColor = array.getColor(R.styleable.EditLayout_editBoxTextColor, -1);

            //设置横向间距和纵向间距
            mAttribute.horizontalSpacing = (int) array.getDimension(R.styleable.EditLayout_editBoxHorizontalSpacing, 0);
            mAttribute.verticalSpacing = (int) array.getDimension(R.styleable.EditLayout_editBoxVerticalSpacing, 0);

            //是否可编辑
            mAttribute.editable = array.getBoolean(R.styleable.EditLayout_editable, true);

            //LeftDrawable支持文字和图片引用
            mAttribute.leftDrawable = array.getResourceId(R.styleable.EditLayout_leftDrawable, 0);
            if (mAttribute.leftDrawable == 0) {
                mAttribute.leftText = array.getString(R.styleable.EditLayout_leftDrawable);
            }

            //RightDrawable支持文字和图片引用
            //String rightText = null;
            mAttribute.rightDrawable = array.getResourceId(R.styleable.EditLayout_rightDrawable, 0);
            if (mAttribute.rightDrawable == 0) {
                mAttribute.rightText = array.getString(R.styleable.EditLayout_rightDrawable);
            }

            //标题大小
            int space = (int) array.getDimension(R.styleable.EditLayout_space, 0);
            mAttribute.leftSpace = (int) array.getDimension(R.styleable.EditLayout_leftSpace, space);
            mAttribute.topSpace = (int) array.getDimension(R.styleable.EditLayout_topSpace, 0);
            mAttribute.rightSpace = (int) array.getDimension(R.styleable.EditLayout_rightSpace, 0);
            mAttribute.bottomSpace = (int) array.getDimension(R.styleable.EditLayout_bottomSpace, 0);


            //输入类型
            mAttribute.inputType = array.getInt(R.styleable.EditLayout_dataType, EditorInfo.TYPE_CLASS_TEXT);

            array.recycle();
        }
        this.initView(context, mAttribute);
    }

    protected void initView(Context context, Attribute attr) {
        int padding = getResources().getDimensionPixelSize(R.dimen.size_title_padding);
        mTitleLayout = new LinearLayout(context);
        mTitleLayout.setPadding(padding, padding, padding, padding);
        mTitleLayout.setOrientation(LinearLayout.HORIZONTAL);

        // 标题
        mTitleView = new TextView(context);
        mTitleView.setIncludeFontPadding(false);
        mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttribute.titleSize);
        mTitleView.setTextColor(mAttribute.titleColor);

        // 是否必选
        LayoutParams requiredParams = new LayoutParams(
                mTitleView.getLineHeight() / 2, mTitleView.getLineHeight());
        requiredParams.setMarginEnd(5);
        mRequiredView = new ImageView(context);
        mRequiredView.setScaleType(ImageView.ScaleType.CENTER);
        mRequiredView.setImageResource(attr.requiredDrawable);
        mRequiredView.setLayoutParams(requiredParams);
        mRequiredView.setVisibility(View.INVISIBLE);

        mTitleLayout.addView(mRequiredView);
        mTitleLayout.addView(mTitleView);

        this.addView(mTitleLayout);
        this.setRequired(mAttribute.isRequired);
        //在设置类型以前不要进行类型判断
        this.setViewType(mAttribute.viewType);
    }

    private void initResource(Attribute attr) {
        this.setHint(attr.hints);
        this.setHintColor(attr.hintColor);
        this.setText(attr.texts);
        this.setTextSize(attr.textSize);
        this.setTextColor(attr.textColor);
        this.setInputType(attr.inputType);
        //优先判断图形资源，没有图形资源则选择文本
        if (attr.leftDrawable == 0) {
            if (attr.leftText != null) {
                this.setLeftDrawable(attr.leftText);
            }
        } else {
            this.setLeftDrawable(attr.leftDrawable);
        }

        if (attr.rightDrawable == 0) {
            if (attr.rightText != null) {
                this.setRightDrawable(attr.rightText);
            }
        } else {
            this.setRightDrawable(attr.rightDrawable);
        }
        //可否变为必须位于最后
        this.setEditable(attr.editable);
        this.limitNumber(attr.limit, attr.minValue, attr.maxValue);
    }

    //限制输入框的小数位数
    private void limitNumber(final int limit, final float minValue, final float maxValue) {
        if (getViewType() == TYPE_EDITBOX) {
            EditText editText = (EditText) getEditView();
            if (editText.isEnabled()) {
                if (limit < 0) {
                    editText.addTextChangedListener(new CursorTextWatcher(editText));
                } else {
                    editText.setKeyListener(new DigitsKeyListener(false, true));
                    editText.addTextChangedListener(new LimitTextWatcher(editText, limit, minValue, maxValue));
                }
            }
        }
    }

    /**
     * 条目点击监听
     *
     * @param listener 监听器
     */
    public void setOnItemClickListener(OnItemEditListener listener) {
        mClickListener = listener;
    }


    /**
     * 是否为必填项
     *
     * @return 布尔值
     */
    public boolean isRequired() {
        return mRequiredView.getVisibility() == View.VISIBLE;
    }

    /**
     * 是否为必填项
     *
     * @param isRequired true:必填项;false:非必填
     */
    public void setRequired(boolean isRequired) {
        mRequiredView.setVisibility(isRequired ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 设置标题字体大小
     *
     * @param size 字体大小
     */
    public void setTitleSize(int size) {
        mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    /**
     * 设置标题体字体颜色
     *
     * @param color 字体颜色
     */
    public void setTitleColor(int color) {
        mTitleView.setTextColor(color);
    }

    /**
     * 设置标题的内边距
     *
     * @param left   左内边距
     * @param top    上内边距
     * @param right  右内边距
     * @param bottom 下内边距
     */
    public void setTitlePadding(int left, int top, int right, int bottom) {
        mTitleLayout.setPadding(left, top, right, bottom);
    }

    /**
     * 设置文本尺寸
     *
     * @param size 文本尺寸
     */
    public void setTextSize(int size) {
        mEditView.setTxtSize(size);
    }

    /**
     * 设置文本颜色
     *
     * @param color 文本颜色
     */
    public void setTextColor(int color) {
        mEditView.setTxtColor(color);
    }

    /**
     * 设置提示颜色
     *
     * @param color 提示颜色
     */
    public void setHintColor(int color) {
        mEditView.setHintColor(color);
    }


    /**
     * 获取标题
     *
     * @return 标题内容
     */
    public String getTitle() {
        return mAttribute.title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        mAttribute.title = title;
        mTitleLayout.setVisibility(title == null ? View.GONE : View.VISIBLE);
        if (title == null) {
            return;
        }
        String blank = "\u0020\u0020";
        int viewType = getViewType();
        if (viewType == TYPE_EDITBOX
                || viewType == TYPE_TEXTBOX
                || viewType == TYPE_SPINNER) {
            if (!TextUtils.isEmpty(getTitle())) {
                if (mAttribute.editable) {
                    title = getTitle() + blank;
                } else {
                    if (TextUtils.isEmpty(StringUtils.removeBlank(getTitle()))) {
                        title = getTitle() + blank;
                    } else {
                        title = getTitle() + "\u0020:";
                    }
                }
            }
        } else {
            if (!TextUtils.isEmpty(getTitle())) {
                title = getTitle() + blank;
            }
        }
        mTitleView.setText(title);
    }

    /**
     * 获取控件类型
     *
     * @return 控件类型
     */
    public int getViewType() {
        return mAttribute.viewType;
    }

    /**
     * 获取是否必选控件
     *
     * @return 是否必选控件
     */
    public ImageView getRequiredView() {
        return mRequiredView;
    }

    /**
     * 获取标题控件
     *
     * @return 标题控件
     */
    public TextView getTitleView() {
        return mTitleView;
    }

    /**
     * 获取编辑控件
     *
     * @return 编辑控件
     */
    public EditView getEditView() {
        return mEditView;
    }

    /**
     * 设置编辑控件
     *
     * @param editView 编辑控件
     */
    public void setEditView(EditView editView) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = this.getChildAt(i);
            if (view != mTitleLayout) {
                removeView(view);
            }
        }
        mEditView = editView;
        //文本框可触发点击事件，如日历弹窗
        mEditView.setOnItemEditListener(new OnItemEditListener() {
            @Override
            public void onClick(String text, boolean isSelected) {
                if (mClickListener != null) {
                    mClickListener.onClick(text, isSelected);
                }
            }
        });

        this.setSpace(mAttribute.leftSpace, mAttribute.topSpace,
                mAttribute.rightSpace, mAttribute.bottomSpace);

        this.addView((View) mEditView);
    }

    /**
     * 设置控件类型
     *
     * @param type 类型EditLayout
     */
    public void setViewType(int type) {
        mAttribute.viewType = type;
        EditView editView = ViewFactory.create(getContext(), type);
        this.setEditView(editView);

        if (editView instanceof IEditText) {
            editView.setBackgroundView(mAttribute.editTextBackground);
        }

        //EditBox需要设置横向间距和纵向间距
        if (editView instanceof IEditBox) {
            if (mAttribute.editBoxHintColor != -1) {
                mAttribute.hintColor = mAttribute.editBoxHintColor;
            }
            if (mAttribute.editBoxTextColor != -1) {
                mAttribute.textColor = mAttribute.editBoxTextColor;
            }

            IEditBox box = (IEditBox) editView;
            box.setAverage(false);
            box.setBackgroundView(mAttribute.editBoxBackground);
            box.setHorizontalSpacing(mAttribute.horizontalSpacing);
            box.setVerticalSpacing(mAttribute.verticalSpacing);
        }

        if (editView instanceof ISelectBox) {
            final ISelectBox box = (ISelectBox) mEditView;
            final ViewTreeObserver vto = box.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    box.getViewTreeObserver().removeOnPreDrawListener(this);
                    box.setBackgroundView(mAttribute.selectBoxBackground);
                    initResource(mAttribute);
                    return true;
                }
            });
        } else {
            initResource(mAttribute);
        }

    }

    /**
     * 设置标题与编辑框的间隔
     */
    public void setSpace(int leftSpace, int topSpace, int rightSpace, int bottomSpace) {
        View view = (View) mEditView;
        LayoutParams params = (LayoutParams) view.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
        }
        params.setMargins(leftSpace, topSpace, rightSpace, bottomSpace);
        params.gravity = Gravity.CENTER_VERTICAL;
    }

    /**
     * 设置标题与编辑框的间隔
     *
     * @param leftSpace 间隔距离
     */
    public void setSpace(float leftSpace) {
        View view = (View) mEditView;
        LayoutParams params = (LayoutParams) view.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
        }
        params.setMarginStart((int) leftSpace);
        params.gravity = Gravity.CENTER_VERTICAL;
    }

    /**
     * 判断是否可编辑
     *
     * @return true:可以编辑;false:不可编辑
     */
    @Override
    public boolean isEditable() {
        return mAttribute.editable;
    }

    /**
     * 设置是否可编辑
     *
     * @param editable true:可以编辑;false:不可编辑
     */
    public void setEditable(boolean editable) {
        mAttribute.editable = editable;
        mEditView.setEditable(editable);
        this.setTitle(getTitle());
        //this.setSpace(mSpace);
    }

    /**
     * 内容是否被锁定范围
     *
     * @return true:存在被选项;false:不存在被选项
     */
    @Override
    public boolean isLimit() {
        int viewType = getViewType();
        return viewType == TYPE_CHECKBOX
                || viewType == TYPE_RADIOBOX
                || viewType == TYPE_MULTIBOX
                || viewType == TYPE_SINGLEBOX
                || viewType == TYPE_SPINNER;
    }

    /**
     * 添加提示内容或待选内容
     *
     * @param hint 提示内容
     */
    public void addHint(String... hint) {
        mEditView.addHint(hint);
    }

    /**
     * 设置提示内容或待选内容
     *
     * @param hint 提示内容
     */
    public void setHint(String... hint) {
        mEditView.setHint(hint);
    }

    /**
     * 设置提示内容或待选内容
     *
     * @param hint 提示内容集合
     */
    public void setHint(List<String> hint) {
        if (hint == null) {
            hint = new ArrayList<>();
        }
        String[] array = hint.toArray(new String[hint.size()]);
        mEditView.setHint(array);
    }

    /**
     * 获取提示内容或待选内容
     *
     * @return 提示内容或待选内容
     */
    public String getHint() {
        List<String> hints = mEditView.getHints();
        String text = null;
        for (String h : hints) {
            if (!TextUtils.isEmpty(h)) {
                text = h;
                break;
            }
        }
        return text;
    }

    /**
     * 获取提示内容或待选内容
     *
     * @return 提示内容或待选内容
     */
    @Override
    public List<String> getHints() {
        return mEditView.getHints();
    }


    /**
     * 设置文本内容或选择内容
     *
     * @param text 文本内容
     */
    public void setText(String... text) {
        mEditView.setTexts(text);
    }

    /**
     * 设置文本内容或选择内容
     *
     * @param text 文本内容集合
     */
    @Override
    public void setText(List<String> text) {
        if (text == null) {
            text = new ArrayList<>();
        }
        String[] array = text.toArray(new String[text.size()]);
        mEditView.setTexts(array);
    }

    /**
     * 获取文本内容或选择内容
     *
     * @return 文本内容
     */
    public String getText() {
        List<String> texts = mEditView.getTexts();
        String text = "";
        for (String t : texts) {
            if (!TextUtils.isEmpty(t)) {
                text = t;
                break;
            }
        }
        return text;
    }

    /**
     * 获取文本内容或选择内容
     *
     * @return 文本内容
     */
    @Override
    public List<String> getTexts() {
        return mEditView.getTexts();
    }

    /**
     * 设置编辑框右侧文本
     *
     * @param text 文本内容
     */
    public void setRightDrawable(String text) {
        mEditView.setRightDrawable(text);
    }

    /**
     * 设置编辑框右侧图形
     *
     * @param resId 图形资源ID
     */
    public void setRightDrawable(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        setRightDrawable(drawable);
    }

    /**
     * 设置编辑框右侧图形
     *
     * @param drawable 图形
     */
    public void setRightDrawable(Drawable drawable) {
        mEditView.setRightDrawable(drawable);
    }

    /**
     * 设置编辑框左侧文本
     *
     * @param text 文本内容
     */
    public void setLeftDrawable(String text) {
        mEditView.setLeftDrawable(text);
    }

    /**
     * 设置编辑框左侧图形
     *
     * @param resId 图形资源ID
     */
    public void setLeftDrawable(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        setLeftDrawable(drawable);
    }

    /**
     * 设置编辑框左侧图形
     *
     * @param drawable 图形
     */
    public void setLeftDrawable(Drawable drawable) {
        mEditView.setLeftDrawable(drawable);
    }

    /**
     * 设置编辑框输入类型
     *
     * @param type 类型InputType
     */
    public void setInputType(int type) {
        mEditView.setEditInputType(type);
    }

}
