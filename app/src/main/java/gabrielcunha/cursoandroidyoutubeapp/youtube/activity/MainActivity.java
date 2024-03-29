package gabrielcunha.cursoandroidyoutubeapp.youtube.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import gabrielcunha.cursoandroidyoutubeapp.youtube.R;
import gabrielcunha.cursoandroidyoutubeapp.youtube.adapter.AdapterVideo;
import gabrielcunha.cursoandroidyoutubeapp.youtube.api.YoutubeService;
import gabrielcunha.cursoandroidyoutubeapp.youtube.helper.RetrofitConfig;
import gabrielcunha.cursoandroidyoutubeapp.youtube.helper.YoutubeConfig;
import gabrielcunha.cursoandroidyoutubeapp.youtube.listener.RecyclerItemClickListener;
import gabrielcunha.cursoandroidyoutubeapp.youtube.model.Item;
import gabrielcunha.cursoandroidyoutubeapp.youtube.model.Resultado;
import gabrielcunha.cursoandroidyoutubeapp.youtube.model.Video;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    //Widgets
    private RecyclerView recyclerVideos;
    private SwipeRefreshLayout refreshLayout;
    private MaterialSearchView searchView;

    private List<Item> videos = new ArrayList<>();
    private Resultado resultado;

    private AdapterVideo adapterVideo;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerVideos= findViewById(R.id.recyclerVideos);
        refreshLayout = findViewById(R.id.refresh);
        searchView = findViewById(R.id.searchView);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recuperarVideos("");
            }
        });

        //Configurações iniciais Retrofit
          retrofit = RetrofitConfig.getRetrofit();

        


        //Recupera vídeos
        recuperarVideos("");



        //Configura metodos para SearchView
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recuperarVideos(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });

        //Configura Toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ana Nunes ");
        setSupportActionBar(toolbar);

    }

    private void recuperarVideos(String pesquisa){

        String q = pesquisa.replace(" ","+");

        YoutubeService youtubeService = retrofit.create(YoutubeService.class);

        youtubeService.recuperarVideos(
                "snippet","date","12",
                YoutubeConfig.CHAVE_YOUTUBE_API,YoutubeConfig.CANAL_ID,q

        ).enqueue(new Callback<Resultado>() {
            @Override
            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                //Log.d("resultado", "resultado: " +response.toString());
                if(response.isSuccessful()){
                    resultado = response.body();
                    videos = resultado.items;
                    configurarRecyclerView();
                    refreshLayout.setRefreshing(false);

                }
            }

            @Override
            public void onFailure(Call<Resultado> call, Throwable t) {

            }
        });
    }

    public void configurarRecyclerView(){
        adapterVideo = new AdapterVideo(videos,this);
        recyclerVideos.setHasFixedSize(true);
        recyclerVideos.setLayoutManager(new LinearLayoutManager(this));
        recyclerVideos.setAdapter(adapterVideo);

        //Configura evento de clique

        recyclerVideos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerVideos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Item video = videos.get(position);
                                String idVideo = video.id.videoId;

                                Intent i = new Intent(MainActivity.this,PlayerActivity.class);
                                i.putExtra("idVideo",idVideo);
                                startActivity(i);

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);

        MenuItem item = menu.findItem(R.id.menu_search);
        searchView.setMenuItem(item);
        return true;
    }
}
