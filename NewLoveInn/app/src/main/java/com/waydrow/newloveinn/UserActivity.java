package com.waydrow.newloveinn;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.waydrow.newloveinn.bean.Volunteer;
import com.waydrow.newloveinn.util.API;
import com.waydrow.newloveinn.util.HttpUtil;
import com.waydrow.newloveinn.util.PictureUtil;
import com.waydrow.newloveinn.util.Utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.data;
import static android.R.attr.name;
import static android.R.attr.path;
import static android.R.attr.type;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.waydrow.newloveinn.R.id.cancel_action;
import static com.waydrow.newloveinn.R.id.default_activity_button;
import static com.waydrow.newloveinn.R.id.image;
import static com.waydrow.newloveinn.R.id.user_auth;

public class UserActivity extends AppCompatActivity {

    private static final String TAG = "UserActivity";

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;

    public static final int TAKE_PHOTO_STUCARD = 3;
    public static final int CHOOSE_PHOTO_STUCARD = 4;

    // avatar
    private Uri imageUri;
    private String imagePath;

    // stucard
    private Uri imageStucardUri;
    private String imageStucardPath;

    private RelativeLayout userAuth; // 认证栏
    private TextView userAuthState; // 认证状态

    private RelativeLayout userAvatar; // 头像栏
    private CircleImageView userAvatarImage; // 头像

    private RelativeLayout userRealname; // 真实姓名栏
    private TextView userRealnameText;

    private RelativeLayout userAge; // 年龄栏
    private TextView userAgeText;

    private RelativeLayout userSex;
    private TextView userSexText;

    private RelativeLayout userIdcard;
    private TextView userIdcardText;

    private RelativeLayout userPhone;
    private TextView userPhoneText;

    private RelativeLayout userEmail;
    private TextView userEmailText;

    private RelativeLayout userInfo;
    private TextView userInfoText;

    private RelativeLayout userStucard;
    private ImageView userStucardImage;

    private ProgressDialog progressDialog;

    private String userId; // 用户id

    private Volunteer volunteer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        userAuth = (RelativeLayout) findViewById(user_auth);
        userAuthState = (TextView) findViewById(R.id.user_auth_state);
        userAvatar = (RelativeLayout) findViewById(R.id.user_avatar);
        userAvatarImage = (CircleImageView) findViewById(R.id.user_avatar_image);
        userRealname = (RelativeLayout) findViewById(R.id.user_realname);
        userRealnameText = (TextView) findViewById(R.id.user_realname_text);
        userAge = (RelativeLayout) findViewById(R.id.user_age);
        userAgeText = (TextView) findViewById(R.id.user_age_text);
        userSex = (RelativeLayout) findViewById(R.id.user_sex);
        userSexText = (TextView) findViewById(R.id.user_sex_text);
        userIdcard = (RelativeLayout) findViewById(R.id.user_idcard);
        userIdcardText = (TextView) findViewById(R.id.user_idcard_text);
        userPhone = (RelativeLayout) findViewById(R.id.user_phone);
        userPhoneText = (TextView) findViewById(R.id.user_phone_text);
        userEmail = (RelativeLayout) findViewById(R.id.user_email);
        userEmailText = (TextView) findViewById(R.id.user_email_text);
        userInfo = (RelativeLayout) findViewById(R.id.user_info);
        userInfoText = (TextView) findViewById(R.id.user_info_text);
        userStucard = (RelativeLayout) findViewById(R.id.user_stucard);
        userStucardImage = (ImageView) findViewById(R.id.user_stucard_image);

        // 获取用户id
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        userId = prefs.getString(API.PREF_USER_ID, null);

        init(); // 加载个人信息

        userAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UserActivity.this)
                        .setTitle("确认实名认证")
                        .setMessage("请保证您所填写的资料真实有效")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                auth();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

            }
        });

        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                builder.setTitle("更换头像");
                final String[] choices = {"拍照", "从相册中选择"};
                builder.setItems(choices, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(UserActivity.this, choices[which], Toast.LENGTH_SHORT).show();
                        switch (which) {
                            case 0: // 拍照
                                // 创建File对象, 存储拍照后的图片
                                String timeStamp = new SimpleDateFormat("yyyyHHdd_HHmmss").format(new Date());

                                File outputImage = new File(getExternalCacheDir(), timeStamp + "_newloveinn_avatar.png");
                                imagePath = outputImage.getPath();
                                Log.d(TAG, "onClick: outputImage path: " + outputImage.getPath());
                                if(outputImage.exists()) {
                                    outputImage.delete();
                                }
                                try {
                                    outputImage.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if(Build.VERSION.SDK_INT >= 24) {
                                    imageUri = FileProvider.getUriForFile(UserActivity.this,
                                            "com.waydrow.newloveinn.fileprovider", outputImage);
                                } else {
                                    imageUri = Uri.fromFile(outputImage);
                                }

                                // 启动相机
                                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(intent, TAKE_PHOTO);
                                break;
                            case 1: // 选取照片
                                if (ContextCompat.checkSelfPermission(UserActivity.this,
                                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(UserActivity.this, new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                } else {
                                    openAlbum(CHOOSE_PHOTO);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
                builder.show();
            }
        });

        userStucard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                builder.setTitle("更换学生证");
                final String[] choices = {"拍照", "从相册中选择"};
                builder.setItems(choices, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(UserActivity.this, choices[which], Toast.LENGTH_SHORT).show();
                        switch (which) {
                            case 0: // 拍照
                                // 创建File对象, 存储拍照后的图片
                                String timeStamp = new SimpleDateFormat("yyyyHHdd_HHmmss").format(new Date());

                                File outputImage = new File(getExternalCacheDir(), timeStamp + "_newloveinn_stucard.png");
                                imageStucardPath = outputImage.getPath();
                                //Log.d(TAG, "onClick: outputImage path: " + outputImage.getPath());
                                if(outputImage.exists()) {
                                    outputImage.delete();
                                }
                                try {
                                    outputImage.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if(Build.VERSION.SDK_INT >= 24) {
                                    imageStucardUri = FileProvider.getUriForFile(UserActivity.this,
                                            "com.waydrow.newloveinn.fileprovider", outputImage);
                                } else {
                                    imageStucardUri = Uri.fromFile(outputImage);
                                }

                                // 启动相机
                                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageStucardUri);
                                startActivityForResult(intent, TAKE_PHOTO_STUCARD);
                                break;
                            case 1: // 选取照片
                                if (ContextCompat.checkSelfPermission(UserActivity.this,
                                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(UserActivity.this, new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                                } else {
                                    openAlbum(CHOOSE_PHOTO_STUCARD);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
                builder.show();
            }
        });

        // 修改真实姓名
        userRealname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(UserActivity.this);
                et.setText(userRealnameText.getText());
                new AlertDialog.Builder(UserActivity.this)
                        .setTitle("输入真实姓名")
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String inputText = et.getText().toString();
                                if (!TextUtils.isEmpty(inputText)) {
                                    updateRealName(inputText);
                                } else {
                                    Toast.makeText(UserActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        // 修改年龄
        userAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(UserActivity.this);
                et.setText(userAgeText.getText());
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                new AlertDialog.Builder(UserActivity.this)
                        .setTitle("输入年龄")
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int input = Integer.parseInt(et.getText().toString());
                                if (TextUtils.isEmpty(et.getText().toString())) {
                                    Toast.makeText(UserActivity.this, "年龄输入不可为空", Toast.LENGTH_SHORT).show();
                                } else if (input < 0 || input > 200) {
                                    Toast.makeText(UserActivity.this, "年龄输入不合法", Toast.LENGTH_SHORT).show();
                                } else {
                                    updateAge(et.getText().toString());
                                }

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        // 修改性别
        userSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                builder.setTitle("选择性别");
                final String[] sex = {"男", "女"};
                final String[] choice = new String[1];
                int choose = 0;
                // Log.d(TAG, "Sex: " + volunteer.getSex());
                if (volunteer.getSex() != null) {
                    if (volunteer.getSex().equals("m")) {
                        choose = 0;
                        choice[0] = "m";
                    } else {
                        choose = 1;
                        choice[0] = "f";
                    }
                } else {
                    choose = 0;
                    choice[0] = "m";
                }
                builder.setSingleChoiceItems(sex, choose, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (sex[which].equals("男")) {
                            choice[0] = "m";
                        } else {
                            choice[0] = "f";
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateSex(choice[0]);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });

        // 修改身份证号
        userIdcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(UserActivity.this);
                et.setText(userIdcardText.getText());
                new AlertDialog.Builder(UserActivity.this)
                        .setTitle("输入学号")
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String inputText = et.getText().toString();
                                updateIdcard(inputText);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        // 修改电话
        userPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(UserActivity.this);
                et.setText(userPhoneText.getText());
                et.setInputType(InputType.TYPE_CLASS_PHONE);
                new AlertDialog.Builder(UserActivity.this)
                        .setTitle("输入电话")
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String inputText = et.getText().toString();
                                if (inputText.length() < 1) {
                                    Toast.makeText(UserActivity.this, "输入不合法", Toast.LENGTH_SHORT).show();
                                } else {
                                    updatePhone(inputText);
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        // 修改邮箱
        userEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(UserActivity.this);
                et.setText(userEmailText.getText());
                et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                new AlertDialog.Builder(UserActivity.this)
                        .setTitle("输入邮箱")
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String inputText = et.getText().toString();
                                if (inputText.length() < 1) {
                                    Toast.makeText(UserActivity.this, "输入不合法", Toast.LENGTH_SHORT).show();
                                } else {
                                    updateEmail(inputText);
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        // 修改info
        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(UserActivity.this);
                et.setText(userInfoText.getText());
                new AlertDialog.Builder(UserActivity.this)
                        .setTitle("输入个人简介")
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String inputText = et.getText().toString();
                                updateInfo(inputText);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    // 初始化
    private void init() {
        showProgressDialog();
        String url = API.INTERFACE + "getVolunteerInfo";
        RequestBody body = new FormBody.Builder()
                .add("id", userId)
                .build();
        HttpUtil.sendPostRequest(url, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                closeProgressDialog();
                Toast.makeText(UserActivity.this, "获取个人信息失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                volunteer = Utility.handleVolunInfo(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (volunteer.getIspass() == -1) {
                            userAuthState.setText("未实名认证");
                        } else if (volunteer.getIspass() == 0) {
                            userAuthState.setText("等待审核中");
                        } else if (volunteer.getIspass() == 1) {
                            userAuthState.setText("已实名认证");
                        }

                        if (!TextUtils.isEmpty(volunteer.getAvatar())) {
                            Glide.with(UserActivity.this).load(API.IMG_HEADER + volunteer.getAvatar()).crossFade()
                                    .into(userAvatarImage);
                        }

                        if (!TextUtils.isEmpty(volunteer.getRealname())) {
                            userRealnameText.setText(volunteer.getRealname());
                        } else {
                            userRealnameText.setText("");
                        }

                        if (!TextUtils.isEmpty(String.valueOf(volunteer.getAge()))) {
                            userAgeText.setText(String.valueOf(volunteer.getAge()));
                        } else {
                            userAgeText.setText("未知");
                        }

                        if (!TextUtils.isEmpty(volunteer.getSex())) {
                            if (volunteer.getSex().equals("m")) {
                                userSexText.setText("男");
                            } else {
                                userSexText.setText("女");
                            }
                        } else {
                            userSexText.setText("未知");
                        }

                        if (!TextUtils.isEmpty(volunteer.getIdcard())) {
                            userIdcardText.setText(volunteer.getIdcard());
                        } else {
                            userIdcardText.setText("");
                        }

                        if (!TextUtils.isEmpty(volunteer.getPhone())) {
                            userPhoneText.setText(volunteer.getPhone());
                        } else {
                            userPhoneText.setText("");
                        }

                        if (!TextUtils.isEmpty(volunteer.getEmail())) {
                            userEmailText.setText(volunteer.getEmail());
                        } else {
                            userEmailText.setText("");
                        }

                        if (!TextUtils.isEmpty(volunteer.getInfo())) {
                            userInfoText.setText(volunteer.getInfo());
                        } else {
                            userInfoText.setText("");
                        }

                        if (!TextUtils.isEmpty(volunteer.getStucard())) {
                            Glide.with(UserActivity.this).load(API.IMG_HEADER + volunteer.getStucard())
                                    .into(userStucardImage);
                        }

                        closeProgressDialog();
                    }
                });
            }
        });
    }

    // 显示加载框
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(msg);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    // 关闭加载框
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void openAlbum(int choice) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        if (choice == CHOOSE_PHOTO) {
            startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
        } else if (choice == CHOOSE_PHOTO_STUCARD) {
            startActivityForResult(intent, CHOOSE_PHOTO_STUCARD);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum(CHOOSE_PHOTO);
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum(CHOOSE_PHOTO_STUCARD);
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {

                        File file = new File(imagePath);
                        String timeStamp = new SimpleDateFormat("yyyyHHdd_HHmmss").format(new Date());
                        File outputImage = new File(getExternalCacheDir(), timeStamp + "_newloveinn_avatar_compress.png");
                        String targetPath = outputImage.getPath();
                        final String compressImage = PictureUtil.compressImage(file.getPath(), targetPath, 30);
                        final File compressedPic = new File(compressImage);
                        if (compressedPic.exists()) {
                            uploadAvatar(compressedPic, "avatar", "uploadAvatar", TAKE_PHOTO);
                            Glide.with(UserActivity.this).load(compressedPic).crossFade().into(userAvatarImage);
                        }else{//直接上传
                            uploadAvatar(file, "avatar", "uploadAvatar", TAKE_PHOTO);
                            Glide.with(UserActivity.this).load(file).crossFade().into(userAvatarImage);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    String path;
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        path = handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统
                        path = handleImageBeforeKitKat(data);
                    }
                    try {
                        File file = new File(path);
                        String timeStamp = new SimpleDateFormat("yyyyHHdd_HHmmss").format(new Date());
                        File outputImage = new File(getExternalCacheDir(), timeStamp + "_newloveinn_avatar_compress.png");
                        String targetPath = outputImage.getPath();
                        final String compressImage = PictureUtil.compressImage(file.getPath(), targetPath, 30);
                        final File compressedPic = new File(compressImage);
                        if (compressedPic.exists()) {
                            uploadAvatar(compressedPic, "avatar", "uploadAvatar", TAKE_PHOTO);
                            Glide.with(UserActivity.this).load(compressedPic).crossFade().into(userAvatarImage);
                        }else{//直接上传
                            uploadAvatar(file, "avatar", "uploadAvatar", TAKE_PHOTO);
                            Glide.with(UserActivity.this).load(file).crossFade().into(userAvatarImage);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case TAKE_PHOTO_STUCARD:
                if (resultCode == RESULT_OK) {
                    try {

                        File file = new File(imageStucardPath);
                        String timeStamp = new SimpleDateFormat("yyyyHHdd_HHmmss").format(new Date());
                        File outputImage = new File(getExternalCacheDir(), timeStamp + "_newloveinn_stucard_compress.png");
                        String targetPath = outputImage.getPath();
                        final String compressImage = PictureUtil.compressImage(file.getPath(), targetPath, 30);
                        final File compressedPic = new File(compressImage);
                        if (compressedPic.exists()) {
                            uploadAvatar(compressedPic, "stucard", "uploadStucard", TAKE_PHOTO_STUCARD);
                            Glide.with(UserActivity.this).load(compressedPic).crossFade().into(userStucardImage);
                        }else{//直接上传
                            uploadAvatar(file, "stucard", "uploadStucard", TAKE_PHOTO_STUCARD);
                            Glide.with(UserActivity.this).load(file).crossFade().into(userStucardImage);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO_STUCARD:
                if (resultCode == RESULT_OK) {
                    String path;
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        path = handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统
                        path = handleImageBeforeKitKat(data);
                    }
                    try {
                        File file = new File(path);
                        String timeStamp = new SimpleDateFormat("yyyyHHdd_HHmmss").format(new Date());
                        File outputImage = new File(getExternalCacheDir(), timeStamp + "_newloveinn_stucard_compress.png");
                        String targetPath = outputImage.getPath();
                        final String compressImage = PictureUtil.compressImage(file.getPath(), targetPath, 30);
                        final File compressedPic = new File(compressImage);
                        if (compressedPic.exists()) {
                            uploadAvatar(compressedPic, "stucard", "uploadStucard", TAKE_PHOTO_STUCARD);
                            Glide.with(UserActivity.this).load(compressedPic).crossFade().into(userStucardImage);
                        }else{//直接上传
                            uploadAvatar(file, "stucard", "uploadStucard", TAKE_PHOTO_STUCARD);
                            Glide.with(UserActivity.this).load(file).crossFade().into(userStucardImage);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private String handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();

        Log.d(TAG, "handleImageOnKitKat's Uri: " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的uri, 则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            Log.d(TAG, "handleImageOnKitKat: docId: " + docId);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                Log.d(TAG, "handleImageOnKitKat: id: " + id + "  selection: " + selection);
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的uri, 则使用普通方式处理
            Log.d(TAG, "handleImageOnKitKat: is content type uri");
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri, 直接获取图片路径即可
            imagePath = uri.getPath();
        }

        return imagePath;

    }

    private String handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        // displayImage(imagePath);
        return imagePath;
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    // 上传图片到服务器
    private void uploadAvatar(final File file, String key, String address, final int choice) {
        //Log.d(TAG, "uploadAvatar: file type: " + type);
        //Log.d(TAG, "uploadAvatar: file size: " + file.length());
        showProgressDialog("正在上传...");
        MediaType MEDIA_TYPE = MediaType.parse("image/png");
        String url = API.INTERFACE + address;
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", userId)
                .addFormDataPart(key, file.getName(), RequestBody.create(MEDIA_TYPE, file))
                .build();
        HttpUtil.sendPostRequest(url, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                closeProgressDialog();
                Toast.makeText(UserActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        if (responseText.equals("0")) {
                            Toast.makeText(UserActivity.this, "上传图片失败", Toast.LENGTH_SHORT).show();
                        } else if (responseText.equals("1")) {
                            Toast.makeText(UserActivity.this, "上传图片成功", Toast.LENGTH_SHORT).show();
                            if (choice == TAKE_PHOTO_STUCARD) {
                                Glide.with(UserActivity.this).load(file).into(userStucardImage);
                            } else if (choice == TAKE_PHOTO) {
                                Glide.with(UserActivity.this).load(file).into(userAvatarImage);
                            }
                        } else if (responseText.equals("2")) {
                            Toast.makeText(UserActivity.this, "暂时无法使用此选项", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    // 修改真实姓名
    private void updateRealName(final String name) {
        String url = API.INTERFACE + "updateRealName";
        RequestBody body = new FormBody.Builder()
                .add("id", userId)
                .add("realname", name)
                .build();
        HttpUtil.sendPostRequest(url, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(UserActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseText.equals("0")) {
                            Toast.makeText(UserActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        } else if (responseText.equals("1")) {
                            Toast.makeText(UserActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            userRealnameText.setText(name);
                        }
                    }
                });
            }
        });
    }

    // 修改年龄
    private void updateAge(final String age) {
        String url = API.INTERFACE + "updateAge";
        RequestBody body = new FormBody.Builder()
                .add("id", userId)
                .add("age", age)
                .build();
        HttpUtil.sendPostRequest(url, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(UserActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseText.equals("0")) {
                            Toast.makeText(UserActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        } else if (responseText.equals("1")) {
                            Toast.makeText(UserActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            userAgeText.setText(age);
                        }
                    }
                });
            }
        });
    }

    // 修改身份证号
    private void updateIdcard(final String idcard) {
        String url = API.INTERFACE + "updateIdcard";
        RequestBody body = new FormBody.Builder()
                .add("id", userId)
                .add("idcard", idcard)
                .build();
        HttpUtil.sendPostRequest(url, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(UserActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseText.equals("0")) {
                            Toast.makeText(UserActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        } else if (responseText.equals("1")) {
                            Toast.makeText(UserActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            userIdcardText.setText(idcard);
                        }
                    }
                });
            }
        });
    }

    // 修改电话
    private void updatePhone(final String phone) {
        String url = API.INTERFACE + "updatePhone";
        RequestBody body = new FormBody.Builder()
                .add("id", userId)
                .add("phone", phone)
                .build();
        HttpUtil.sendPostRequest(url, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(UserActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseText.equals("0")) {
                            Toast.makeText(UserActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        } else if (responseText.equals("1")) {
                            Toast.makeText(UserActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            userPhoneText.setText(phone);
                        }
                    }
                });
            }
        });
    }

    // 修改邮箱
    private void updateEmail(final String email) {
        String url = API.INTERFACE + "updateEmail";
        RequestBody body = new FormBody.Builder()
                .add("id", userId)
                .add("email", email)
                .build();
        HttpUtil.sendPostRequest(url, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(UserActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseText.equals("0")) {
                            Toast.makeText(UserActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        } else if (responseText.equals("1")) {
                            Toast.makeText(UserActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            userEmailText.setText(email);
                        }
                    }
                });
            }
        });
    }

    // 修改个人简介
    private void updateInfo(final String info) {
        String url = API.INTERFACE + "updateInfo";
        RequestBody body = new FormBody.Builder()
                .add("id", userId)
                .add("info", info)
                .build();
        HttpUtil.sendPostRequest(url, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(UserActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseText.equals("0")) {
                            Toast.makeText(UserActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        } else if (responseText.equals("1")) {
                            Toast.makeText(UserActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            userInfoText.setText(info);
                        }
                    }
                });
            }
        });
    }

    // 修改性别
    private void updateSex(final String sex) {
        String url = API.INTERFACE + "updateSex";
        RequestBody body = new FormBody.Builder()
                .add("id", userId)
                .add("sex", sex)
                .build();
        HttpUtil.sendPostRequest(url, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(UserActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseText.equals("0")) {
                            Toast.makeText(UserActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        } else if (responseText.equals("1")) {
                            Toast.makeText(UserActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            if (sex.equals("m")) {
                                userSexText.setText("男");
                                volunteer.setSex("m");
                            } else {
                                volunteer.setSex("f");
                                userSexText.setText("女");
                            }
                        }
                    }
                });
            }
        });
    }

    // 发起实名认证请求
    private void auth() {
        String address = API.INTERFACE + "auth";
        RequestBody body = new FormBody.Builder()
                .add("id", userId)
                .build();
        HttpUtil.sendPostRequest(address, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(UserActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseText.equals("-1")) {
                            Toast.makeText(UserActivity.this, "认证失败", Toast.LENGTH_SHORT).show();
                        } else if (responseText.equals("0")) {
                            Toast.makeText(UserActivity.this, "已提交过申请啦, 请等待", Toast.LENGTH_SHORT).show();
                        } else if (responseText.equals("1")) {
                            Toast.makeText(UserActivity.this, "申请成功, 请等待审核", Toast.LENGTH_SHORT).show();
                            userAuthState.setText("等待管理员审核");
                        }
                    }
                });
            }
        });
    }
}
