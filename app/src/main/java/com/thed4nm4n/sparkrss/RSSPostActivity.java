package com.thed4nm4n.sparkrss;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.toolbox.NetworkImageView;
import com.thed4nm4n.sparkrss.handlers.ImageLoaderHandler;
import com.thed4nm4n.sparkrss.types.RSSPost;

public class RSSPostActivity extends AppCompatActivity {

    private RSSPost post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rsspost);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            post = extras.getSerializable("post", RSSPost.class);
        }

        NetworkImageView post_image = findViewById(R.id.post_image);
        post_image.setImageUrl(post.getImageUrl(), ImageLoaderHandler.getInstance(this).getImageLoader());

        TextView titleText = findViewById(R.id.post_title);
        TextView descText = findViewById(R.id.post_desc);
        TextView linkText = findViewById(R.id.post_link);

        titleText.setText(post.getTitle());
        descText.setText(post.getDescription());

        CardView linkCard = findViewById(R.id.link_card);

        Uri uri = Uri.parse(post.getUrl());
        linkText.setText(uri.getHost());

        linkCard.setOnClickListener(v -> {
            Log.d("TESTING", "ONCLICK LISTENED");
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(i);
        });
    }
}