package com.projects.diwanshusoni.happyapp.viewHolders;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.projects.diwanshusoni.happyapp.R;
import com.projects.diwanshusoni.happyapp.pojos.PojoSubmitedTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Aniket on 10/3/2017.
 */

public class PostviewHolder extends RecyclerView.ViewHolder
{

    private TextView tv_userName, tv_date, tv_exp, tv_taskHead;
    private ImageView ivTaskImage;
    private CircleImageView cv_profilePic;
    View mview;

    int width, hieght;

    private void setScreenWidth() {

    }

    Context context ;

    public PostviewHolder(View itemview)
    {
        super(itemview);
        mview=itemView;
        context = mview.getContext();

        tv_date = mview.findViewById(R.id.dateposted_id);
        tv_userName = mview.findViewById(R.id.username_id);
        tv_exp = mview.findViewById(R.id.text_tskexp_id);
        tv_taskHead = mview.findViewById(R.id.text_tskheading_id);
        ivTaskImage = mview.findViewById(R.id.iv_tskimg_id);
        cv_profilePic = mview.findViewById(R.id.profile_pic_id);

    }
    public void setData(PojoSubmitedTask pojoSubmitedTask){
        tv_date.setText(pojoSubmitedTask.getDateSubmittedOn());
        tv_userName.setText(pojoSubmitedTask.getUserName());
        tv_exp.setText(pojoSubmitedTask.getTaskExperience());
        if (pojoSubmitedTask.getTaskImageUrl() != null){
            Picasso.with(context)
                    .load(pojoSubmitedTask.getTaskImageUrl())
                    .resize(400, 400)
                    .into(ivTaskImage);
        }
        if (!pojoSubmitedTask.getUserProfilePicImageUrl().toString().equals("null")){
            Picasso.with(context)
                    .load(pojoSubmitedTask.getUserProfilePicImageUrl())
                    .into(cv_profilePic);
        }
        else {
            cv_profilePic.setImageResource(R.drawable.logo);
        }
    }
    /*public void setUsername(String username)
    {
        TextView username2=(TextView)mview.findViewById(R.id.username);
        username2.setText(username);

    }
    public void setText(String text)
    {
        TextView text1=(TextView)mview.findViewById(R.id.text);
        text1.setText(text);
    }
    public void setImage(Context ctx,String image)
    {
        CircleImageView profile_pic_view = (CircleImageView)mview.findViewById(R.id.profile_pic);
        Picasso.with(ctx).load(image).into(profile_pic_view);
    }*/
}
