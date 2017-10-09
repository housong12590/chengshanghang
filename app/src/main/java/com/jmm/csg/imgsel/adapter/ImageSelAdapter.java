package com.jmm.csg.imgsel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jmm.csg.R;
import com.jmm.csg.imgload.ImageLoaderUtils;
import com.jmm.csg.imgsel.bean.ImageFolderBean;
import com.jmm.csg.imgsel.core.ImageSelectObservable;
import com.jmm.csg.imgsel.listener.OnRecyclerViewClickListener;

import java.util.List;

public class ImageSelAdapter extends RecyclerView.Adapter<ImageSelAdapter.ImageSelHolder> {

    private int maxCount;
    private Context context;
    private boolean isSingleImage;
    private List<ImageFolderBean> list;
    private List<ImageFolderBean> selectList;
    private OnRecyclerViewClickListener onClickListener;
    private LayoutInflater mInflater;

    public ImageSelAdapter(Context context, List<ImageFolderBean> list, boolean isSingleImage, int maxCount) {
        this.context = context;
        this.list = list;
        this.maxCount = maxCount;
        this.isSingleImage = isSingleImage;
        mInflater = LayoutInflater.from(context);
        selectList = ImageSelectObservable.getInstance().getSelectImages();
    }

    @Override
    public ImageSelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_image_layout, parent, false);
        return new ImageSelHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageSelHolder holder, int position) {
        ImageFolderBean bean = list.get(position);
        bean.position = holder.getAdapterPosition();

        notifyImageChanged(holder.ivPicture, bean);
        notifyCheckChanged(holder, bean);

        setSelectOnClickListener(holder.selectView, bean, holder.getAdapterPosition());
        setOnItemClickListener(holder.containerView, holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<ImageFolderBean> getData() {
        return list;
    }

    private void notifyImageChanged(ImageView imageView, ImageFolderBean imageBean) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoaderUtils.getInstance().loadImage(context, imageBean.path, imageView);
    }

    private void notifyCheckChanged(ImageSelHolder viewHolder, ImageFolderBean imageBean) {
        if (isSingleImage) {
            viewHolder.checked.setVisibility(View.INVISIBLE);
        } else {
            if (selectList.contains(imageBean)) {
                viewHolder.checked.setEnabled(true);
                viewHolder.checked.setText(String.valueOf(imageBean.selectPosition));
                viewHolder.ivForground.setVisibility(View.VISIBLE);
            } else {
                viewHolder.checked.setEnabled(false);
                viewHolder.checked.setText("");
                viewHolder.ivForground.setVisibility(View.GONE);
            }
        }
    }

    private void setOnItemClickListener(View view, final int position) {
        view.setOnClickListener(v -> {
            if (onClickListener != null) {
                if (isSingleImage) {
                    selectList.add(list.get(position));
                }
                onClickListener.onItemClick(v, position);
            }
        });
    }

    private void subSelectPosition() {
        for (int index = 0, len = selectList.size(); index < len; index++) {
            ImageFolderBean folderBean = selectList.get(index);
            folderBean.selectPosition = index + 1;
            notifyItemChanged(folderBean.position);
        }
    }

    public void setOnClickListener(OnRecyclerViewClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private void setSelectOnClickListener(View view, final ImageFolderBean imageBean, final int position) {
        view.setOnClickListener(v -> {
            if (selectList.contains(imageBean)) {
                selectList.remove(imageBean);
                subSelectPosition();
            } else {
                if (selectList.size() >= maxCount) {
                    Toast.makeText(context, context.getResources().getString(R.string.publish_select_photo_max, maxCount),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                selectList.add(imageBean);
                imageBean.selectPosition = selectList.size();
            }
            notifyItemChanged(position);
            if (onClickListener != null) {
                onClickListener.onItemClick(v, -1);
            }
        });
    }


    public class ImageSelHolder extends RecyclerView.ViewHolder {

        ImageView ivPicture;
        View containerView;
        ImageView ivForground;
        TextView checked;
        TextView selectView;

        public ImageSelHolder(View itemView) {
            super(itemView);
            containerView = itemView.findViewById(R.id.main_frame_layout);
            ivPicture = (ImageView) itemView.findViewById(R.id.iv_picture);
            checked = (TextView) itemView.findViewById(R.id.tv_select);
            ivForground = (ImageView) itemView.findViewById(R.id.iv_forgound);
            selectView = (TextView) itemView.findViewById(R.id.tv_select_v);
        }
    }
}
