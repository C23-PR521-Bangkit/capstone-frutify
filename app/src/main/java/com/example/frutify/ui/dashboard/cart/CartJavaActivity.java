package com.example.frutify.ui.dashboard.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Header;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.frutify.R;
import com.example.frutify.utils.Helper;
import com.example.frutify.utils.SharePref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class CartJavaActivity extends AppCompatActivity {

    private String TAG = Helper.DEBUG_TAG;
    private SharePref sharePref;

    private LinearLayout divContainer;
    private TextView tvTotal;
    private LinearLayout divLanjut;

    private JSONArray data;
    private JSONObject storeOrdered;
    private String storeSelected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        divContainer = findViewById(R.id.divContainer);
        tvTotal = findViewById(R.id.tvTotal);
        divLanjut = findViewById(R.id.divLanjut);

        sharePref = new SharePref(CartJavaActivity.this);

        divLanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(storeSelected.equals("")){
                    Toast.makeText(CartJavaActivity.this, "Mohon pilih seller yang akan dipesan", Toast.LENGTH_SHORT).show();
                }else{
                    new AlertDialog.Builder(CartJavaActivity.this)
                        .setTitle("KONFIRMASI PESANAN")
                        .setMessage("Pesanan akan diteruskan ke WhatsApp penjual dan item akan dihapus dari keranjang")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String buildMsg = "Halo " + storeOrdered.optString("USER_FULLNAME") + ", saya ingin memesan:";
                                JSONArray items = storeOrdered.optJSONArray("ITEM");
                                for(int i = 0; i < items.length(); i++){
                                    JSONObject aItem = items.optJSONObject(i);
                                    changeCartItem(- Integer.parseInt(aItem.optString("TOTAL_QTY")), aItem.optString("PRODUCT_ID"));
                                    buildMsg += "\n" + i+1 + ". x" + aItem.optString("TOTAL_QTY") + " " +
                                            aItem.optString("PRODUCT_NAME") +
                                            " (Rp " + (Integer.valueOf(aItem.optString("PRODUCT_PRICE")) * Integer.valueOf(aItem.optString("TOTAL_QTY"))) + ")";
                                }
                                buildMsg += "\nTOTAL = Rp " + storeOrdered.optString("TOTAL");
                                // send WA
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse("https://api.whatsapp.com/send?phone="+storeOrdered.optString("USER_PHONE") + "&text=" + buildMsg));
                                startActivity(i);
                                recreate();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                }
            }
        });

        fetch();
    }

    public void fetch(){
        String url = Helper.BASE_URL + "cart/list";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("user_id", String.valueOf(sharePref.getGetUserId()));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        final String requestBody = jsonBody.toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "onResponse: " + response);
                    final String status = response.optString(Helper.RESPONSE_STATUS_FIELD);
                    final String message = response.optString(Helper.RESPONSE_MESSAGE_FIELD);

                    if(status.equalsIgnoreCase(Helper.RESPONSE_STATUS_VALUE_SUCCESS)) {
                        JSONObject payload = response.optJSONObject(Helper.RESPONSE_PAYLOAD_FIELD);
                        data = payload.optJSONArray("cart");
                        setupMenu();
                    }else{
                        Toast.makeText(CartJavaActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                })
            {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

        }
        ;

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    private void setupMenu(){
        if(data.length() == 0){
            Toast.makeText(CartJavaActivity.this, "Cart kosong", Toast.LENGTH_SHORT).show();
        }

        divContainer.removeAllViews();
        divContainer.setVisibility(View.VISIBLE);
        for(int i=0; i < data.length(); i++) {
            JSONObject aMenu = data.optJSONObject(i);
            if(aMenu == null || aMenu.optString("TOTAL").equals("0"))    continue;

            LayoutInflater factory = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = factory.inflate(R.layout.item_row_seller, divContainer, false);

            TextView tvName = rowView.findViewById(R.id.tvName);
            LinearLayout divContainerProduct = rowView.findViewById(R.id.divContainerProduct);
            Button btnFlagActive = rowView.findViewById(R.id.btnFlagActive);

            String userId = aMenu.optString("USER_ID");
            if(storeSelected.equals(userId)) {
                tvTotal.setText("Rp " + aMenu.optString("TOTAL"));
            }

            JSONArray items = aMenu.optJSONArray("ITEM");
            divContainerProduct.removeAllViews();
            for(int i2 = 0; i2 < items.length(); i2++){
                addItemView(items.optJSONObject(i2), divContainerProduct);
            }

            tvName.setText(aMenu.optString("USER_FULLNAME"));
            if(storeSelected.equals(userId)){
                btnFlagActive.setVisibility(View.VISIBLE);
            }else{
                btnFlagActive.setVisibility(View.INVISIBLE);
            }

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectStore(userId, aMenu);
                    fetch();
                }
            });

            divContainer.addView(rowView);
        }
    }


    public void addItemView(JSONObject item, LinearLayout divContainerProduct){

        LayoutInflater factory = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = factory.inflate(R.layout.item_row_cart, divContainer, false);

        ImageView ivImage = rowView.findViewById(R.id.ivImage);
        TextView tvName = rowView.findViewById(R.id.tvName);
        TextView tvFruitType = rowView.findViewById(R.id.tvFruitType);
        TextView tvQuality = rowView.findViewById(R.id.tvQuality);
        TextView tvPrice = rowView.findViewById(R.id.tvPrice);
        ImageView ivEditMinus = rowView.findViewById(R.id.ivEditMinus);
        ImageView ivEditPlus = rowView.findViewById(R.id.ivEditPlus);
        TextView tvEditQty = rowView.findViewById(R.id.tvEditQty);
        ImageView ivDelete = rowView.findViewById(R.id.ivDelete);

        tvName.setText(item.optString("PRODUCT_NAME"));
        tvFruitType.setText(item.optString("FRUIT_NAME"));
        tvQuality.setText(item.optString("PRODUCT_QUALITY"));
        tvPrice.setText("Rp" + item.optString("PRODUCT_PRICE"));
        tvEditQty.setText("" + item.optString("TOTAL_QTY"));

        String productId = item.optString("PRODUCT_ID");

        Glide.with(CartJavaActivity.this)
            .load(Helper.getFile(item.optString("PRODUCT_FILE_PATH")))
            .centerCrop()
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(ivImage);

        ivEditMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeCartItem(-1, productId);
            }
        });
        ivEditPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeCartItem(1, productId);
            }
        });
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeCartItem(- Integer.parseInt(item.optString("TOTAL_QTY")), productId);
            }
        });

        divContainerProduct.addView(rowView);
    }

    public void changeCartItem(int qty, String productId){
        String url = Helper.BASE_URL + "cart/add";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("user_id", String.valueOf(sharePref.getGetUserId()));
            jsonBody.put("product_id", productId);
            jsonBody.put("qty", String.valueOf(qty));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        final String requestBody = jsonBody.toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response);
                        fetch();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                })
            {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public byte[] getBody() {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }


    public void selectStore(String userId, JSONObject aMenu){
        if(storeSelected.equals(userId)){
            storeSelected = "";
            tvTotal.setText("Rp 0");
        }else{
            storeSelected = userId;
            storeOrdered = aMenu;
            tvTotal.setText("Rp " + aMenu.optInt("TOTAL"));
        }
    }

}