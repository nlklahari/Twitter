package com.codepath.apps.restclienttemplate.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {

    Tweet tweet;

    TextView tvBody;
    TextView tvScreenName;
    TextView tvHandle;
    TextView tvTimeStamp;
    ImageView ivProfileImage;
    ImageView ivTweetPhoto;

    public DetailsActivity() {
        this.tweet = tweet;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvBody = (TextView) findViewById(R.id.tvBody);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        tvHandle = (TextView) findViewById(R.id.tvHandle);
        tvTimeStamp = (TextView) findViewById(R.id.tvTimeStamp);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        ivTweetPhoto = (ImageView) findViewById(R.id.ivTweetImage);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        tvBody.setText(tweet.body);
        tvScreenName.setText(tweet.user.screenName);
        tvHandle.setText("@"+tweet.user.name);
        tvTimeStamp.setText(Tweet.getRelativeTimeAgo(tweet.createdAt));
        Glide.with(this).load(tweet.user.profileImageUrl).into(ivProfileImage);
        Glide.with(this).load(tweet.imagePath).transform(new CenterInside(), new RoundedCorners(50)).into(ivTweetPhoto);
    }
}