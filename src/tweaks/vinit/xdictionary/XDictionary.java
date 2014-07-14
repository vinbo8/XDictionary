package tweaks.vinit.xdictionary;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class XDictionary implements IXposedHookLoadPackage {

	int POPUP_TEXT_LAYOUT;
	int MAX_URI_LENGTH = 200;
	int selStart, selEnd;
	int width, height;
	Object mEditor;
	TextView mTextView, mDictTextView;
	ViewGroup mContentView;
	PopupWindow mPopupWindow;
	LayoutInflater mPopupInflater;
	Intent browserIntent;
	StringBuffer url;
	LinearLayout layout;
	LayoutParams params;
	PopupDisp p;
	BrowserDisp b;
	ToastDisp t;
	AssetManager am;

	public void handleLoadPackage(final LoadPackageParam lpparam)
			throws Throwable {

		findAndHookMethod("android.widget.Editor.ActionPopupWindow",
				lpparam.classLoader, "initContentView", new XC_MethodHook() {
					protected void afterHookedMethod(MethodHookParam param) {

						mEditor = XposedHelpers
								.getSurroundingThis(param.thisObject);
						mTextView = (TextView) XposedHelpers.getObjectField(
								mEditor, "mTextView");

						mContentView = (ViewGroup) XposedHelpers
								.getObjectField(param.thisObject,
										"mContentView");
						POPUP_TEXT_LAYOUT = XposedHelpers.getIntField(
								param.thisObject, "POPUP_TEXT_LAYOUT");

						LayoutInflater inflater = (LayoutInflater) mTextView
								.getContext().getSystemService(
										Context.LAYOUT_INFLATER_SERVICE);

						LayoutParams wrapContent = new LayoutParams(
								ViewGroup.LayoutParams.WRAP_CONTENT,
								ViewGroup.LayoutParams.WRAP_CONTENT);

						mDictTextView = (TextView) inflater.inflate(
								POPUP_TEXT_LAYOUT, null);
						mDictTextView.setLayoutParams(wrapContent);
						mContentView.addView(mDictTextView);
						mDictTextView.setText("Define");

						// CONSTRUCT POPUP

						mDictTextView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								XSharedPreferences pref = new XSharedPreferences(
										"tweaks.vinit.xdictionary", "my_prefs");

								int text = Integer.parseInt(pref.getString(
										"displayModeVal", "0"));
								selStart = mTextView.getSelectionStart();
								selEnd = mTextView.getSelectionEnd();

								String search_word = mTextView.getText()
										.subSequence(selStart, selEnd)
										.toString();

								switch (text) {
								case 1:

									b = new BrowserDisp(search_word, v
											.getContext());
									b.show();
									break;

								case 2:
									p = new PopupDisp(search_word, v
											.getContext());
									p.show();
									break;

								default:

									t = new ToastDisp(search_word, v
											.getContext());
									t.show();
									break;
								}

							}
						});
					}
				});
	}

}
