package marion.marguerettaz.channelmessaging;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.*;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by marguerm on 27/01/2017.
 */
public class ChannelMessageAdapter extends ArrayAdapter<Message> {

    TextView textViewMessage;
    TextView textView2;
    ImageView imageView;
    ImageView imageViewEnvoyer;

    public ChannelMessageAdapter(Context context, int textViewResourceId, List<Message> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout2, parent, false);
        textViewMessage = (TextView) rowView.findViewById(R.id.textView);
        textViewMessage.setText(getItem(position).message);
        textView2 = (TextView) rowView.findViewById(R.id.textView2);
        textView2.setText(getItem(position).username);

        imageViewEnvoyer = (ImageView) rowView.findViewById(R.id.imageViewEnvoyer);

        if(getItem(position).messageImageUrl != null)
        {
            Bitmap btmEnvoyer = BitmapFactory.decodeFile(getItem(position).messageImageUrl);
            imageViewEnvoyer.setImageBitmap(btmEnvoyer);
        }

        final ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);

        if(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+getItem(position).username+".jpg").exists()) {
            Bitmap btm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+getItem(position).username+".jpg");
            if(btm != null)
                btm  = getRoundedCornerBitmap(btm);
            imageView.setImageBitmap(btm);
        }
        else {

            AsyncTaskStorage asyn = new AsyncTaskStorage(getContext(), getItem(position).imageUrl, getItem(position).username + ".jpg");
            asyn.setOnDownloadCompleteListener(new OnDownloadCompleteListener() {
                @Override
                public void onDownloadComplete(String result, int requestCode) {
                        Bitmap roundBtm = getRoundedCornerBitmap(BitmapFactory.decodeFile(result));
                        imageView.setImageBitmap(roundBtm);
                }
            });
            asyn.execute();
        }
        return rowView;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        if(bitmap != null) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);


            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();

            int width;
            int height;
            if (bitmap.getWidth() > bitmap.getHeight()) {
                width = bitmap.getWidth();
                height = bitmap.getHeight();
            } else {
                width = bitmap.getWidth();
                height = bitmap.getHeight();
            }

            final Rect rect = new Rect(0, 0, width, height);
            final RectF rectF = new RectF(rect);
            final float roundPx = 150;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        }
        return null;
    }

}
