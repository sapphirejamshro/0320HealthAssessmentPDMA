package com.sapphire.HealthAssessmentPDMA.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.bean.PreventionRVAdapterBean;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;

import java.util.List;

public class PreventionRVHeaderAdapter extends RecyclerView.Adapter<PreventionRVHeaderAdapter.PreventionRVHeaderAdapterViewHolder>{

    private Context context;
    private List<PreventionRVAdapterBean> beanList;

    private String selectedLanguage="";
    public PreventionRVHeaderAdapter(Context context, List<PreventionRVAdapterBean> beanList){
        this.context = context;
        this.beanList = beanList;

        selectedLanguage = new UserSession(context).getSelectedLanguage();
    }

    @NonNull
    @Override
    public PreventionRVHeaderAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(context).inflate(R.layout.rv_prevention_header_adapter_item,parent,false);
        return new PreventionRVHeaderAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreventionRVHeaderAdapterViewHolder holder, int position) {

        holder.iconImgV.setImageResource(beanList.get(position).getImg());

    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class PreventionRVHeaderAdapterViewHolder extends RecyclerView.ViewHolder{


        private ImageView iconImgV;
        public PreventionRVHeaderAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            iconImgV = itemView.findViewById(R.id.imgViewRvHeaderAdapterItem);

        }
    }

}
