package com.ninguno.simpletwitter;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.TweetViewAdapter;

import java.util.List;

import io.fabric.sdk.android.Fabric;

public class UserTimeLineActivity extends ListActivity {

    public static final int DEFAULT_USERID_VALUE = -1;
    private long userId;
    //TweetTimelineListAdapter adapter
    TweetViewAdapter adapter;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_time_line);
        receiveUserId();
        initializeFabricAppConnection();
        initializeComponents();
    }

    private void receiveUserId() {
        Intent intent = getIntent();
        userId = intent.getLongExtra(TimeLineActivity.USER_ID, DEFAULT_USERID_VALUE);
    }

    private void initializeComponents() {
        /*final SearchTimeline searchTimeline = new SearchTimeline.Builder()
                .query("#patata")
                .build();
        UserTimeline userTimeline = new UserTimeline.Builder().screenName("manucasariego").build();
        CollectionTimeline timeline = new CollectionTimeline.Builder()
                .id(569961150045896704L)
                .build();*/

        /*adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(timeline)
                .build();*/


        adapter = new TweetViewAdapter(this);
        loadTweets();
        setListAdapter(adapter);

    }

    private void initializeFabricAppConnection() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TimeLineActivity.TWITTER_KEY, TimeLineActivity.TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
    }

    public void loadTweets() {
        final StatusesService service = Twitter.getInstance().getApiClient().getStatusesService();

        service.homeTimeline(200, null, null, null, true, null, null, new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> result) {
                        adapter.setTweets(result.data);
                        msg = result.data.get(0).user.name + " escribió: " + result.data.get(0).text;
                        Toast.makeText(UserTimeLineActivity.this, msg, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(TwitterException error) {
                        Toast.makeText(UserTimeLineActivity.this, "Failed to retrieve timeline",
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }


}
