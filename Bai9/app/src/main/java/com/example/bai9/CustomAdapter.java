package com.example.bai9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Contact> {
    private Context context;
    private int resource;
    private List<Contact> arrContact;

    public CustomAdapter(Context context, int resource, List<Contact> arrContact) {
        super(context, resource, arrContact);
        this.context = context;
        this.resource = resource;
        this.arrContact = arrContact;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.tvId = convertView.findViewById(R.id.tvMaSinhVien);
            holder.tvAvatar = convertView.findViewById(R.id.tvAvatar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Contact c = arrContact.get(position);
        holder.tvAvatar.setBackgroundColor(c.getColor());
        holder.tvAvatar.setText(String.valueOf(position + 1));
        holder.tvName.setText(c.getName());
        holder.tvId.setText(c.getIdStudent());
        return convertView;
    }

    static class ViewHolder {
        TextView tvName, tvId, tvAvatar;
    }
}

