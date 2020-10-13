package br.edu.dmos5.github_dmos5.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.dmos5.github_dmos5.R;
import br.edu.dmos5.github_dmos5.model.Repository;

/**
 * @author vinicius.montouro
 */
public class ItemRepositoryAdapter extends RecyclerView.Adapter<ItemRepositoryAdapter.RepositoryViewHolder> {

    private List<Repository> repositories;

    private static RecyclerItemClickListener clickListener;

    public ItemRepositoryAdapter(@NonNull List<Repository> repositories){
        this.repositories = repositories;
    }

    @NonNull
    @Override
    public RepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);
        RepositoryViewHolder holder = new RepositoryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryViewHolder holder, int position) {
        holder.repositoryName.setText(repositories.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    public void setClickListener(RecyclerItemClickListener clickListener) {
        ItemRepositoryAdapter.clickListener = clickListener;
    }

    public class RepositoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView repositoryName;

        public RepositoryViewHolder(@NonNull View itemView){
            super(itemView);
            repositoryName = itemView.findViewById(R.id.item_recicler_view_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null)
                clickListener.onItemClick(getAdapterPosition());
        }
    }
}