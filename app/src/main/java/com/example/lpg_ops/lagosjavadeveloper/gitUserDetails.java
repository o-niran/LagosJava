package com.example.lpg_ops.lagosjavadeveloper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class gitUserDetails extends AppCompatActivity {

    public static String gitUserName;
    public static String avatarUrl;
    public static String gitUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git_user_details);
        updateUi();
    }

    public void shareUrl(View view) {
        TextView html = (TextView) findViewById(R.id.user_html);

        String message = "Check out this awesome developer @ " + gitUserName + " " + html.getText();
        Intent intents = new Intent(Intent.ACTION_SEND);
        intents.setType("text/plain");
        intents.putExtra(Intent.EXTRA_TEXT, message);
        Intent selectIntent = Intent.createChooser(intents, "Share");
        startActivity(selectIntent);
    }

    // Populating the Git User Details Activity with Data about the User
    public void updateUi() {
        gitUserName = getIntent().getStringExtra("userId");
        avatarUrl = getIntent().getStringExtra("avatarId");
        gitUrl = getIntent().getStringExtra("htmlId");
        CircleImageView imageView = (CircleImageView) findViewById(R.id.userAvatarId);
        Picasso.with(getBaseContext()).load(avatarUrl).placeholder(R.drawable.images).resize(80, 80).into(imageView);
        TextView nameView = (TextView) findViewById(R.id.user_name);
        nameView.setText(gitUserName);
        TextView htmlView = (TextView) findViewById(R.id.user_html);
        htmlView.setText(gitUrl);
    }
}
