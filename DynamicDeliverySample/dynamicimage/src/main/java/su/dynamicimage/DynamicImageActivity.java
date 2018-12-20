package su.dynamicimage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import su.dynamicdeliverysample.BaseDynamicActivity;

public class DynamicImageActivity extends BaseDynamicActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ImageDataSet mDataSet;
    private TextView mTextView;
    private NativeTextLoaderJNI mNativeTextLoaderJNI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_image);
        setup();
    }

    void setup(){
        mTextView = findViewById(R.id.HelloNativeTextId);
        mNativeTextLoaderJNI = new NativeTextLoaderJNI();
        mTextView.setText(mNativeTextLoaderJNI.getText());
        mDataSet = new ImageDataSet();
        mRecyclerView = findViewById(R.id.ListId);
        mAdapter = new ImageAdapter(mDataSet);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.notifyDataSetChanged();
    }

    private class ImageAdapter extends RecyclerView.Adapter<ImageCellViewHolder>{
        private ImageDataSet mData;

        ImageAdapter(ImageDataSet imageDataSet){
            mData = imageDataSet;
        }

        @NonNull
        @Override
        public ImageCellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View child = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cellview_image, parent, false);
            return new ImageCellViewHolder(child);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageCellViewHolder imageCellViewHolder, int i) {
            imageCellViewHolder.setImage(mData.getImage(i));
        }

        @Override
        public int getItemCount() {
            return mData.getSize();
        }
    }

    private class ImageCellViewHolder extends RecyclerView.ViewHolder{

        private View mRootView;
        private ImageView mImageView;

        ImageCellViewHolder(@NonNull View itemView) {
            super(itemView);
            mRootView = itemView;
            mImageView = mRootView.findViewById(R.id.image_id);
        }

        void setImage(int resId){
            mImageView.setBackgroundResource(resId);
        }
    }

}
