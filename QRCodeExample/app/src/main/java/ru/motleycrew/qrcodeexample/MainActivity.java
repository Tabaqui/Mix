package ru.motleycrew.qrcodeexample;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivityTag";
    private static final int TIMEOUT_MILLIS = 250;

    private TextView mTextView;
    private Button mButton;
    private ImageView mImageView;
    private Bitmap mBitmap;
    private int mEncoded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.name_text);
        mButton = (Button) findViewById(R.id.generate_button);
        mButton.setOnClickListener(this);
        mImageView = (ImageView) findViewById(R.id.qr_image);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != R.id.generate_button || mTextView.getText() == null || mTextView.getText().length() == 0) {
            return;
        }
        Date datePart = new Date();
        while (mEncoded < 4) {
            Log.d(TAG, "Date part is " + datePart.getTime() + mEncoded * TIMEOUT_MILLIS);
            encode(mTextView.getText().toString() + String.valueOf(datePart.getTime() + mEncoded * TIMEOUT_MILLIS), getX(), getY());
            mEncoded++;
        }
        mImageView.setImageBitmap(mBitmap);
    }

    private void encode(String source, int x, int y) {
        if (mBitmap == null) {
            mBitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);
        }
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix matrix = writer.encode(source, BarcodeFormat.QR_CODE, 100, 100);
            for (int i = 0; i < matrix.getWidth(); i++) {
                for (int j = 0; j < matrix.getHeight(); j++) {
                    int color = matrix.get(i, j) ? Color.BLACK : Color.WHITE;
                    mBitmap.setPixel(x + i, y + j, color);
                }
            }
//            mImageView.setImageBitmap(bitmap);
        } catch (WriterException we) {
            Log.e(TAG, "Failed encoding string: " + we);
        }
    }

    private int getX() {
        if (mEncoded == 0 || mEncoded == 2) {
            return 0;
        }
        if (mEncoded == 1 || mEncoded == 3) {
            return 100;
        }
        return -1;
    }

    private int getY() {
        if (mEncoded == 0 || mEncoded == 1) {
            return 0;
        }
        if (mEncoded == 2 || mEncoded == 3) {
            return 100;
        }
        return -1;
    }

}
