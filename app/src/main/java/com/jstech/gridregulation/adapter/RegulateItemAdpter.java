package com.jstech.gridregulation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jstech.gridregulation.R;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CheckTableBean;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.entity.CheckTableEntity;

import java.util.ArrayList;
import java.util.List;

public class RegulateItemAdpter extends BaseExpandableListAdapter {


    private LayoutInflater mInflater;
    private Context mContext;
    private List<CheckTableEntity> data;
    private ClickInterface anInterface;

    public RegulateItemAdpter(Context mContext, List<CheckTableEntity> data) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.data = data;
    }

    public RegulateItemAdpter(Context mContext, List<CheckTableEntity> data, ClickInterface anInterface) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.data = data;
        this.anInterface = anInterface;
    }

    // 刷新数据
    public void flashData(ArrayList<CheckTableEntity> datas) {
        this.data = datas;
        this.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (null == data.get(groupPosition).getCheckItemBeans()) {
            return 0;
        }
        return data.get(groupPosition).getCheckItemBeans().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getCheckItemBeans().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * 选择检查表
     *
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TableViewHolder tableViewHolder;
        if (null == convertView) {
            tableViewHolder = new TableViewHolder();
            convertView = mInflater.inflate(R.layout.item_check_table_select, parent, false);
            tableViewHolder.tvTable = convertView.findViewById(R.id.tv_content);
            tableViewHolder.ckbTable = convertView.findViewById(R.id.ckb_select);
            convertView.setTag(tableViewHolder);
        } else {
            tableViewHolder = (TableViewHolder) convertView.getTag();
        }

        if (isExpanded) {

        } else {

        }


        final int postion = groupPosition;
        tableViewHolder.tvTable.setText(data.get(groupPosition).getName());

//        if (!data.get(groupPosition).isExpanded()) {
//            tableViewHolder.ckbTable.setVisibility(View.GONE);
//        } else {


        if (data.get(groupPosition).isExpanded()) {
            /**
             * 展开状态下
             */
            tableViewHolder.ckbTable.setVisibility(View.VISIBLE);
            if (data.get(groupPosition).isAllSected()) {
                tableViewHolder.ckbTable.setImageResource(R.drawable.ic_selected);
            } else {
                tableViewHolder.ckbTable.setImageResource(R.drawable.ic_unselect);
            }
        } else {
            /**
             * 关闭状态下
             */
            if (data.get(groupPosition).isSelected()) {
                tableViewHolder.ckbTable.setVisibility(View.VISIBLE);
                tableViewHolder.ckbTable.setImageResource(R.drawable.ic_selected);
            } else {
                tableViewHolder.ckbTable.setVisibility(View.GONE);
            }
        }


        tableViewHolder.ckbTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != anInterface) {
                    anInterface.selectAll(postion);
                }
            }
        });
        return convertView;
    }

    /**
     * 选择检查项
     *
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ItemViewHolder itemViewHolder;
        if (null == convertView) {
            itemViewHolder = new ItemViewHolder();
            convertView = mInflater.inflate(R.layout.item_check_item_select, parent, false);
            itemViewHolder.tvItem = convertView.findViewById(R.id.tv_content);
            itemViewHolder.ckbItem = convertView.findViewById(R.id.ckb_select);
//            itemViewHolder.layoutFather = convertView.findViewById(R.id.layout_item);
            convertView.setTag(itemViewHolder);
        } else {
            itemViewHolder = (ItemViewHolder) convertView.getTag();
        }
        itemViewHolder.tvItem.setText(data.get(groupPosition).getCheckItemBeans().get(childPosition).getContent());
        if (data.get(groupPosition).getCheckItemBeans().get(childPosition).getIsSelected()) {
            itemViewHolder.ckbItem.setImageResource(R.drawable.ic_selected);
        } else {
            itemViewHolder.ckbItem.setImageResource(R.drawable.ic_unselect);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private class TableViewHolder {
        TextView tvTable;
        //        CheckBox ckbTable;
        ImageView ckbTable;

    }

    private class ItemViewHolder {
        TextView tvItem;
        ImageView ckbItem;
    }

    public interface ClickInterface {
        //        void table(int groupPosition);
        void selectAll(int groupPosition);
//        void item(int groupPosition, int childPosition);
    }

}
