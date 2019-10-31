package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.d3ifcool.debtreminder.EditDeletePeminjamanActivity;
import org.d3ifcool.debtreminder.R;
import java.util.ArrayList;
import entities.Pembayaran;

public class PembayaranListAdapter extends RecyclerView.Adapter<PembayaranListAdapter.viewHolder>{
    private Context mContext;
    private ArrayList<Pembayaran> listPembayaran;

    public PembayaranListAdapter(Context mContext, ArrayList<Pembayaran> listPembayaran) {
        this.mContext = mContext;
        this.listPembayaran = listPembayaran;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_view_pembayaran_layout,viewGroup,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        final Pembayaran pembayaran = listPembayaran.get(i);

        viewHolder.tvPay.setText(String.valueOf(pembayaran.getPay()));
        viewHolder.tvDatePay.setText(pembayaran.getDatePay());
        viewHolder.tvDescription.setText(pembayaran.getDescription());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,EditDeletePeminjamanActivity.class);
                intent.putExtra("pembayaran", pembayaran);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPembayaran.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        TextView tvPay;
        TextView tvDatePay;
        TextView tvDescription;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tvPay = itemView.findViewById(R.id.tv_pay);
            tvDatePay = itemView.findViewById(R.id.tv_datePay);
            tvDescription = itemView.findViewById(R.id.tv_description);
        }
    }
}
