package findboom.android.com.findboom.dailog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import findboom.android.com.findboom.R;

/**
 * Created by Administrator on 2016/11/15.
 */

public class CheckPhoto extends BasePopupwind {
    private View view;
    private ImageView imgPhoto;

    public CheckPhoto(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.check_photo_pop, null);
        view.findViewById(R.id.img_close).setOnClickListener(this);
        view.findViewById(R.id.img_delete).setOnClickListener(this);
        imgPhoto = (ImageView) view.findViewById(R.id.img_photo);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.setContentView(view);
        this.getBackground().setAlpha(255);
    }

    public void setPhoto(String picPath) {
        Glide.with(context).load(picPath).into(imgPhoto);
    }
    public void setDeleteInvis(){
        view.findViewById(R.id.img_delete).setVisibility(View.INVISIBLE);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.img_delete) {
            if (popInterfacer!=null)
                popInterfacer.OnCancle(flag);
        }
        dismiss();
    }
}
