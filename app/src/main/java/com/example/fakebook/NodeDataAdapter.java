package com.example.fakebook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NodeDataAdapter extends RecyclerView.Adapter<NodeDataAdapter.NodeViewHolder> {

    private List<NodeData> nodeList;

    public NodeDataAdapter(List<NodeData> nodeList) {
        this.nodeList = nodeList;
    }


    @NonNull
    @Override
    public NodeDataAdapter.NodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new NodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NodeDataAdapter.NodeViewHolder holder, int position) {
        NodeData nodeData = nodeList.get(position);
        holder.tagNameTextView.setText("Tag: " + nodeData.getTagName());
        holder.innerTextTextView.setText("Text: " + nodeData.getInnerText());
    }

    public int getItemCount() {
        return nodeList.size();
    }


    public static class NodeViewHolder extends RecyclerView.ViewHolder {
        TextView tagNameTextView;
        TextView innerTextTextView;

        public NodeViewHolder(@NonNull View itemView) {
            super(itemView);
            tagNameTextView = itemView.findViewById(android.R.id.text1);
            innerTextTextView = itemView.findViewById(android.R.id.text2);
        }
    }
}
