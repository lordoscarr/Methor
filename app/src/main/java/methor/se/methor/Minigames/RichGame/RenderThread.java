package methor.se.methor.Minigames.RichGame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

/**
 * Created by Richard on 13-Dec-18.
 */

public class RenderThread extends Thread{

    private SurfaceHolder surfaceHolder;

    public RenderThread(SurfaceHolder surfaceHolder){
        this.surfaceHolder = surfaceHolder;
    }

    public void run(){
        Canvas canvas = surfaceHolder.lockCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawColor(Color.BLUE);
        canvas.drawCircle(300, 300, 100, paint);
        canvas.drawBitmap();

        surfaceHolder.unlockCanvasAndPost(canvas);
    }

}
