package com.hxqc.mall.thirdshop.accessory4s.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.api.AccessoryApiClient;
import com.hxqc.mall.thirdshop.accessory4s.activity.ShoppingCart4SActivity;
import com.hxqc.mall.thirdshop.accessory4s.adapter.ShoppingCart4SAdapter;
import com.hxqc.mall.thirdshop.accessory4s.model.PackageInShoppingCart4S;
import com.hxqc.mall.thirdshop.accessory4s.model.ProductInfo4S;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.SquareImageView;

import java.util.ArrayList;

/**
 * 说明:失效购物车
 *
 * @author: 吕飞
 * @since: 2016-02-22
 * Copyright:恒信汽车电子商务有限公司
 */
public class ShoppingCartInvalid4S extends LinearLayout implements View.OnClickListener {
    RelativeLayout mHeadView;//套餐头部
    ImageView mInvalidProductView;//失效商品
    ImageView mDeletePackageView;//删除失效套餐
    SquareImageView mPhotoView;//商品图片
    TextView mNameView;//商品名字
    TextView mInvalidView;//商品失效文字提示
    ImageView mDeleteProductView;//删除失效商品
    View mBottomLineView;//底部线
    View mBottomBlankView;//底部空白
    View mTopView;
    View mTopLineView;
    ProductInfo4S mProductInfo;
    int mPosition;
    ArrayList<ProductInfo4S> mProductInfos;
    ShoppingCart4SAdapter mShoppingCartAdapter;

    public ShoppingCartInvalid4S(Context context) {
        super(context);
    }

    public ShoppingCartInvalid4S(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_shopping_cart_invalid_4s, this);
        mTopView = findViewById(R.id.top);
        mTopLineView = findViewById(R.id.top_line);
        mHeadView = (RelativeLayout) findViewById(R.id.head);
        mInvalidProductView = (ImageView) findViewById(R.id.invalid_product);
        mDeletePackageView = (ImageView) findViewById(R.id.delete_package);
        mPhotoView = (SquareImageView) findViewById(R.id.photo);
        mNameView = (TextView) findViewById(R.id.name);
        mInvalidView = (TextView) findViewById(R.id.invalid);
        mDeleteProductView = (ImageView) findViewById(R.id.delete_product);
        mBottomLineView = findViewById(R.id.bottom_line);
        mBottomBlankView = findViewById(R.id.bottom_blank);
        mDeletePackageView.setOnClickListener(this);
        mDeleteProductView.setOnClickListener(this);
    }

    public void fillData(ArrayList<ProductInfo4S> productInfos, int position, ShoppingCart4SAdapter shoppingCartAdapter) {
        mProductInfo = productInfos.get(position);
        mProductInfos = productInfos;
        mPosition = position;
        mShoppingCartAdapter = shoppingCartAdapter;
        initView();
    }

    private void initView() {
        OtherUtil.setVisible(mTopView, mPosition == 0);
        OtherUtil.setVisible(mTopLineView, mPosition == 0);
        OtherUtil.setVisible(mBottomBlankView, !mProductInfo.isInPackage || mProductInfo.isLastInPackage);
        OtherUtil.setVisible(mBottomLineView, mProductInfo.isLastInPackage || !mProductInfo.isInPackage);
        OtherUtil.setVisible(mHeadView, mProductInfo.isFirstInPackage);
        OtherUtil.setVisible(mInvalidProductView, !mProductInfo.isInPackage);
        ImageUtil.setImageSquare(getContext(), mPhotoView, mProductInfo.smallPhoto);
        mNameView.setText(mProductInfo.name);
        OtherUtil.setVisible(mInvalidView, !mProductInfo.isInPackage);
        OtherUtil.setVisible(mDeleteProductView, !mProductInfo.isInPackage);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.delete_package) {
            final ArrayList<ProductInfo4S> deleteList = new ArrayList<>();
            for (int j = mPosition; j < mProductInfos.size(); j++) {
                deleteList.add(mProductInfos.get(j));
                if (mProductInfos.get(j).isLastInPackage) {
                    break;
                }
            }
            new AccessoryApiClient().deleteFromShoppingCart("", "[" + JSONUtils.toJson(new PackageInShoppingCart4S(mPosition, mProductInfos)).replace(" ", "") + "]", new LoadingAnimResponseHandler(getContext()) {
                @Override
                public void onSuccess(String response) {
                    mProductInfos.removeAll(deleteList);
                    updateList();
                }
            });
        } else if (i == R.id.delete_product) {
            new AccessoryApiClient().deleteFromShoppingCart(mProductInfo.productID, "", new LoadingAnimResponseHandler(getContext()) {
                @Override
                public void onSuccess(String response) {
                    mProductInfos.remove(mPosition);
                    updateList();
                }
            });
        }
    }

    private void updateList() {
        ((ShoppingCart4SActivity) getContext()).checkNull();
        mShoppingCartAdapter.notifyDataSetChanged();
    }
}
