package com.example.newsapp.DocBao;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newsapp.Card.CardTrangChu_Adapter;
import com.example.newsapp.Card.ClickItem;
import com.example.newsapp.Card.NoiDungModel;
import com.example.newsapp.R;
import com.example.newsapp.TruyenDuLieu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class docbaovideo extends AppCompatActivity {

    String linkbao,video,tieude,tgian,ndung,tacgia;
    TextView tv_tieude,tv_tgian,tv_ndung,tv_tacgia;
    VideoView vv;
    RecyclerView rcv_lienquan;

    CardTrangChu_Adapter cardTrangChu_adapter;
    List<NoiDungModel> noiDungModelList = new ArrayList<>();

    Elements data,data1;
    Document document;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docbaovideo);

        linkbao = TruyenDuLieu.Truyen_Linkbao;

        khaibao();

        Content content = new Content();
        content.execute();


    }

    private void khaibao() {
        vv = findViewById(R.id.img_anhbao_docbaovideo);
        tv_tieude = findViewById(R.id.txt_tieude_docbaovideo);
        tv_tgian = findViewById(R.id.txt_tgian_docbaovideo);
        tv_ndung = findViewById(R.id.txt_tieude2_docbaovideo);
        tv_tacgia = findViewById(R.id.txt_tacgia_docbaovideo);
        rcv_lienquan = findViewById(R.id.rcv_tinlienquan_docbaovideo);

    }

    private class Content extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            tv_tieude.setText(tieude);
            tv_tgian.setText(tgian);
            tv_ndung.setText(ndung);
            tv_tacgia.setText(tacgia);

            /*MediaController mediacontroller = new MediaController(docbaovideo.this);
            mediacontroller.setAnchorView(vv);
            vv.setMediaController(mediacontroller);
            vv.setVideoURI(Uri.parse(video));
            vv.start();*/

            cardTrangChu_adapter = new CardTrangChu_Adapter((ArrayList<NoiDungModel>) noiDungModelList, new ClickItem() {
                @Override
                public void onClickItem(NoiDungModel noiDungModel) {
                    Intent intent = new Intent(docbaovideo.this, docbaovideo.class);
                    startActivity(intent);
                }
            });
            rcv_lienquan.setAdapter(cardTrangChu_adapter);
            cardTrangChu_adapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = linkbao;
                Log.e("url", url);
                document = Jsoup.connect(url).get();

                //đổ dữ liệu cho phần báo - chưa
                data = document.select("div.content");
                video = data.select("div.column-first-second>div.main-content-body>div.content").select("div.VCSortableInPreviewMode[type='photo']").select("img").attr("src");
                tieude = data.select("div.content-detail>div.w980>h1.article-title").text();
                tgian = data.select("div.content-detail>div.w980>div.date-time").text();
                ndung = data.select("div.column-first-second>div.main-content-body>div.content>p").text();
                tacgia = data.select("div.column-first-second>div.main-content-body>div.author").text();

                //đổ dữ liệu cho rcv liên quan - chưa
                data1 = document.select("ul.list-news-content").select("li.news-item");
                int size = data1.size();
                for (int i=0; i<size;i++) {
                    String tieude = data1.select("h3.title-news").eq(i).text();
                    String thoigian = data1.select("p.sapo").eq(i).text();
                    String anhbao = data1.select("a.img212x132.pos-rlt").eq(i).select("img").attr("src");
                    linkbao = "https://tuoitre.vn" + data.select("a.img212x132.pos-rlt").eq(i).attr("href");
                    noiDungModelList.add(new NoiDungModel(tieude,thoigian,anhbao,linkbao));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}