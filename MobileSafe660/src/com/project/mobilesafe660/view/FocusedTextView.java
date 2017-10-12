package com.project.mobilesafe660.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

/**
 * 可以强制获取焦点的TextView
 * @author 袁星明
 */
public class FocusedTextView extends TextView {

	//当布局文件中具有属性和样式style时,系统底层解析时,就会走此构造方法
	public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		System.out.println("FocusedTextView调用了带样式的构造方法");
	}

	//当布局文件中具有属性时,系统底层解析时,就会走此构造方法
	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		System.out.println("FocusedTextView调用了带属性的构造方法");
	}

	//从代码中new对象
	public FocusedTextView(Context context) {
		super(context);
		System.out.println("FocusedTextView调用了无任何属性样式的构造方法");
	}
	
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		return true;//强制让TextView具有焦点
	}

}
