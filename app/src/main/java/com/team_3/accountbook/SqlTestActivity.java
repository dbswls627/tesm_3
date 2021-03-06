package com.team_3.accountbook;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class SqlTestActivity extends AppCompatActivity {
    private HomeActivity h = new HomeActivity();
    EditText mAssetsName, mWayName, mWayBalance, mFKAssetsId;
    AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql_test);

        mAssetsName = findViewById(R.id.et_assetsName);
        mWayName = findViewById(R.id.et_wayName);
        mWayBalance = findViewById(R.id.et_wayBalance);
        mFKAssetsId = findViewById(R.id.et_FK_assetsId);

        db = AppDatabase.getInstance(this);

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.bt_insertAssets:
                Asset asset = new Asset();
                if (mAssetsName.length() > 0) {
                    asset.setAssetName(mAssetsName.getText().toString());
                    db.dao().insertAsset(asset);
                    Toast.makeText(this, "추가되었습니다.", Toast.LENGTH_SHORT).show();
                    mAssetsName.setText("");
                }
                else {
                    Toast.makeText(this, "자산명을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.bt_insertWay:
                if (mWayName.length() > 0 && mFKAssetsId.length() > 0) {
                    db.dao().insertWay(mWayName.getText().toString(), Integer.parseInt(mWayBalance.getText().toString()), Integer.parseInt(mFKAssetsId.getText().toString()));
                    Toast.makeText(this, "추가되었습니다.", Toast.LENGTH_SHORT).show();
                    mWayName.setText("");
                    mFKAssetsId.setText("");
                }
                else {
                    Toast.makeText(this, "수단명, 잔액, 자산 id를 모두 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.bt_deleteAssetsAll:
                db.dao().deleteAssetAll();
                Toast.makeText(this, "모두 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                break;


            case R.id.bt_referAssets:
                List<Asset> list_assets = db.dao().getAssetAll();
                for (int i = 0; i < list_assets.size(); i++) {
                    Log.d("Assets", "ID:" + list_assets.get(i).getAssetId() + ", name:" + list_assets.get(i).getAssetName());
                }
                break;


            case R.id.bt_referWay:
                List<Way> list_way = db.dao().getWayAll();
                for (int i = 0; i < list_way.size(); i++) {
                    Log.d("Way", "name:" + list_way.get(i).getWayName()
                            + ", FK:" + list_way.get(i).getFK_assetId());
                }
                break;


            case R.id.bt_referCost:
                List<Cost> list_cost = db.dao().getCostAll();
                for (int i = 0; i < list_cost.size(); i++) {
                    Log.d("Cost", "ID:" + list_cost.get(i).getCostId() + ", " +
                            "amount:" + list_cost.get(i).getAmount() + ", " +
                            "content:" + list_cost.get(i).getContent() + ", " +
                            "date:" + list_cost.get(i).getUseDate() + ", " +
                            "balance:" + list_cost.get(i).getBalance());
                }
                break;



//            case R.id.bt_referAssetWithWays:
//                List<AssetWithWays> AWW2 = db.dao().getAssetWithWays2();
//                for (int i = 0; i < AWW2.size(); i++) {
//                    int len = AWW2.get(i).getWay().size();
//                    Log.d("AWW2", String.valueOf(AWW2.size()) + ", " + len);
//                    for (int j = 0; j < len; j++) {
//                        Log.d("refer_Assets", AWW2.get(i).getAsset().getAssetId() + " " + AWW2.get(i).getAsset().getAssetName());
//                        Log.d("refer_Way", "- " + AWW2.get(i).getWay().get(j).getWayName());
//                    }
//                }
//                break;

            case R.id.bt_referAssetWithWays:
                List<AssetWithWay> list_AW = db.dao().getAssetWithWays();
                list_AW.forEach(it-> Log.d("ways :",
                                " wayName :"+it.getWayName()+
                                " wayBalance :"+it.getWayBalance()+
                                " FK_assetId :"+it.getFK_assetId()+
                                " assetID :"+it.getAssetId()+
                               " assetName :"+it.getAssetName()));
                break;


            case R.id.bt_referWayWithCosts:
                List<WayWithCost> list_WC = db.dao().getWayWithCosts();
                list_WC.forEach(it ->Log.d("WayWithCost", it.getUseDate() + " " + it.getWayName() + " " +
                        it.getAmount() + " " + it.getDivision()));
                break;

            case R.id.AssetNameWayName:
                List<AssetNameWayNameAndBalance> list_AnWnWb = db.dao().getAnWnWb();
                for (int i = 0; i < list_AnWnWb.size(); i++) {
                    Log.d("AssetNameWayName", list_AnWnWb.get(i).getAssetName() + "/" + list_AnWnWb.get(i).getWayName() + "/"
                                                + list_AnWnWb.get(i).getWayBalance());
                }

                break;

            case R.id.deleteCostAll:
                db.dao().deleteCostAll();

                break;
        }
    }


}