package com.codepath.apps.restclienttemplate.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "DetailsActivity";
    Tweet tweet;

    TextView tvBody;
    TextView tvScreenName;
    TextView tvHandle;
    TextView tvTimeStamp;
    ImageView ivProfileImage;
    ImageView ivTweetPhoto;

    ImageButton iBtnReply;
    ImageButton iBtnRetweet;

    TwitterClient client;

    public DetailsActivity() {
        this.tweet = new Tweet();
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

        iBtnReply = (ImageButton) findViewById(R.id.iBtnReply);
        iBtnRetweet = (ImageButton) findViewById(R.id.iBtnLike);

        client = TwitterApp.getRestClient(this);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        tvBody.setText(tweet.body);
        tvScreenName.setText(tweet.user.screenName);
        tvHandle.setText("@"+tweet.user.name);
        tvTimeStamp.setText(Tweet.getRelativeTimeAgo(tweet.createdAt));
        Glide.with(this).load(tweet.user.profileImageUrl).into(ivProfileImage);
        Glide.with(this).load(tweet.imagePath).transform(new CenterInside(), new RoundedCorners(50)).into(ivTweetPhoto);
    }

    public void onReplyClick(View v) {
        Intent intent = new Intent(this, ComposeActivity.class);
        intent.putExtra("replyUser", Parcels.wrap(tweet.user.screenName));
        this.startActivity(intent);
        Log.i("DetailsActivity", "Reply launched");
    }

    public void onRetweetClick(View v) {
        // Make API call to twitter to publish the tweet
        client.sendTweet(tweet.body, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess to publish tweet");
                Log.i(TAG, "Published tweet says: " + tweet.body);
                Intent intent = new Intent();
                intent.putExtra("tweet", Parcels.wrap(tweet));
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure to publish tweet");
            }
        });
    }

}