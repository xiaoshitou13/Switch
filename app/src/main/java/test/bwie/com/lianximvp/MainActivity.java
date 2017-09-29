package test.bwie.com.lianximvp;

import android.os.Handler;
import android.service.autofill.Dataset;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> list= new ArrayList<>();

    private SwipeRefreshLayout  swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Myadater myadater;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);

        //创建数据
        for(int  i = 0 ; i < 20 ;i ++){
            list.add("第"+i+"条数据");
        }

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        myadater = new Myadater();
        recyclerView.setAdapter(myadater);



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> newDatas = new ArrayList<String>();
                        for(int j = 0 ; j < 5; j++){
                            int index = j + 1;
                            newDatas.add("new item" + index);
                        }
                        myadater.addItem(newDatas);
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(MainActivity.this, "更新了", Toast.LENGTH_SHORT).show();
                    }
                },3000);
            }
        });

        //下拉加载

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int  i =    linearLayoutManager.findFirstVisibleItemPosition();
                if(newState ==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 ==myadater.getItemCount()){
                  new Handler().postDelayed(new Runnable() {
                      @Override
                      public void run() {
                          List<String> newDatas = new ArrayList<String>();
                          for (int i = 0; i< 5; i++) {
                              int index = i +1;
                              newDatas.add("more item" + index);
                          }
                          myadater.addMore(newDatas);
                          myadater.notifyDataSetChanged();
                      }
                  },2000);
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView,dx, dy);
                lastVisibleItem =linearLayoutManager.findLastVisibleItemPosition();
            }

        });

    }




    class  Myadater extends RecyclerView.Adapter<Myadater.ViewHolder>{
        private static final int TYPE_ITEM =0;  //普通Item View

        private static final int TYPE_FOOTER = 1;  //顶部FootView
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new ViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.item,parent,false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.text.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list!=null?list.size():0;
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            TextView text;
            public ViewHolder(View itemView) {
                super(itemView);
                text =(TextView) itemView.findViewById(R.id.tv);
            }
        }

        class Hander extends RecyclerView.ViewHolder{

            TextView t;
            public Hander(View itemView) {
                super(itemView);
                t = itemView.findViewById(R.id.tv1);
            }
        }

        //   下拉刷新
        public void addItem(List<String> newDatas){
            newDatas.addAll(list);
            list.removeAll(list);
            list.addAll(newDatas);
            notifyDataSetChanged();
        }

        //  上拉加载
        public void addMore(List<String>  Dataset){

            list.addAll(Dataset);
            notifyDataSetChanged();
        }


        @Override
        public int getItemViewType(int position) {
            if(position + 1  ==getItemCount()){
                return TYPE_FOOTER;
            }
            return TYPE_ITEM;
        }


    }
}
