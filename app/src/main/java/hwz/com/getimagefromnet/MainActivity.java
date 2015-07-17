package hwz.com.getimagefromnet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener
{

    private ImageView image,image2;
    private Bitmap bitmap1,bitmap2;
    private static Handler handler;
    private Button fromSDCard, fromHttp;
    private ProgressDialog dialog;
    private static String pathurl="";

    private static final int REQWIDTH = 400;
    private static final int REQHEIGHT = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        fromSDCard.setOnClickListener(this);
        fromHttp.setOnClickListener(this);

        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                dialog.cancel();
                if (msg.what == 1)
                {
                    try
                    {
                        image.setImageBitmap(bitmap1);
                        showToast(bitmap1.getWidth() + "," + bitmap1.getHeight());
                    } catch (Exception e)
                    {
                        showToast("加载图片失败...");
                    }

                }
                else if (msg.what == 2)
                {
                    try
                    {
                        image2.setImageBitmap(bitmap2);
                        showToast(bitmap2.getWidth() + "," + bitmap2.getHeight());
                    } catch (Exception e)
                    {
                        showToast("加载图片失败...");
                    }

                }
            }
        };
    }

    private void findViewById()
    {
        image = (ImageView) findViewById(R.id.showPictrue);
        image2 = (ImageView) findViewById(R.id.showLocalPictrue);
        fromHttp = (Button) findViewById(R.id.fromHttp);
        fromSDCard = (Button) findViewById(R.id.fromSDCard);
        dialog = ProgressDialog.show(MainActivity.this, "加载中...",
                "正在努力的加载中..请稍后");
        dialog.cancel();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.fromHttp:
                dialog.show();
                new Thread()
                {
                    public void run()
                    {
                        String url = getResources().getString(R.string.url);
                        try
                        {
                            bitmap1 = ZoomPictrue.decodeSampleBitmapFromBitmap(
                                    Downloader.downloadBitmap(url), REQWIDTH,
                                    REQHEIGHT);
                            //保存图片到本地
                            pathurl =System.currentTimeMillis()+ ".jpg";
                            System.out.println(pathurl);
                            int result = ZoomPictrue.saveFile(bitmap1,pathurl);
                            showToast(result+"");
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(1);
                    }
                }.start();
                break;
            case R.id.fromSDCard:
                dialog.show();
                new Thread()
                {
                    public void run()
                    {
                        try
                        {

                            bitmap2 = ZoomPictrue.decodeSampledBitmapFromSDCard(
                                    pathurl, REQWIDTH,
                                    REQHEIGHT);
                        } catch (Exception e1)
                        {
                        }
                        handler.sendEmptyMessage(2);
                    }

                    ;
                }.start();
                break;
        }
    }

    private void showToast(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
