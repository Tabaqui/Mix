package ru.motleycrew.yetnotmvp;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.motleycrew.yetnotmvp.model.User;
import ru.motleycrew.yetnotmvp.util.AvatarDownloader;

/**
 * Created by User on 22.03.2016.
 */
public class UsersFragment extends Fragment {

    private static final String TAG = "UsersFragment";

    public static Fragment getInstance() {
        return new UsersFragment();
    }

    private List<User> mUsers;
    private AvatarDownloader<UserHolder> mDownloader;
    private boolean mGetting;

    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsers = new ArrayList<>();
        mDownloader = new AvatarDownloader<>();
        mDownloader.setDownloadListener(new AvatarDownloader.DownloadListener<UserHolder>() {
            @Override
            public void onDownloaded(UserHolder holder, Bitmap bitmap) {
                if (isAdded()) {
                    Drawable avatar = new BitmapDrawable(getResources(), bitmap);
                    holder.bind(avatar);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_users, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_users_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getFriendList();
        setupAdapter();
        return v;
    }

    private void getFriendList() {
        if (mGetting || AccessToken.getCurrentAccessToken() == null || Profile.getCurrentProfile() == null) {
            return;
        }
        mGetting = true;
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/invitable_friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONArray friendIds = response.getJSONObject().getJSONArray("data");
                            for (int i = 0; i < friendIds.length(); i++) {
                                User u = new User();
                                u.setFaceId(friendIds.getJSONObject(i).getString("id"));
                                u.setName(friendIds.getJSONObject(i).getString("name"));
                                JSONObject pictureJsonData = friendIds.getJSONObject(i)
                                        .getJSONObject("picture")
                                        .getJSONObject("data");
                                u.setUrl(pictureJsonData.getString("url"));
                                mUsers.add(u);
                            }
                            setupAdapter();
                        } catch (JSONException ex) {
                            Log.e(TAG, "Failed parse JSON", ex);
                        } finally {
                            mGetting = false;
                        }
                    }
                }
        ).executeAsync();
    }

    private void setupAdapter() {
        if (isAdded()) {
            mRecyclerView.setAdapter(new UserAdapter(mUsers));
        }
    }

    private class UserAdapter extends RecyclerView.Adapter<UserHolder> {

        private List<User> mUsers;

        public UserAdapter(List<User> users) {
            mUsers = users;
        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }

        @Override
        public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.item_users, parent, false);
            return new UserHolder(v);
        }

        @Override
        public void onBindViewHolder(UserHolder holder, int position) {
            User user = mUsers.get(position);
            holder.bind(user);
            mDownloader.start(user.getUrl(), holder);
            // Drawable drawable = new BitmapDrawable(getResources(), thumbnail);
            // get bitmap
        }
    }

    private class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private User mUser;
        private ImageView mUserAvatar;
        private TextView mUserName;

        public UserHolder(View userView) {
            super(userView);
            mUserAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            mUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
        }

        @Override
        public void onClick(View v) {

        }

        public void bind(User user) {
            mUser = user;
            mUserName.setText(user.getName());
        }

        public void bind(Drawable avatar) {
            mUserAvatar.setImageDrawable(avatar);
        }
    }
}
