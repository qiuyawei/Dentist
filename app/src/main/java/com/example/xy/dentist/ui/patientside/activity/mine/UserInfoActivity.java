package com.example.xy.dentist.ui.patientside.activity.mine;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xy.dentist.R;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.InfoBean;
import com.example.xy.dentist.bean.PicBean;
import com.example.xy.dentist.bean.UserInfo;
import com.example.xy.dentist.bean.eventbus.UpdateDocInfo;
import com.example.xy.dentist.bean.eventbus.UpdateInfo;
import com.example.xy.dentist.contract.UserInfoContract;
import com.example.xy.dentist.model.UserInfoModel;
import com.example.xy.dentist.presenter.UserInfoPresenter;
import com.example.xy.dentist.utils.BitmapUtils;
import com.example.xy.dentist.utils.CommonUtils;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.MySelfSheetDialog;
import com.example.xy.dentist.utils.PartMapUtils;
import com.example.xy.dentist.utils.PicUtil;
import com.example.xy.dentist.utils.PopUtil;
import com.example.xy.dentist.utils.SharedPreferencesUtil;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.FormatUtil;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;
import com.jaydenxiao.common.utils.RxPermissionsUtils;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.RequestBody;

public class UserInfoActivity extends BaseActivity<UserInfoPresenter, UserInfoModel> implements UserInfoContract.View {


    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    @Bind(R.id.iv_head)
    ImageView mIvHead;
    @Bind(R.id.ll_head)
    LinearLayout mLlHead;
    @Bind(R.id.ll_name)
    LinearLayout mLlName;
    @Bind(R.id.ll_year)
    LinearLayout mLlYear;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_year)
    TextView mTvYear;
    private String photoSaveName;
    private Uri imageUri;
    private String dat;
    private String mToken;
    private InfoBean info;
    private UserInfo mUserInfo;
    private HashMap<String, RequestBody> mMap = new HashMap<String, RequestBody>();
    ;
    private String name, age, avatar;
    private String phone;
    private boolean mIsdoc;
    private String doctor_token;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        mNtb.setTitleText("编辑资料");
        mNtb.setRightImagVisibility(false);
        mNtb.setLeftImagSrc(R.mipmap.back);
        info = (InfoBean) getIntent().getSerializableExtra("info");
        if (info != null) {
            mUserInfo = info.info;
            mTvName.setText(FormatUtil.checkValue(mUserInfo.name));
            mTvYear.setText(FormatUtil.checkValue(mUserInfo.age + "岁"));
            phone = mUserInfo.phone;
            //  tv_phone.setText(FormatUtil.checkValue(mUserInfo.phone));
            ImageLoaderUtils.displayRound(mActivity, mIvHead, ApiConstants.IMAGE_URL + mUserInfo.avatar);
        }
//        Log.i("TAG","myToken="+GlobalParams.getuser_token());
    }

    @Override
    protected void initData() {

        doctor_token = GlobalParams.getdoctor_token();
        mIsdoc = SharedPreferencesUtil.getBooleanData(mActivity, "isdoc", false);
        if (!mIsdoc) {
            mToken = GlobalParams.getuser_token();
        } else {
            mToken = GlobalParams.getdoctor_token();
        }
    }

    @Override
    protected void initListener() {
        mNtb.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @OnClick({R.id.ll_head, R.id.ll_name, R.id.ll_year})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_head:
                selectPic();
                break;
            case R.id.ll_name:
                intent = new Intent(mActivity, UserDataActivity.class);
                intent.putExtra("name", name);
                startActivityForResult(intent, 200);
                break;
            case R.id.ll_year:
                PopUtil.showBirthdayPopwindow(this,
                        PopUtil.getYearPick(this, new PopUtil.onSelectFinishListener() {
                            @Override
                            public void onSelectFinish(String date) {
                                mTvYear.setText(date + "岁");
                                name = "";
                                age = date;
                                updateUser();
                                // loadData();
                            }
                        }));
                break;
        }
    }

    private void updateUser() {
        mPresenter.updateInfo(mIsdoc, mToken, name, avatar, age);
    }


    private void selectPic() {
        new MySelfSheetDialog(mActivity).builder().addSheetItem(getResources().getString(R.string.photo), MySelfSheetDialog.SheetItemColor.Gray, new MySelfSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                RxPermissionsUtils.checkPermission(mActivity, new RxPermissionsUtils.onBackListener() {
                    @Override
                    public void listener(Boolean isAgree) {
                        if (isAgree) {
                            photoSaveName = String.valueOf(System.currentTimeMillis()) + ".png";

                            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            File file = new File(CommonUtils.getPhotoSavePath(), photoSaveName);

                            //改变Uri  com.xykj.customview.fileprovider注意和xml中的一致
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                //7.0
                                imageUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.xy.dentist.fileprovider", file);//通过FileProvider创建一个content类型的Uri
                            } else {
                                imageUri = Uri.fromFile(file);
                            }

                            openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(openCameraIntent, PicUtil.PHOTOTAKE);
                        } else {
                            ToastUitl.showShort("没有拍照权限或没有写入SD 卡权限");
                        }

                    }
                }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
              /*else{
                    new AlertDialog.Builder(mActivity)
                            .setCancelable(false)
                            .setTitle("权限申请")
                            .setMessage("在设置-应用-当前应用权限中开启相机权限，以正常使用拍照")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            })
                            .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //开启设置页
                                     startActivity(new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS));
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }*/


            }
        }).addSheetItem(getResources().getString(R.string.camera), MySelfSheetDialog.SheetItemColor.Gray, new MySelfSheetDialog.OnSheetItemClickListener() {

            @Override
            public void onClick(int which) {

                RxPermissionsUtils.checkPermission(mActivity, new RxPermissionsUtils.onBackListener() {
                    @Override
                    public void listener(Boolean isAgree) {
                        if(isAgree){
                            Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            openAlbumIntent.setType("image/*");
                            startActivityForResult(openAlbumIntent, PicUtil.PHOTOZOOM);
                        } else {
                            ToastUitl.showShort("没有拍照权限或没有写入SD 卡权限");
                        }
                    }
                }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }).show();
    }

    String path;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case PicUtil.PHOTOZOOM:

                    path = BitmapUtils.getInstance().getPath(mActivity, data.getData());// 相册
                    startCrop(data.getData());


                    break;
                case PicUtil.PHOTOTAKE:// 拍照
                    path = CommonUtils.getPhotoSavePath() + "/" + photoSaveName;
                    startPhoneCrop();


                    break;
                case UCrop.REQUEST_CROP:

                    path = BitmapUtils.getRealFilePath(mActivity, UCrop.getOutput(data));

                    mMap.put("avatar\";filename=\"" + (System.currentTimeMillis() + ".png"), PartMapUtils.getImageRequestBody(path));

//                    Log.i("TAG","isDOC="+mIsdoc);
                    if (!mIsdoc) {
                        mMap.put("user_token", PartMapUtils.getTextRequestBody(mToken));
                        mPresenter.updateAvatar(mMap);
                    } else {
                        Log.i("TAG","doc_token/////////="+GlobalParams.getdoctor_token());

                        mMap.put("doctor_token", PartMapUtils.getTextRequestBody(GlobalParams.getdoctor_token()));
                        mPresenter.updateDoctorAvatar(mMap);
                    }

                    break;
                //裁切失败
                case UCrop.RESULT_ERROR:
                    Toast.makeText(this, "裁切图片失败", Toast.LENGTH_SHORT).show();
                    break;

                case 200:
                    dat = data.getStringExtra("data");
                    mTvName.setText(dat);
                    age = "";
                    name = dat;
                    updateUser();
                    break;


            }
        }

    }

    private void startPhoneCrop() {
        Uri sourceUri = imageUri;
        //裁剪后保存到文件中
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "CropImage.png"));
        UCrop.of(sourceUri, destinationUri).withMaxResultSize(300, 300).start(this);
    }

    private void startCrop(Uri path) {
        Uri sourceUri = path;
        //裁剪后保存到文件中
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "SampleCropImage.png"));
        UCrop.of(sourceUri, destinationUri).withMaxResultSize(300, 300).start(this);
    }


    @Override
    public void changeInfoState(BaseResult result, String type) {

        switch (type) {
            case "0":
                ToastUitl.showShort("更新成功");
                break;
            case "1":
                ToastUitl.showShort("更新成功");
                break;
            case "2":
                ToastUitl.showShort("更新成功");
                break;
        }

        if (!mIsdoc) {
            EventBus.getDefault().post(new UpdateInfo(true));
        } else {
            EventBus.getDefault().post(new UpdateDocInfo(true));
        }
    }

    @Override
    public void updateAvatarState(BaseResult<PicBean> result) {
        ToastUitl.showShort("修改成功");
        PicBean bean = result.data;
        avatar = bean.path;
        ImageLoaderUtils.displayRound(mActivity, mIvHead, path);

        updateUser();
    }

    @Override
    public void updateDoctorAvatarState(BaseResult<PicBean> result) {
        ToastUitl.showShort("修改成功");
        PicBean bean = result.data;
        avatar = bean.path;
        ImageLoaderUtils.displayRound(mActivity, mIvHead, path);

        updateUser();
    }



    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }
}
