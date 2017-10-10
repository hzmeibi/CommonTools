package com.tools.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

/**
 * Created by milo on 17/9/14.
 */

public class TextLinkUtil {

    public TextLinkUtil(Context context, TextView textView, int color, String contentStr, String linkStr) {
        int start = contentStr.indexOf(linkStr);//若内容不存在返回-1
        int end = start + linkStr.length();
        if (start < 0 || end > contentStr.length()) {
            return;
        }

        SpannableStringBuilder builder = new SpannableStringBuilder(contentStr);
        //文字颜色
        ForegroundColorSpan blueColor = new ForegroundColorSpan(color);
        builder.setSpan(blueColor, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //设置点击事件....
        builder.setSpan(new NoLineClickSpan(context) {
            @Override
            public void onClick(View widget) {
                LogUtil.e("onClick");

            }
        }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(builder);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * Created by milo on 17/6/12.
     * 无下划线超链接，
     * 使用textColorLink、textColorHighlight分别修改超链接前景色和按下时的颜色
     */

    public class NoLineClickSpan extends ClickableSpan {
        private Context mContext;

        public NoLineClickSpan(Context context) {
            mContext = context;
        }

        public NoLineClickSpan() {
            super();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            /**set textColor**/
//        ds.setColor(mContext.getResources().getColor(R.color.app_blue));
            /**Remove the underline**/
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
        }
    }
}
