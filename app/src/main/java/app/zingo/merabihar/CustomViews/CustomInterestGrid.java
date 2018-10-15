package app.zingo.merabihar.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by ZingoHotels Tech on 11-10-2018.
 */

public class CustomInterestGrid extends GridView {

    public CustomInterestGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomInterestGrid(Context context) {
        super(context);
    }

    public CustomInterestGrid(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
