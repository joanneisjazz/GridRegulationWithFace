package com.jstech.gridregulation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jstech.gridregulation.ConstantValue;
import com.jstech.gridregulation.R;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.RegulateResultBean;

import java.util.List;

public class CheckResult2Adapter extends RecyclerView.Adapter<CheckResult2Adapter.ViewHolder> {
    MethodInterface listener;
    List<RegulateResultBean> data;
    private Context context;


    public CheckResult2Adapter(MethodInterface listener, List<RegulateResultBean> data, Context context) {
        this.context = context;
        this.listener = listener;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_site_check, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvContent.setText(data.get(position).getItemcontent());
        final int nowpostion = position;
        //检查方法
        holder.tvCheckMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.showMethod(data.get(nowpostion).getItemid());
                }
            }
        });
        String result = data.get(position).getInspresult();

        if (ConstantValue.RESULT_BASIC_QUALIFIED.equals(result)) {
            setSelectStyle(holder.tvBasicQualified, holder.tvQualified, holder.tvUnqualified);
            holder.tvUnqualifiedReason.setVisibility(View.GONE);
        } else if (ConstantValue.RESULT_QUALIFIED.equals(result)) {
            setSelectStyle(holder.tvQualified, holder.tvBasicQualified, holder.tvUnqualified);
            holder.tvUnqualifiedReason.setVisibility(View.GONE);
        } else if (ConstantValue.RESULT_UNQUALIFIED.equals(result)) {
            setSelectStyle(holder.tvUnqualified, holder.tvBasicQualified, holder.tvQualified);
            holder.tvUnqualifiedReason.setVisibility(View.VISIBLE);
            if (null == data.get(position).getInspdesc() || "".equals(data.get(position).getInspdesc())) {
                holder.tvUnqualifiedReason.setText(context.getResources().getString(R.string.unqualified_reason) + "无");
            } else {
                holder.tvUnqualifiedReason.setText(context.getResources().getString(R.string.unqualified_reason) + data.get(position).getInspdesc());
            }
        } else {
            setUnSelectStyle(holder.tvQualified, holder.tvBasicQualified, holder.tvUnqualified);
            holder.tvUnqualifiedReason.setVisibility(View.GONE);
        }

        //检查结果--合格
        holder.tvQualified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setSelectStyle(tvQualified, tvBasicQualified, tvUnqualified);
                listener.selectResult(ConstantValue.RESULT_QUALIFIED, data.get(nowpostion));

            }
        });

        //检查结果--基本合格
        holder.tvBasicQualified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setSelectStyle(tvBasicQualified, tvQualified, tvUnqualified);
                listener.selectResult(ConstantValue.RESULT_BASIC_QUALIFIED, data.get(nowpostion));

            }
        });

        //检查结果--不合格
        holder.tvUnqualified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.selectResult(ConstantValue.RESULT_UNQUALIFIED, data.get(nowpostion));
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface MethodInterface {
        void showMethod(String id);

        void selectResult(String result, RegulateResultBean data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        TextView tvCheckMethod;
        TextView tvTitle;
        TextView tvQualified;
        TextView tvBasicQualified;
        TextView tvUnqualified;
        TextView tvUnqualifiedReason;

        public ViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvCheckMethod = itemView.findViewById(R.id.tv_method);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvQualified = itemView.findViewById(R.id.tv_qualified);
            tvBasicQualified = itemView.findViewById(R.id.tv_basic_qualified);
            tvUnqualified = itemView.findViewById(R.id.tv_unqualified);
            tvUnqualifiedReason = itemView.findViewById(R.id.tv_unqualified_reason);
        }
    }

    /**
     * 设置选中和未选中的样式
     */
    private void setSelectStyle(TextView tvSeleted, TextView tvUnSeleted1, TextView tvSeleted2) {
        tvSeleted.setBackground(context.getResources().getDrawable(R.drawable.bg_check_item_result_selected));
        tvUnSeleted1.setBackground(context.getResources().getDrawable(R.drawable.bg_check_item_result_unselect));
        tvSeleted2.setBackground(context.getResources().getDrawable(R.drawable.bg_check_item_result_unselect));
    }

    /**
     * 设置未选中的样式
     *
     * @param tvSeleted
     * @param tvUnSeleted1
     * @param tvSeleted2
     */
    private void setUnSelectStyle(TextView tvSeleted, TextView tvUnSeleted1, TextView tvSeleted2) {
        tvSeleted.setBackground(context.getResources().getDrawable(R.drawable.bg_check_item_result_unselect));
        tvUnSeleted1.setBackground(context.getResources().getDrawable(R.drawable.bg_check_item_result_unselect));
        tvSeleted2.setBackground(context.getResources().getDrawable(R.drawable.bg_check_item_result_unselect));
    }
}
