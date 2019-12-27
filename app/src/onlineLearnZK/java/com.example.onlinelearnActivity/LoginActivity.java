package com.example.onlinelearnActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.util.NavigationBarCompat;
import com.example.util.NetWork;
import com.example.entity.LearnAccountEntity;
import com.example.entity.User;
import com.example.jdbc.DBManagerToUser;
import com.example.jdbc.DBManagerToUsers;
import com.example.jsonReturn.LoginReturn;
import com.example.onlinelearn.R;
import com.example.util.CodeSecurityUtil;
import com.example.util.CommonUtils;
import com.example.util.FinalStr;
import com.example.util.JsonUtil;
import com.example.util.SpUtils;
import com.example.util.StringToJson;
import com.example.util.SysUtil;
import com.example.view.captchalib.SwipeCaptchaView;
import com.example.view.captchalib.ValidateSeekBar;
import com.example.view.captchalib.ValidateSeekBar.ValidateSeekBarCallBack;
import com.example.view.dialog.AlertDialog;
import com.example.view.dialog.YinshiDialog;
import com.example.view.smoothCheckBox.SmoothCheckBox;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 登录页面
 * */

public class LoginActivity extends BaseActivity {
    private String TAG = "LoginActivity";
    @BindView(R.id.tv)
    TextView tv; //app名字
    @BindView(R.id.login_user_edit)
    EditText loginName; //用户名
    @BindView(R.id.login_passwd_edit)
    EditText loginPsw;//密码
    @BindView(R.id.checkBox)
    SmoothCheckBox rememberPsw; //记住密码按钮
    @BindView(R.id.ischeckBox)
    SmoothCheckBox ischeckBox; //同意
    @BindView(R.id.login_login_btn)
    Button loginBut; //登录按钮
    @BindView(R.id.fuwu)
    TextView fuwu; //登录按钮
    @BindView(R.id.error_tv)
    TextView err_tv;//错误框
    @BindView(R.id.swipeCaptchaView1)
    SwipeCaptchaView mSwipeCaptchaView;//拼图
    @BindView(R.id.dragBar1)
    ValidateSeekBar mSeekBar;//拖拉seekbar
    @BindView(R.id.captchalib)
    RelativeLayout shadow;//遮盖层
    @BindView(R.id.checkbox_group)
    RelativeLayout checkbox_group;//记住密码组
    @BindView(R.id.captchalib_layout)
    LinearLayout captchalib_layout; //登录验证界面
    @BindView(R.id.icon_user)
    TextView icon_user;//记住密码组
    @BindView(R.id.icon_lock)
    TextView icon_lock; //登录验证界面

    private Boolean isRememberPsw = false;//是否记住了密码
    private Boolean agree = true;//是否记住了密码
    private DBManagerToUser dbManager; //sqlite操作对象
    private DBManagerToUsers dbManagers; //sqlite操作对象
    private User user = new User();
    private String loginType = "RSA";
    private List<User> listuser;
    private Boolean netStatus=true;
    private ScaleAnimation scaleAnimation;//缩放动画
    private int error_count =0;//错误次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        NavigationBarCompat.assistActivity(findViewById(android.R.id.content));
        ischeckBox.setChecked(true);
    }

    /**
     * 初始化
     * @param view
     */
    public void initViews(View view) {

        //设置app名字
        tv.setText(CommonUtils.getAppName(this));

        dbManagers = new DBManagerToUsers(getApplicationContext());

        //设置动画
        scaleAnimation = (ScaleAnimation) AnimationUtils
                .loadAnimation(LoginActivity.this, R.anim.scale_animation);
        //本地图片生成拼图
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.bg_captcha);
        mSwipeCaptchaView.setImageBitmap(bitmap);

        icon_user.setTypeface(iconfont);
        icon_lock.setTypeface(iconfont);
        //网络图片生成拼图
		/* Glide.with(this)
	            .load("http://www.investide.cn/data/edata/image/20151201/20151201180507_281.jpg")
	            .asBitmap()
	            .into(new SimpleTarget<Bitmap>() {
	                @Override
	                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
	                	Log.d("lklk", resource.toString());
	                    mSwipeCaptchaView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.captcha_bg));
	                    mSwipeCaptchaView.createCaptcha();
	                }
	            });*/

    }

    /**
     * 监听器管理
     */
    public void setListener(){

        //记住密码按钮监听
        rememberPsw.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {

                if(isChecked){
                    isRememberPsw = true;
                }else{
                    isRememberPsw = false;
                }
            }
        });
//记住密码按钮监听
        ischeckBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {

                if(isChecked){
                    agree = true;
                }else{
                    agree = false;
                }
            }
        });
        //拼图refresh按钮监听
        findViewById(R.id.btnChange).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeCaptchaView.createCaptcha();
                mSeekBar.setEnabled(true);
                mSeekBar.setProgress(0);
            }
        });

        //拼图回调
        mSwipeCaptchaView.setOnCaptchaMatchCallback(new SwipeCaptchaView.OnCaptchaMatchCallback() {
            @Override
            public void matchSuccess(SwipeCaptchaView swipeCaptchaView) {
                Toast.makeText(LoginActivity.this, "登录验证成功", Toast.LENGTH_SHORT).show();
                //swipeCaptcha.createCaptcha();
                mSeekBar.setEnabled(false);
                shadow.setVisibility(View.INVISIBLE);
                captchalib_layout.setVisibility(View.INVISIBLE);
                error_count=0;

            }

            @Override
            public void matchFailed(SwipeCaptchaView swipeCaptchaView) {

                Toast.makeText(LoginActivity.this, "登录验证失败", Toast.LENGTH_SHORT).show();
                swipeCaptchaView.resetCaptcha();
                mSeekBar.setProgress(0);
                mSeekBar.refreshText();
            }
        });

        //seekbar回调监听
        mSeekBar.setValidateSeekBarCallBack(new ValidateSeekBarCallBack(){

            @Override
            public void onProgressChangedCallBack(SeekBar seekbar,
                                                  int progress, boolean arg2) {
                mSwipeCaptchaView.setCurrentSwipeValue(progress);

            }

            @Override
            public void onStartTrackingTouchCallBack(SeekBar seekbar) {
                //随便放这里是因为控件
                mSeekBar.setMax(mSwipeCaptchaView.getMaxSwipeValue());

            }

            @Override
            public void onStopTrackingTouchCallBack(SeekBar seekbar) {

                mSwipeCaptchaView.matchCaptcha();

            }


        });

        //加强点击效果
        checkbox_group.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View arg0) {

                boolean ischeck =rememberPsw.isChecked();
                rememberPsw.setChecked(!ischeck, true);
            }

        });

        //取消loginType
        loginPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginType="";
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        loginName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginType="";
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //监测软键盘的完成动作
        loginPsw.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_DONE){

                    closeInput();//关闭软件盘
                    login();//登录操作
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 业务逻辑
     * @param mContext
     */
    public void doBusiness(Context mContext) {
       /* isFileExistInSD();  //同步磁盘文件及数据库的信息*/
        //是否有版本更新
        isVesionUpdate();
        //收次弹出隐私对话框
        Boolean isFrist = SpUtils.getBoolean(this, "isFrist");
        if(!isFrist){
            isFristTime();
        }
    }

    private void isFristTime() {
        final YinshiDialog alertDialog = new YinshiDialog(this);

        //部分字体上色可点击
        /*final SpannableStringBuilder style = new SpannableStringBuilder();
        //设置文字
        style.append("关于本活动更多规则，请点我查看");
        //设置部分文字点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
//                Toast.makeText(LoginActivity.this, "触发点击事件!", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
                Intent intent = new Intent(LoginActivity.this,FuWuActivity.class);
                startActivity(intent);
            }
        };
        style.setSpan(clickableSpan, 11, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        alertDialog.setMessage(style);

        //设置部分文字颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#1f8be7"));
        style.setSpan(foregroundColorSpan, 11, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //配置给TextView
        alertDialog.setMovementMethod(LinkMovementMethod.getInstance());
        alertDialog.setMessage(style);*/

        alertDialog.setTitle("用户隐私政策概要");
        alertDialog.setMessage("在线学习App（系统）隐私政策\n" +
                "一、我们如何收集和使用您的个人信息\n" +
                "（一）我们如何收集您的信息\n" +
                "我们会出于本政策所述的以下目的，收集和使用您的个人信息：\n"+"1.帮助您成为本系统用户\n" +
                "为成为本系统用户（学习者），以便我们为您提供服务，包括内部论坛等实现沟通的功能，您需要提供手机号码、姓名、性别、身份证信息、电子邮箱信息、受教育信息、职业信息等个人信息，并激活本系统用户账号及修改默认密码。\n" +
                "成为本系统用户后，用户按照适用的业务规定通过系统功能对个人信息做修改，需要遵循业务流程使修改生效。用户按照适用的业务规定主动注销账户之后，我们将停止为您提供服务，根据适用法律的要求进行归档处理。\n" +
                "就本系统账号注销流程，需要遵循适用的业务规定和流程，您可以查阅本系统用户手册了解具体内容。\n" +
                "2.向您提供服务\n" +
                "（1）您向我们提供的信息\n" +
                "在服务使用过程中，您可以反馈对本系统提供的服务的体验问题，帮助我们更好地了解您使用我们服务的体验和需求，改善我们的服务。\n" +
                "（2）我们在您使用服务过程中收集的信息\n" +
                "为向您提供更契合您需求的智能硬件和/或软件、页面展示和搜索结果、了解产品适配性、识别账号异常状态，我们会收集关于您使用服务以及使用方式的信息并将这些信息进行关联，这些信息包括：\n" +
                "设备信息：我们会根据您在软件安装及使用中授予的具体权限，接收并记录您所使用的设备相关信息（例如设备型号、操作系统版本、设备设置、唯一设备标识符等软硬件特征信息）、设备所在位置相关信息（例如IP地址、GPS/北斗位置信息以及能够提供相关信息的Wi-Fi接入点、蓝牙和基站等传感器信息）。\n" +
                "日志信息：当您使用本系统提供的服务时，我们会自动收集您对我们服务的详细使用情况，作为有关网络日志保存。例如您的搜索查询内容、IP" +
                "地址、浏览器的类型、电信运营商、使用的语言、访问日期和时间、您访问的网页记录、通话状态信息等。\n" +
                "请注意，单独的设备信息、日志信息等是无法识别特定自然人身份的信息。如果我们将这类非个人信息与其他信息结合用于识别特定自然人身份，或者将其与个人信息结合使用，则在结合使用期间，这类非个人信息将被视为个人信息，除取得您授权或法律法规另有规定外，我们会将该类个人信息做匿名化、去标识化处理。\n" +
                "当您与我们联系时，我们可能会保存您的通信/通话记录和内容或您留下的联系方式等信息，以便与您联系或帮助您解决问题，或记录相关问题的处理方案及结果。\n" +
                "（3）我们向第三方提供的信息\n" +
                "对于您在本系统开通使用的第三方应用服务，我们将在您启动该应用时收集相关信息，并按照本隐私权政策进行处理；第三方应用服务提供者可能会获取您使用本系统服务的相关个人信息，该第三方应用服务对您的个人信息的收集（如有）则受该第三方隐私权政策约束。\n" +
                "（4）第三方向我们提供的信息\n" +
                "本系统将对其他用户进行与您相关的操作时收集关于您的信息，亦可能从本系统合作第三方或通过其他合法途径获取的您的个人数据信息。\n" +
                "3.为您提供安全保障\n" +
                "为预防、发现、调查欺诈、侵权、危害安全、非法或违反与我们的协议、政策或规则的行为，我们可能收集或整合您的用户信息、服务使用信息、设备信息、日志信息以及我们合作伙伴取得您授权或依据法律共享的信息。\n" +
                "4.其他用途\n" +
                "我们将基于本政策未载明的其他特定目的收集您的信息时，会事先征求您的同意。\n" +
                "本系统收集及使用上述信息的目的是为了更好地运营本系统提供服务（包括但不限于向您提供个性化的服务），并且会通过包括但不限于通过网站通知、电子邮件、电话或短信息等方式通知。\n" +
                "如我们停止运营本系统，我们将及时停止继续收集您个人信息的活动，将停止运营的通知以逐一送达或公告的形式通知您，对所持有的个人信息进行删除或归档处理。\n" +
                "（二）我们如何使用您的信息\n" +
                "收集您的信息是为了向您提供服务及提升服务质量，为了实现这一目的，我们会把您的信息用于下列用途：\n" +
                "1.向您提供本系统的服务，并维护、改进、优化这些服务及服务体验；\n" +
                "2.为预防、发现、调查欺诈、侵权、危害安全、非法或违反与我们的协议、政策或规则的行为，保护您、其他用户或公众，我们的合法权益，我们可能使用或整合您的用户信息、服务使用信息、设备信息、日志信息以及我们合作伙伴取得您授权或依据法律共享的信息，来综合判断您账户及交易风险、进行身份验证、检测及防范安全事件，并依法采取必要的记录、审计、分析、处置措施；\n" +
                "3.我们可能会将您的信息进行处理或者与来自其他服务的信息结合起来，用于为了向您提供更加个性化的服务使用，例如向您推荐可能感兴趣的内容，包括但不限于向您发出产品和服务信息，或通过系统向您展示个性化的第三方推广信息，或者在征得您同意的情况下与本系统合作伙伴共享信息以便他们向您发送有关其产品和服务的信息；\n" +
                "4.经您许\n"+"应用服务提供者可能会获取您使用本系统服务的相关个人信息，该第三方应用服务对您的个人信息的收集（如有）则受该第三方隐私权政策约束。\n" +
                "（4）第三方向我们提供的信息\n" +
                "本系统将对其他用户进行与您相关的操作时收集关于您的信息，亦可能从本系统合作第三方或通过其他合法途径获取的您的个人数据信息。\n" +
                "3.为您提供安全保障\n" +
                "为预防、发现、调查欺诈、侵权、危害安全、非法或违反与我们的协议、政策或规则的行为，我们可能收集或整合您的用户信息、服务使用信息、设备信息、日志信息以及我们合作伙伴取得您授权或依据法律共享的信息。\n" +
                "4.其他用途\n" +
                "我们将基于本政策未载明的其他特定目的收集您的信息时，会事先征求您的同意。\n" +
                "本系统收集及使用上述信息的目的是为了更好地运营本系统提供服务（包括但不限于向您提供个性化的服务），并且会通过包括但不限于通过网站通知、电子邮件、电话或短信息等方式通知。\n" +
                "如我们停止运营本系统，我们将及时停止继续收集您个人信息的活动，将停止运营的通知以逐一送达或公告的形式通知您，对所持有的个人信息进行删除或归档处理。\n" +
                "（二）我们如何使用您的信息\n" +
                "二、我们如何使用 Cookie 和同类技术\n" +
                "为使您获得更轻松的访问体验，您使用本系统提供的服务时，我们可能会通过采用各种技术收集和存储您访问本系统的相关数据，在您访问或再次访问本系统时，我们能识别您的身份，并通过分析数据为您提供更好更多的服务。包括使用小型数据文件识别您的身份，这么做是为了解您的使用习惯，帮您省去重复输入账户信息的步骤，或者帮助判断您的账户安全。这些数据文件可能是Cookie、Flash Cookie，或您的浏览器或关联应用程序提供的其他本地存储（统称“Cookie”）。 请您理解，我们的某些服务目前只能通过使用Cookie才可得到实现。如果您的浏览器或浏览器附加服务允许，您可以修改对Cookie的接受程度或者拒绝本系统的Cookie，但拒绝本系统的Cookie在某些情况下您可能无法使用依赖于Cookies的本系统的部分服务或功能。\n" +
                "网页上常会包含一些电子图像，称为“单像素 GIF 文件”或“网络 " +
                "beacon”，它可以帮助网站计算浏览网页的用户或访问某些Cookie。我们可能会通过网络beacon" +
                "收集您浏览网页活动信息，例如您访问的页面地址、您先前访问的援引页面的位址、您停留在页面的时间、您的浏览环境以及显示设定等。\n" +
                "三、我们如何共享、转让、公开披露您的信息\n" +
                "（一）共享\n" +
                "我们不会与本系统提供者以外的公司、组织和个人共享您的个人信息，但以下情况除外：\n" +
                "1.在获取明确同意的情况下共享：获得您的明确同意后，我们会与其他方共享您的个人信息；\n" +
                "2.在法定情形下的共享：我们可能会根据法律法规规定、诉讼争议解决需要，或按行政、司法机关依法提出的要求，对外共享您的个人信息；\n" +
                "3.只有透露您的资料，才能提供您所要求的产品和服务；\n" +
                "4.在您被他人投诉侵犯知识产权或其他合法权利时，需要向投诉人披露您的必要资料，以便进行投诉处理的；\n" +
                "5.与授权合作伙伴共享：仅为实现本隐私政策中声明的目的，我们的某些服务将由我们和授权合作伙伴共同提供。我们可能会与合作伙伴共享您的某些个人信息，以提供更好的客户服务和用户体验。我们仅会出于合法、正当、必要、特定、明确的目的共享您的个人信息，并且只会共享提供服务所必要的个人信息。我们的合作伙伴无权将共享的个人信息用于与产品或服务无关的其他用途。\n" +
                "本系统服务含有到其他网站的链接。除法律另有规定外，本系统对其他网站的隐私保护措施不负任何责任。我们可能在任何需要的时候增加商业伙伴或共用品牌的网站，但是提供给他们的将仅是综合信息，我们将不会公开您的身份。\n" +
                "（二）转让\n"+"我们不会将您的个人信息转让给任何公司、组织和个人，但以下情况除外：\n" +
                "1.在获取明确同意的情况下转让：获得您的明确同意后，我们会向其他方转让您的个人信息。\n" +
                "2.本系统服务提供者发生合并、收购或破产清算情形，或其他涉及合并、收购或破产清算情形时，如涉及到个人信息转让，我们会要求新的持有您个人信息的公司、组织继续受本政策的约束，否则我们将要求该公司、组织和个人重新向您征求授权同意。\n" +
                "（三）公开披露\n" +
                "我们仅会在以下情况下，公开披露您的个人信息：\n" +
                "1.获得您明确同意或基于您的主动选择，我们可能会公开披露您的个人信息；\n" +
                "2.如果我们确定您出现违反法律法规或严重违反本系统相关协议规则的情况，或为保护本系统用户或公众的人身财产安全免遭侵害，我们可能依据法律法规或本系统相关协议规则征得您同意的情况下披露关于您的个人信息，包括相关违规行为以及本系统已对您采取的措施。\n" +
                "（四）共享、转让、公开披露个人信息时事先征得授权同意的例外\n" +
                "以下情形中，共享、转让、公开披露您的个人信息无需事先征得您的授权同意：\n" +
                "1.与国家安全、国防安全有关的；\n" +
                "2.与公共安全、公共卫生、重大公共利益有关的；\n" +
                "3.与犯罪侦查、起诉、审判和判决执行等有关的；\n" +
                "4.出于维护您或其他个人的生命、财产等重大合法权益但又很难得到本人同意的；\n" +
                "5.您自行向社会公众公开的个人信息；\n" +
                "6.从合法公开披露的信息中收集个人信息的，如合法的新闻报道、政府信息公开等渠道。\n" +
                "根据法律规定，共享、转让经去标识化处理的个人信息，且确保数据接收方无法复原并重新识别个人信息主体的，不属于个人信息的对外共享、转让及公开披露行为，对此类数据的保存及处理将无需另行向您通知并征得您的同意。\n" +
                "四、我们如何保护您的信息\n" +
                "我们会采取各种预防措施来保护您的个人信息，以保障您的个人信息免遭丢失、盗用和误用，以及被擅自取阅、披露、更改或销毁。为确保您个人信息的安全，我们有严格的信息安全规定和流程在公司内部执行上述措施。\n" +
                "我们会采取合理可行的措施，尽力避免收集无关的个人信息，并在限于达成本政策所述目的所需的期限内保留您的个人信息，除非需要延长保留期限或在法律的允许期限内。\n"+"在不幸发生个人信息安全事件后，我们将按照法律法规的要求（并最迟不迟于30 个自然日内）向您告知：安全事件的基本情况和可能的影响、我们已采取或将要采取的处置措施、您可自主防范和降低风险的建议、对您的补救措施等。事件相关情况我们将以邮件、信函、电话、推送通知等方式告知您，难以逐一告知个人信息主体时，我们会采取合理、有效的方式发布公告。同时，我们还将按照监管部门要求，上报个人信息安全事件的处置情况。\n" +
                "互联网环境并非百分之百安全，尽管我们有这些安全措施，但请注意在互联网上不存在“完善的安全措施”，我们将尽力确保您的信息的安全性。\n" +
                "五、未成年人保护\n" +
                "我们重视未成年人的信息保护，如您为未成年人的，建议您请您的父母或监护人仔细阅读本隐私权政策，并在征得您的父母或监护人同意的前提下使用本系统的服务或向我们提供信息。对于经父母或监护人同意使用本系统服务而收集未成年人个人信息的情况，我们只会在法律法规允许，父母或监护人明确同意或者保护未成年人所必要的情况下使用，共享，转让或披露此信息。我们将根据国家相关法律法规及本《隐私政策》的规定保护未成年人的个人信息。\n" +
                "六、您的个人信息如何在全球范围转移\n" +
                "我们在中华人民共和国境内运营中收集和产生的个人信息，存储在中华人民共和国境内。就中华人民共和国境外本系统用户使用本系统服务过程中产生的个人信息，您同意回传存储在中国境内的服务器上，否则您无法使用现有本系统服务。在此类情况下，我们会确保您的个人信息得到在中华人民共和国境内足够同等的保护。\n" +
                "七、本政策如何更新\n" +
                "我们的隐私政策可能变更。\n" +
                "未经您明确同意我们不会限制您按照本隐私政策所应享有的权利。\n" +
                "对于重大变更，我们提供更为显著的通知（包括公告通知和弹窗提示）。\n" +
                "本政策所指的重大变更包括但不限于：\n" +
                "1.本系统服务模式发生重大变化。如处理用户信息的目的、用户信息的使用方式等；\n" +
                "2.我们在控制权、组织架构等方面发生重大变化。如业务调整、破产并购等引起的所有者变更等；\n" +
                "3.用户信息共享、转让或公开披露的主要对象发生变化；\n" +
                "4.我们负责处理用户信息安全的责任部门、联络方式及投诉渠道发生变化时；\n" +
                "5.用户信息安全影响评估报告表明存在高风险时。\n"+"八、如何联系我们\n" +
                "如果您对本系统的隐私政策或数据处理有任何疑问、意见或建议，可以通过以下地址与我们联系，我们邮寄联系方式如下：\n" +
                " 收件人：广州华南教育科技发展有限公司\n" +
                " 地址：广州天河区五山路381号华南理工大学北区网络教育大楼\n" +
                " 邮编：510640\n" +
                "一般情况下，我们将在收到您相关联系信息后三十天内回复。\n" +
                "如果您对我们的回复不满意，特别是您认为我们的个人信息处理行为损害了您的合法权益，您还可以通过向广州市人民法院提起诉讼来寻求解决方案。\n" +
                "（文档结尾）\n");
        alertDialog.setAlertmsgSureOnclick(new OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                SpUtils.putBoolean(LoginActivity.this, "isFrist",true);
            }
        }, "同意");
        alertDialog.setAlertmsgCancelOnclick(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        },"退出APP");
    }


    @OnClick({R.id.login_login_btn,R.id.fuwu})
    public void onClick(View view)  {
        Intent intent = null;
        switch (view.getId()) {
            //登录操作
            case R.id.login_login_btn:
                if(agree){
                    login();
                }else {
                  Toast.makeText(getApplicationContext(),"同意服务条款后登录..",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fuwu:
                intent = new Intent(LoginActivity.this,FuWuActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 登录操作
     */
    public void login(){
        //如果没有权限
        if (!isGetPermissions){
            checkPermissions(mNeedPermissions);
            return;
        }
        if(!loginType.equals("MD5")){
            user.setUserName(loginName.getText()+"");
            user.setUserPsw(loginPsw.getText()+"");
        }
        err_tv.setText("");
        if(SysUtil.isBlank(user.getUserName()) || SysUtil.isBlank(user.getUserPsw())){
            err_tv.setText("在线学习帐号或者密码不能\n为空，请输入后再登录！");
        }else{
            getResult();//登录
        }
    }

    /**
     * 离线登录/在线登录
     */
    public void getResult()  {
        //是否记住了密码
         if(isRememberPsw){ //记住密码
            //查看用户表里是否有数据
            if(null != listuser && listuser.size() > 0){
                user = listuser.get(0);
            }
        }else{
            user.setUserName(loginName.getText()+"");
            user.setUserPsw(loginPsw.getText()+"");
            if(null != listuser && listuser.size() > 0){
                dbManager.deleteUser();
            }
        }
        //点击登录 判断是否联网
        netStatus= NetWork.isNetworkConnected(LoginActivity.this);
        if (netStatus) {
            loginThread();
        }else {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("student_code", user.getUserName());
            map.put("student_password", user.getUserPsw());
            LearnAccountEntity entity=dbManagers.queryByMap(map);
            if (entity!=null) {//数据库有数据，则不用登录
                loginSuccess(entity);
            }else {
                err_tv.setText("登录错误，请重新输入或联网登录");

            }
        }
    }

    /**
     * 登录线程
     */
    private void loginThread(){
        showLoading();//显示加载框
        //调用接口方法
        Call<ResponseBody> call = serverApi.login(user.getUserName(),user.getUserPsw(),loginType);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) { //登录成功
                    try {
                        String result =response.body().string();
                        Log.d(JSON_TAG,"登录成功返回："+result);
                        //gson解析
                        LoginReturn loginReturn= JsonUtil.parserString(result, LoginReturn.class);
                        if(loginReturn!=null){
                            if (loginReturn.isSuccess()){
                                LearnAccountEntity learnAccountEntity=loginReturn.getLearnAccountEntity();
                                loginSuccess(learnAccountEntity);
                            }else{
                                loginFail(StringUtils.isNotBlank(loginReturn.getMsg()) ?
                                        loginReturn.getMsg(): "登录失败,\n请重试！");
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                closeLoading();//关闭加载框
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                err_tv.setText("登录异常，请检查你的网\n络连接或者联系我们！");
                closeLoading();//关闭加载框
            }
        });
    }

    /**
     * 登录成功
     * @param learnAccountEntity
     */
    private void loginSuccess(LearnAccountEntity learnAccountEntity)  {


        Bundle bundle=new Bundle();
        bundle.putString("stuid",learnAccountEntity.getId());
        bundle.putInt("type", 1);
        //保存用户信息到缓存
        hdaplication.setStuid(learnAccountEntity.getId());
        hdaplication.setUserXm(learnAccountEntity.getStudentName());
        hdaplication.setUsername(loginName.getText()+"");

        //判断是否联网，联网则同步数据
        if (netStatus) {
            //添加记录到用户信息表
            User users=new User();
            users.setUserId(learnAccountEntity.getId());
            users.setUserName(user.getUserName());
            users.setUserXm(learnAccountEntity.getStudentName());
            users.setUserPsw(learnAccountEntity.getStudentPassword());

            dbManagers.saveUser(learnAccountEntity);
        }
        String psw=user.getUserPsw();
        if(!loginType.equals("MD5")){
            try {
                psw=CodeSecurityUtil.getMD5(loginPsw.getText()+"");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //是否需要记住密码
        if(isRememberPsw){
            if(null != listuser && listuser.size() > 0){
                dbManager.updateUser(1, loginName.getText()+"",psw );
            }else{
                user.setId(1);
                user.setUserName(loginName.getText()+"");
                user.setUserPsw(psw);
                dbManager.saveUser(user);
            }
        }
        startActivity(CourseActivity.class,bundle);

    }

    /**
     * 登录失败
     */
    private void loginFail(String msg){
        if(null != listuser && listuser.size() > 0){
            dbManager.deleteUser();
            loginType = "RSA";
        }
        err_tv.setText(msg);
        error_count++;
        if(error_count>=3){
            loginValidate();//登录验证
        }
    }

    /**
     * 登录验证
     */
    private void loginValidate(){
        if(shadow==null){

            int width = captchalib_layout.getMeasuredWidth();  //屏幕宽度
            ViewGroup.LayoutParams lp;
            lp= captchalib_layout.getLayoutParams();
            lp.height=width;
            captchalib_layout.setLayoutParams(lp);  //设置正方形
            //禁止点击下层
            shadow.setOnTouchListener(new OnTouchListener(){
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    return true;
                }
            });
        }
        mSeekBar.setProgress(0);
        mSeekBar.setEnabled(true);
        mSwipeCaptchaView.createCaptcha();
        shadow.setVisibility(View.VISIBLE);
        captchalib_layout.startAnimation(scaleAnimation);

    }


    public void isAutomaticLogin(){
        try {
            dbManager = new DBManagerToUser(getApplicationContext());
            listuser = dbManager.queryUser();
            if(null != listuser && listuser.size() > 0){
                rememberPsw.setChecked(true);
                user = listuser.get(0);
                loginName.setText("");
                loginName.setText(user.getUserName());
                loginPsw.setText("");
                loginPsw.setText("******");
                loginType = "MD5";
            }else{
                rememberPsw.setChecked(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("登录","登录检验是否记住密码错误");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 200://提交dug后返回
                isAutomaticLogin();
                break;
        }
    }
    @Override
    public void onStart() {
        super.onStart();

        isAutomaticLogin();

    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
//        isFristTime();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    /**
     * 本地文件列表与磁盘列表对应
     */
    private void isFileExistInSD() {
      /*  List<FileInfo> fileInfos = fileDao.queryAllFiles();
        List<FileInfo> deleteList = new ArrayList<>();
        for (FileInfo fileInfo : fileInfos) {
            //如果硬盘上不存在该文件
            if (fileInfo.getFile_finish()!=0 &&FileUtil.isExist(FinalStr.DOWNLOAD_PATH, fileInfo.getAttach_name()) == null) {
                deleteList.add(fileInfo);
            }
        }
        fileDao.deleteFiles(deleteList);

        List <FileInfo> F =fileDao.queryAllFiles();
        List <ThreadInfo> T = threadDAO.queryAllThreads();*/
    }

    /**
     * 重写返回按钮
     */
    public void onBackPressed() {
        //返回home,不销毁
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);

        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(mHomeIntent);

    }


}
