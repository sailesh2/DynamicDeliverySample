package su.dynamicimage;

import su.dynamicimage.R;

import java.util.ArrayList;

public class ImageDataSet {

    private ArrayList<Integer> mResources;

    public ImageDataSet(){
        init();
    }

    private void init(){
        mResources = new ArrayList<>();
        mResources.add(R.drawable.mountain1);
        mResources.add(R.drawable.mountain2);
        mResources.add(R.drawable.mountain3);
        mResources.add(R.drawable.mountain4);
        mResources.add(R.drawable.mountain5);
        mResources.add(R.drawable.mountain6);
        mResources.add(R.drawable.mountain7);
        mResources.add(R.drawable.mountain8);
        mResources.add(R.drawable.mountain9);
        mResources.add(R.drawable.mountain10);
    }

    public int getImage(int index){
        return mResources.get(index);
    }

    public ArrayList<Integer> getImageResources(){
        return mResources;
    }

    public int getSize(){
        return mResources.size();
    }
}
