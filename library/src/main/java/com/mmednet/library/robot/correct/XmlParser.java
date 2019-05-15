package com.mmednet.library.robot.correct;

import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Xml;

import com.mmednet.library.Library;
import com.mmednet.library.log.Logger;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class XmlParser {

    private static final String TEXT = "text";
    private static final String VALUE = "value";
    private static final String NAME = "name";
    private static final String LIMIT = "limit";
    private static final String TAG = "XmlParser";

    public static ArrayList<TextBean> parse(String fileName) {
        TextBean text = null;
        ArrayList<TextBean> texts = null;
        Library library = Library.getInstance();
        AssetManager manager = library.getContext().getAssets();
        XmlPullParser parser = Xml.newPullParser();
        InputStream input = null;
        try {
            input = manager.open(fileName);
            parser.setInput(input, "utf-8");
            int type = parser.getEventType();
            ArrayList<String> values = null;
            while (type != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_DOCUMENT:
                        texts = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (TextUtils.equals(TEXT, name)) {
                            values = new ArrayList<>();
                            text = new TextBean();
                            text.setKey(parser.getAttributeValue(null, NAME));
                        } else if (TextUtils.equals(VALUE, name)) {
                            if (values != null)
                                values.add(parser.nextText());
                            if (parser.getEventType() != XmlPullParser.END_TAG)
                                parser.nextTag();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (TEXT.equals(parser.getName())) {
                            if (text != null)
                                text.setValues(values);
                            texts.add(text);
                        }
                        break;
                    default:
                        break;
                }
                type = parser.next();
            }
        } catch (Exception e) {
            Logger.e(TAG, Standard.FILE_NAME + " is error in the assets." + e.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return texts;
    }
}
