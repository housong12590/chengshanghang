package com.jmm.csg.imgsel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jmm.csg.R;
import com.jmm.csg.imgload.ImageLoaderUtils;
import com.jmm.csg.imgsel.bean.ImageFolderBean;
import com.jmm.csg.imgsel.listener.OnRecyclerViewClickListener;

import java.util.List;

public class ImageFolderAdapter extends RecyclerView.Adapter<ImageFolderAdapter.ImageFolderHolder> {

    private OnRecyclerViewClickListener listener;
    private Context context;
    private List<ImageFolderBean> list;
    private final LayoutInflater inflater;

    public ImageFolderAdapter(Context context, List<ImageFolderBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ImageFolderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_folder_layout, parent, false);
        return new ImageFolderHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImageFolderHolder holder, int position) {
        ImageFolderBean item = list.get(position);
        holder.tvFileName.setText(item.fileName);
        holder.tvFileNum.setText(String.format(context.getResources().getString(R.string.photo_num), item.pisNum));
        ImageLoaderUtils.getInstance().loadImage(context, item.path, holder.ivImage);
        if (listener != null) {
            holder.containerView.setOnClickListener(view -> listener.onItemClick(view, holder.getAdapterPosition()));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnClickListener(OnRecyclerViewClickListener listener) {
        this.listener = listener;
    }

    public class ImageFolderHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvFileName;
        TextView tvFileNum;
        View containerView;

        public ImageFolderHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_icon);
            tvFileName = (TextView) itemView.findViewById(R.id.tv_file_name);
            tvFileNum = (TextView) itemView.findViewById(R.id.tv_pic_num);
            containerView = itemView.findViewById(R.id.root_view);
        }
    }
}
