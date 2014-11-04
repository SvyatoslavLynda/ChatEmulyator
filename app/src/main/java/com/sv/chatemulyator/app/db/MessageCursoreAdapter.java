package com.sv.chatemulyator.app.db;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sv.chatemulyator.app.R;

/**
 * Created by Created by IntelliJ IDEA.
 * User: SV
 * Date: 03.11.2014
 * Time: 0:42
 * For the ChatEmulyator project.
 */
public class MessageCursoreAdapter extends CursorAdapter {

    public MessageCursoreAdapter(Context context, Cursor c) {
        super(context, c, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.message_view, parent, false);

        return retView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView message = (TextView) view.findViewById(R.id.message);
        TextView timeStamp = (TextView) view.findViewById(R.id.timeStemp);
        LinearLayout root = (LinearLayout) view.findViewById(R.id.root);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(3))) == 0) {
            lp.gravity = Gravity.LEFT;
            lp.rightMargin = 30;
            root.setBackgroundResource(R.drawable.my_message_background);
        } else {
            lp.gravity = Gravity.RIGHT;
            lp.leftMargin = 30;
            root.setBackgroundResource(R.drawable.any_message_background);
        }
        root.setLayoutParams(lp);
        message.setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1)))));
        timeStamp.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));
    }
}
