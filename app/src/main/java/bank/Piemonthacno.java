package bank;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.bounside.mj.cashiya.AddCategory;
import com.bounside.mj.cashiya.DividerItemDecoration;
import com.bounside.mj.cashiya.R;
import shared_pref.UserInformation;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import adapter.Movie;
import adapter.MoviesAdapter;
import recyclercustomlistener.RecyclerItemClickListener;
import sqlitedatabase.Bankmessagedb;

/**
 * Created by Neeraj Sain on 11/19/2016.
 */

public class Piemonthacno extends AppCompatActivity implements OnChartValueSelectedListener {

    List<String> list;

    private PieChart mChart;
    int lengthofarray;

    String month[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    float perc[], perc1[],amount[];
    String  key;

    String months;
    String acno,acnm;

    ArrayList<String> xVals;

    public Calendar calendar;
    String addadys;

    ArrayList<Integer> colors = new ArrayList<Integer>();

    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    String dbmoney,selectQuery;
    private List<Movie> movieList;
    int id = R.mipmap.ic_launcher;

    SQLiteDatabase sql;
    ContentValues can;
    Bankmessagedb bbd ;
    Cursor cursor;
    Movie movie;
    LinearLayout linearnorecord,linearrecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pie_month_acno);

        mChart = (PieChart)findViewById(R.id.chart1acno);
        linearrecord = (LinearLayout) findViewById(R.id.linearspendsacno);
        linearnorecord = (LinearLayout)  findViewById(R.id.linearnospendsacno);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_spendsacno);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Bundle extras = getIntent().getExtras();

        key = extras.getString("category");
        acno = extras.getString("acno");
        acnm = extras.getString("anm");
        Log.i("dgbigubd",""+key);
        Log.i("dgbigubd",""+acno);
        Log.i("dgbigubd",""+acnm);
        calendar= Calendar.getInstance();

        SimpleDateFormat month_year1 = new SimpleDateFormat("yyyy");
        addadys = month_year1.format(calendar.getTime());
        bbd = new Bankmessagedb(this);
        sql = bbd.getWritableDatabase();
        can = new ContentValues();

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        // lv = (ListView) findViewById(R.id.listmonth);

        list = new ArrayList<String>(UserInformation.set);

//getting total amount or expense from database according to categories
            bbd.gettransCategoryacno(key,acno,acnm);
            bbd.gettransFoodacno(key,acno,acnm);
            bbd.gettransFuelacno(key,acno,acnm);
            bbd.gettransShoppingacno(key,acno,acnm);
            bbd.gettransElectricityacno(key,acno,acnm);
            bbd.gettransTravelacno(key,acno,acnm);
            bbd.gettransEntertainmentacno(key,acno,acnm);
            bbd.gettransGroceriesacno(key,acno,acnm);
            bbd.gettransHealthacno(key,acno,acnm);
            bbd.gettransOtheracno(key,acno,acnm);
            bbd.gettransextraacno(key,acno,acnm); //SPENT CATEGORY
            bbd.gettransdebitacno(key,acno,acnm);
            bbd.gettranspurcacno(key,acno,acnm);
            bbd.gettranscreditacno(key,acno,acnm);
            bbd.gettransavailableacno(key,acno,acnm);

            SimpleDateFormat month_date = new SimpleDateFormat("MMM");
//            SimpleDateFormat month_date1 = new SimpleDateFormat("dd/MM/yyyy");// or "MMM" for short month name

            Calendar c = Calendar.getInstance();

            String month_name = month_date.format(c.getTime());
            getPositionlabel();
            calculatePercentage();
            getAllRecords();

            mChart.setUsePercentValues(true);
            mChart.setDescription("");
            mChart.setExtraOffsets(0, 10, 5, 5);

            mChart.setDragDecelerationFrictionCoef(4.95f);
            //lengthofarray = array1.length;
            lengthofarray = UserInformation.arracno.length;
            Log.i("broading", " length array String " + lengthofarray);
            mChart.setCenterText(generateCenterSpannableText());

            mChart.setDrawHoleEnabled(true);
            mChart.setHoleColor(Color.WHITE);

            mChart.setTransparentCircleColor(Color.WHITE);
            mChart.setTransparentCircleAlpha(110);

            mChart.setHoleRadius(40f);
            mChart.setTransparentCircleRadius(43f);

            mChart.setDrawCenterText(true);

            mChart.setRotationAngle(0);
            mChart.setRotationEnabled(true);
            mChart.setHighlightPerTapEnabled(true);
            mChart.setOnChartValueSelectedListener(this);

            setData(lengthofarray, 100);

            mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
            mChart.setDrawSliceText(false);
            // mChart.spin(2000, 0, 360);

            Legend l = mChart.getLegend();
            l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(0f);
            l.setYOffset(3f);

            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            //fetching item by position
                            try {
                                Movie movie = movieList.get(position);

                                //   HashMap<String,String> map =(HashMap<String,String>)recyclerView.getAdapter(position-1);
                                Intent intent = new Intent(Piemonthacno.this, AddCategory.class);
                                intent.putExtra("account",movie.getTitle());
                                intent.putExtra("date", movie.getYear());
                                intent.putExtra("amount", movie.getGenre());
                                intent.putExtra("cat", movie.geticon());
                                intent.putExtra("type", movie.getType());
                                startActivity(intent);
                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                        }
                    })
            );
        }

    private void getPositionlabel() {
        int pos = 0;
        Log.i("postrfrtf","...."+month.length);
        Log.i("postrfrtf","...."+months);
        for (int i = 0; i < month.length; i++) {
            Log.i("postrfrtf","...."+month[i]);
            if(months.equals(month[i]))
            {
                pos = i;
                Log.i("posyertydf","...."+pos);
            }
        }
    }

    private void getAllRecords() {

        movieList = new ArrayList<>();
        selectQuery ="SELECT * FROM trans_msg where changdate LIKE '%-"+key+"-"+addadys+"' AND nameofbank='"+acnm+"' AND accuntnumber='"+acno+"' ORDER BY yrdata desc,mnthdate desc,changdate  desc,msgtime desc";
        cursor = sql.rawQuery(selectQuery, null);
        Log.i("sql","...."+selectQuery);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                do{
//                    String aa = cursor.getString(cursor.getColumnIndex("msgid"));
                    String bb = cursor.getString(cursor.getColumnIndex("nameofbank"));
                    String cc = cursor.getString(cursor.getColumnIndex("body"));
                    String dd = cursor.getString(cursor.getColumnIndex("transdate"));
                    String ee= cursor.getString(cursor.getColumnIndex("category"));
                    String ff= cursor.getString(cursor.getColumnIndex("type"));
                    String gg= cursor.getString(cursor.getColumnIndex("changdate"));
                    String hh= cursor.getString(cursor.getColumnIndex("amount"));

                    Log.i("aa",""+bb );
                    Log.i("aa",""+cc );
                    Log.i("addda",""+dd );
                    Log.i("aa",""+ee );
                    Log.i("adsda",""+ff );
                    Log.i("atg87tga",""+gg );
                    Log.i("fffff",""+hh );

                    Log.i("adda",""+dbmoney );

                    int count = cursor.getCount();
                    Log.i("count",""+count );

                    //category function

                    id = gettypecat(ff);
                    Log.i("movidde",""+id );

                    // movie = new Movie(aa, bb, dd,id);
                    movie = new Movie(bb, hh, dd,id,ff);
                    Log.i("movie",""+movie );
                    movieList.add(movie);
                    Log.i("movieList",""+movieList );

                    mAdapter = new MoviesAdapter(movieList);
                    Log.i("movieList",""+mAdapter );
                    recyclerView.setAdapter(mAdapter);
                    Log.i("recyclerView",""+recyclerView );
                    mAdapter.notifyDataSetChanged();

                }while (cursor.moveToNext());
            }
            else
            {
                linearnorecord.setVisibility(View.VISIBLE);
                linearrecord.setVisibility(View.GONE);
            }
        }
    }

    private int gettypecat(String ff) {
        String a = ff;
        Log.i("guikk",""+a );
        switch (a) {
            case "Bills":
                id = R.mipmap.bill_aftr;
                break;

            case "Food":
                id = R.mipmap.food_aftr;
                break;

            case "Fuel":
                id = R.mipmap.fuel_aftr;
                break;

            case "Groceries":
                id = R.mipmap.grocery_aftr;
                break;

            case "Health":
                id = R.mipmap.health_aftr;
                break;

            case "Shopping":
                id = R.mipmap.shoping_aftr;
                break;

            case "Travel":
                id = R.mipmap.travel_aftr;
                break;

            case "Other":
                id = R.mipmap.other_aftr;
                break;

            case "PURCHASE.":
                id = R.mipmap.purchase_aftr;
                break;

            case "EXPENSE":
                id = R.mipmap.extra_aftr;
                break;

            case "DEBITED.":
                id = R.mipmap.debited_aftr;
                break;

            case "Entertainment":
                id = R.mipmap.entertainment_aftr;
                break;

            default:
                id = R.mipmap.atm;
                break;
        }
        return id;
    }

    public void calculatePercentage() {
        float[] nmbers = {
                UserInformation.availacno,
                UserInformation.billacno,
                UserInformation.creditacno,
                UserInformation.debitacno,
                UserInformation.enteracno,

                UserInformation.foodacno,
                UserInformation.fuelacno,
                UserInformation.groacno,
                UserInformation.healthacno,
                UserInformation.purchaseacno,
                UserInformation.otheracno,

                UserInformation.shoppingacno,
                UserInformation.extraacno,
                UserInformation.travelacno,
        };

        Log.i("broadingamounts", " amontfood  " + UserInformation.foodacno
                + " amntfuel " + UserInformation.fuelacno
                + " amntbills" + UserInformation.billacno
                + " amntshopping " + UserInformation.shoppingacno
                + " amntgroceries " + UserInformation.groacno
                + " amntentertainment " + UserInformation.enteracno
                + " amnthealth " + UserInformation.healthacno
                + " amntother " + UserInformation.otheracno
                + " amntstravew " + UserInformation.travelacno
                + " totaltransextra " + UserInformation.extraacno
                + " totaltransdebit " + UserInformation.debitacno
                + " totaltranspurchase " + UserInformation.purchaseacno
                + " totaltranscredit " + UserInformation.creditacno

        );

        float total = 0;
        perc = new float[nmbers.length];
        perc1 = new float[nmbers.length];
        amount = new float[nmbers.length];
        for (float a : nmbers)
            total = total + a;
        Log.i("broadingjjj", " total  " + total);
        int j=0;
        int k =0;


        for (int i = 0; i < nmbers.length; i++) {
            perc[i] = (nmbers[i] * 100) / total;
            if(nmbers[i]>0.0)
            {
                amount[k] = nmbers[i];
                Log.i("amountfff", " perc1 " +  amount[k]);
                ++k;
            }

            //  perc[i] = ((nmbers[i])/total*100;
            Log.i("broadingsss", " perc " + perc[i]);
            if (perc[i] > 0.0) {
                perc1[j] = perc[i];
                Log.i("broadingfff", " perc1 " + perc1[j]);
                ++j;
            }
        }
    }

    private void setData(int count, float range) {

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            Log.i("broading", " length count  " + count);
            yVals1.add(new Entry(perc1[i], i));
            Log.i("broading", " perc2 " + perc1[i]);
            Log.i("broading", " yVals " + yVals1.size());
        }

        xVals = new ArrayList<String>();

        for (int i = 0; i < count; i++) {
            xVals.add(UserInformation.arracno[i] + " " + String.valueOf(amount[i]));
        }
        Log.i("broading", " xVals " + xVals.size());
        PieDataSet dataSet = new PieDataSet(yVals1, " ");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        ArrayList<String> labels = new ArrayList<String>();
        labels.addAll(Arrays.asList(month).subList(0, count + 1));

        // add a lot of colors

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        mChart.setData(data);

        // undo all highlights
        // mChart.highlightValues(null);

        mChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Cashiya");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 7, 0);
        //  s.setSpan(new StyleSpan(Typeface.NORMAL), 7, s.length() - 4, 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 4, 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 4, 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 4, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 4, s.length(), 0);
        return s;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        int  entry = e.getXIndex();
        String[] result={};

        Log.i("ddsdd",""+entry);
        String category =xVals.get(entry);
        Log.i("category",""+category);
        result = category.split("\\s+");
        Log.i("category1",""+result[0]);
        if(category.equals("AVAILABLE BALANCE"))
        {
            result[0] = "AVAILABLE BALANCE";
        }
        Intent ii = new Intent(this, ShowSpendsAcno.class);
        ii.putExtra("calling", result[0]);
        ii.putExtra("month",key);
        ii.putExtra("addadys",addadys);
        ii.putExtra("acnumber",acno);
        ii.putExtra("acname",acnm);
        ii.putExtra("typeq","monthly");

        startActivity(ii);

    }

    @Override
    public void onNothingSelected() {

    }
}