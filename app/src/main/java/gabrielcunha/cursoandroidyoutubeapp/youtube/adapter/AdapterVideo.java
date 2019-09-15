package gabrielcunha.cursoandroidyoutubeapp.youtube.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import gabrielcunha.cursoandroidyoutubeapp.youtube.R;
import gabrielcunha.cursoandroidyoutubeapp.youtube.model.Item;
import gabrielcunha.cursoandroidyoutubeapp.youtube.model.Video;

public class AdapterVideo extends RecyclerView.Adapter<AdapterVideo.MyViewHolder> {

    private List<Item> videos = new ArrayList<>();
    private Context context;

    public AdapterVideo(List<Item> videos, Context context) {
        this.videos = videos;
        this.context = context;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView titulo;
        TextView data;
        ImageView capa;
        TextView descricao;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.textTitulo);
            capa   = itemView.findViewById(R.id.imageCapa);
            descricao   = itemView.findViewById(R.id.textDescricao);
        }
    }

      @NonNull
      @Override
      public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_video,parent,false);

          return new AdapterVideo.MyViewHolder(view);

      }


      @Override
      public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Item video = videos.get(position);
        holder.titulo.setText(video.snippet.title);
        holder.descricao.setText(video.snippet.description);
        String url = video.snippet.thumbnails.high.url;
        Picasso.get().load(url).into(holder.capa);
        //holder.titulo.setText(video.id.videoId);
      }

      @Override
      public int getItemCount() {
          return videos.size();
      }
  }
