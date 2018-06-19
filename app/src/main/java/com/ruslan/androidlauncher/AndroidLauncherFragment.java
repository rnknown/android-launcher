package com.ruslan.androidlauncher;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by rusla on 08.03.2016.
 */
public class AndroidLauncherFragment extends Fragment {

    private RecyclerView mRecyclerView;

    public static AndroidLauncherFragment newInstance() {
        return new AndroidLauncherFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_android_launcher, container, false);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.fragment_android_launcher_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupAdapter();
        return v;
    }

    private void setupAdapter() {
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo lhs, ResolveInfo rhs) {
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(lhs.loadLabel(pm).toString(),
                        rhs.loadLabel(pm).toString());
            }
        });
        mRecyclerView.setAdapter(new ActivityAdapter(activities));
    }

    private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private RelativeLayout fContainer;
        private ResolveInfo mResolveInfo;
        private TextView mNameTextView;
        private ImageView mImageView;
        private TextView mNameLetterTextView;

        public ActivityHolder(View itemView) {
            super(itemView);
            fContainer = (RelativeLayout) itemView.findViewById(R.id.item_container);
            mNameTextView = (TextView)itemView.findViewById(R.id.list_item_text_view);
            mImageView = (ImageView)itemView.findViewById(R.id.list_item_image_view);
            mNameLetterTextView = (TextView)itemView.findViewById(R.id.list_item_text_letter);
            fContainer.setOnClickListener(this);
        }

        public void bindActivity (ResolveInfo resolveInfo) {
            mResolveInfo = resolveInfo;
            PackageManager pm = getActivity().getPackageManager();
            String appName = mResolveInfo.loadLabel(pm).toString();
            Drawable dr =  mResolveInfo.loadIcon(pm);
            mNameTextView.setText(appName);
            mImageView.setImageDrawable(dr);
            mNameLetterTextView.setText((""+appName.charAt(0)).toUpperCase());
        }

        @Override
        public void onClick(View v) {
            ActivityInfo activityInfo = mResolveInfo.activityInfo;
            Intent i = new Intent(Intent.ACTION_MAIN)
                    .setClassName(
                    activityInfo.applicationInfo.packageName, activityInfo.name)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }


    }

    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder> {
        private final List<ResolveInfo> mActivities;

        public ActivityAdapter(List<ResolveInfo> activities) {
            mActivities = activities;
        }

        @Override
        public ActivityHolder onCreateViewHolder (ViewGroup parent, int type) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item, parent,
                    false);
            return new ActivityHolder(view);
        }

        @Override
        public void onBindViewHolder (ActivityHolder activityHolder, int position) {
            ResolveInfo resolveInfo = mActivities.get(position);
            activityHolder.bindActivity(resolveInfo);
        }

        @Override
        public int getItemCount () {
            return mActivities.size();
        }
    }
}
