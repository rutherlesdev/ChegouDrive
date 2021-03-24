package com.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AlertDialog;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.chegou.motorista.BankDetailActivity;
import com.chegou.motorista.BookingsActivity;
import com.chegou.motorista.CardPaymentActivity;
import com.chegou.motorista.ContactUsActivity;
import com.chegou.motorista.DriverFeedbackActivity;
import com.chegou.motorista.EmergencyContactActivity;
import com.chegou.motorista.HelpActivity;
import com.chegou.motorista.InviteFriendsActivity;
import com.chegou.motorista.ListOfDocumentActivity;
import com.chegou.motorista.ManageVehiclesActivity;
import com.chegou.motorista.MyGalleryActivity;
import com.chegou.motorista.MyProfileActivity;
import com.chegou.motorista.MyWalletActivity;
import com.chegou.motorista.NotificationActivity;
import com.chegou.motorista.PrefranceActivity;
import com.chegou.motorista.R;
import com.chegou.motorista.SetAvailabilityActivity;
import com.chegou.motorista.StaticPageActivity;
import com.chegou.motorista.StatisticsActivity;
import com.chegou.motorista.SubscriptionActivity;
import com.chegou.motorista.UfxCategoryActivity;
import com.chegou.motorista.UploadDocTypeWiseActivity;
import com.chegou.motorista.VerifyInfoActivity;
import com.chegou.motorista.WayBillActivity;
import com.chegou.motorista.deliverAll.LiveTaskListActivity;
import com.dialogs.OpenListView;
import com.general.files.AppFunctions;
import com.general.files.ExecuteWebServerUrl;
import com.general.files.GeneralFunctions;
import com.general.files.InternetConnection;
import com.general.files.MyApp;
import com.general.files.SetUserData;
import com.general.files.StartActProcess;
import com.livechatinc.inappchat.ChatWindowActivity;

import com.utils.Logger;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;
import com.view.editBox.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyProfileFragment extends Fragment {

    private static final String TAG = "MyProfileFragment";
    public String userProfileJson = "";
    public JSONObject obj_userProfile;
    GeneralFunctions generalFunc;

    ImageView backImg, editProfileImage;
    SelectableRoundedImageView userImgView, userImgView_toolbar;
    MTextView userNameTxt, userNameTxt_toolbar, userEmailTxt, walletHTxt, walletVxt, generalSettingHTxt, accountHTxt;
    MTextView bookingTxt, inviteTxt, topupTxt;
    MTextView notificationHTxt, paymentHTxt, privacyHTxt, termsHTxt, myPaymentHTxt, mybookingHTxt,
            addMoneyHTxt, sendMoneyHTxt, personalDetailsHTxt, changePasswordHTxt, changeCurrencyHTxt, changeLanguageHTxt, supportHTxt, livechatHTxt, contactUsHTxt;
    LinearLayout notificationArea, paymentMethodArea, privacyArea, myBookingArea,
            addMoneyArea, sendMoneyArea, personalDetailsArea, changesPasswordArea, changesCurrancyArea, changeslanguageArea, termsArea, liveChatArea, contactUsArea, verifyEmailArea, verifyMobArea;
    View notificationView, paymentView, rewardView, myBookingView, addMoneyView, aboutUsView, myWalletView, statisticsView, wayBillView,
            sendMoneyView, personalDetailsView, changePasswordView, changeCurrencyView, changeLangView, termsView, livechatView, mySubView, myServiceView, manageGallleryView, myAvailView, verifyEmailView, verifyMobView;
    LinearLayout bookingArea, inviteArea, topUpArea, logOutArea;
    LinearLayout myWalletArea, inviteFriendArea, helpArea, aboutusArea, headerwalletArea, emeContactArea, bankDetailsArea, manageDocArea, mySubArea,
            myServiceArea, manageGalleryArea, myAvailArea, statisticsArea, userFeedbackArea, wayBillArea;
    MTextView mywalletHTxt, inviteHTxt, helpHTxt, aboutusHTxt, logoutTxt, otherHTxt, headerwalletTxt, emeContactHTxt, bankDetailsHTxt, manageDocHTxt,
            mySubHTxt, myServiceHTxt, manageGalleryHTxt, myAvailHTxt, statisticsHTxt, userFeedbackHTxt, wayBillHTxt, verifyEmailHTxt, verifyMobHTxt;
    ImageView notificationArrow, paymentArrow, privacyArrow, termsArrow, mywalletArrow, inviteArrow, helpArrow, aboutusArrow, statisticsArrow, wayBillArrow,
            mybookingArrow, addMoneyArrow, sendMoneyArrow, personalDetailsArrow, manageDocArrow, mySubArrow, myServiceArrow, myAvailArrow, userFeedbackArrow,
            changePasswordArrow, changeCurrencyArrow, changeLangArrow, livechatArrow, contactUsArrow, logoutArrow, emeContactArrow, bankDetailsArrow, manageGalleryArrow, verifyMobsArrow, verifyEmailArrow;

    View view;
    InternetConnection internetConnection;
    String ENABLE_FAVORITE_DRIVER_MODULE_KEY = "";
    boolean isDeliverOnlyEnabled;
    boolean isAnyDeliverOptionEnabled;
    AlertDialog alertDialog;
    String SITE_TYPE = "";
    String SITE_TYPE_DEMO_MSG = "";


    ArrayList<HashMap<String, String>> language_list = new ArrayList<>();
    String selected_language_code = "";
    String default_selected_language_code = "";

    ArrayList<HashMap<String, String>> currency_list = new ArrayList<>();

    String selected_currency = "";
    String default_selected_currency = "";
    String selected_currency_symbol = "";
    AlertDialog list_currency;

    LinearLayout toolbar_profile;
    String app_type = "";
    int MY_BOOKING_REQ_CODE = 788;
    private boolean isUfxServicesEnabled = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        if (view != null) {
//            return view;
//        }
        view = inflater.inflate(R.layout.activity_my_profile_new, container, false);
        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());

        updateUserProfileObj();

        internetConnection = new InternetConnection(getActContext());
        initViews();
        setLabel();
        setuserInfo();
        manageView();
        buildLanguageList();


        return view;

    }

    public void manageView() {
        if (generalFunc.getJsonValue("ENABLE_LIVE_CHAT", userProfileJson).equalsIgnoreCase("Yes")) {
            liveChatArea.setVisibility(View.VISIBLE);
            livechatView.setVisibility(View.VISIBLE);
        } else {
            liveChatArea.setVisibility(View.GONE);
            livechatView.setVisibility(View.GONE);
        }


        if (!generalFunc.getJsonValueStr(Utils.WALLET_ENABLE, obj_userProfile).equals("") &&
                generalFunc.getJsonValueStr(Utils.WALLET_ENABLE, obj_userProfile).equalsIgnoreCase("Yes")) {
            myWalletArea.setVisibility(View.VISIBLE);
            headerwalletArea.setVisibility(View.VISIBLE);
            myWalletView.setVisibility(View.VISIBLE);
            addMoneyArea.setVisibility(View.VISIBLE);
            sendMoneyArea.setVisibility(View.VISIBLE);
            addMoneyView.setVisibility(View.VISIBLE);
            sendMoneyView.setVisibility(View.VISIBLE);
            topUpArea.setVisibility(View.VISIBLE);


        } else {
            myWalletArea.setVisibility(View.GONE);
            headerwalletArea.setVisibility(View.GONE);
            myWalletView.setVisibility(View.GONE);
            addMoneyArea.setVisibility(View.GONE);
            sendMoneyArea.setVisibility(View.GONE);
            addMoneyView.setVisibility(View.GONE);
            sendMoneyView.setVisibility(View.GONE);
            topUpArea.setVisibility(View.GONE);
        }


        if (generalFunc.retrieveValue(Utils.DRIVER_SUBSCRIPTION_ENABLE_KEY).equalsIgnoreCase("Yes")) {
            mySubArea.setVisibility(View.VISIBLE);
            mySubView.setVisibility(View.VISIBLE);
        } else {
            mySubArea.setVisibility(View.GONE);
            mySubView.setVisibility(View.GONE);

        }

        if (generalFunc.getJsonValue("ENABLE_NEWS_SECTION", userProfileJson) != null && generalFunc.getJsonValue("ENABLE_NEWS_SECTION", userProfileJson).equalsIgnoreCase("yes")) {
            notificationArea.setVisibility(View.VISIBLE);
            notificationView.setVisibility(View.VISIBLE);

        } else {
            notificationArea.setVisibility(View.GONE);
            notificationView.setVisibility(View.GONE);

        }
        if (!generalFunc.getJsonValueStr(Utils.REFERRAL_SCHEME_ENABLE, obj_userProfile).equals("") && generalFunc.getJsonValueStr(Utils.REFERRAL_SCHEME_ENABLE, obj_userProfile).equalsIgnoreCase("Yes")) {
            inviteFriendArea.setVisibility(View.VISIBLE);
            inviteArea.setVisibility(View.VISIBLE);
        } else {
            inviteFriendArea.setVisibility(View.GONE);
            inviteArea.setVisibility(View.GONE);
        }

        if (generalFunc.getJsonValue("SYSTEM_PAYMENT_FLOW", userProfileJson).equalsIgnoreCase("Method-1") && !generalFunc.getJsonValueStr("APP_PAYMENT_MODE", obj_userProfile).equalsIgnoreCase("Cash")) {
            paymentMethodArea.setVisibility(View.VISIBLE);
            paymentView.setVisibility(View.VISIBLE);

        } else {
            paymentMethodArea.setVisibility(View.GONE);
            paymentView.setVisibility(View.GONE);
        }

        JSONArray currencyList_arr = generalFunc.getJsonArray(generalFunc.retrieveValue(Utils.CURRENCY_LIST_KEY));

        if (currencyList_arr!=null){
            if (currencyList_arr.length() < 2) {
                changesCurrancyArea.setVisibility(View.GONE);
                changeCurrencyView.setVisibility(View.GONE);
            } else {
                changesCurrancyArea.setVisibility(View.VISIBLE);
                changeCurrencyView.setVisibility(View.VISIBLE);
            }
        }

        HashMap<String, String> data = new HashMap<>();
        data.put(Utils.LANGUAGE_LIST_KEY, "");
        data.put(Utils.LANGUAGE_CODE_KEY, "");
        data = generalFunc.retrieveValue(data);

        JSONArray languageList_arr = generalFunc.getJsonArray(data.get(Utils.LANGUAGE_LIST_KEY));

        if (languageList_arr.length() < 2) {
            changeslanguageArea.setVisibility(View.GONE);
        } else {
            changeslanguageArea.setVisibility(View.VISIBLE);
        }

    }

    public void initViews() {
        backImg = view.findViewById(R.id.backImg);
        editProfileImage = view.findViewById(R.id.editProfileImage);
        userImgView = view.findViewById(R.id.userImgView);
        userImgView_toolbar = view.findViewById(R.id.userImgView_toolbar);
        userNameTxt = view.findViewById(R.id.userNameTxt);
        userNameTxt_toolbar = view.findViewById(R.id.userNameTxt_toolbar);
        userEmailTxt = view.findViewById(R.id.userEmailTxt);
        walletHTxt = view.findViewById(R.id.walletHTxt);
        walletVxt = view.findViewById(R.id.walletVxt);
        bookingTxt = view.findViewById(R.id.bookingTxt);
        inviteTxt = view.findViewById(R.id.inviteTxt);
        topupTxt = view.findViewById(R.id.topupTxt);
        headerwalletTxt = view.findViewById(R.id.headerwalletTxt);
        generalSettingHTxt = view.findViewById(R.id.generalSettingHTxt);
        accountHTxt = view.findViewById(R.id.accountHTxt);
        notificationHTxt = view.findViewById(R.id.notificationHTxt);
        paymentHTxt = view.findViewById(R.id.paymentHTxt);
        privacyHTxt = view.findViewById(R.id.privacyHTxt);
        termsHTxt = view.findViewById(R.id.termsHTxt);
        logoutTxt = view.findViewById(R.id.logoutTxt);
        otherHTxt = view.findViewById(R.id.otherHTxt);

        notificationArea = view.findViewById(R.id.notificationArea);
        paymentMethodArea = view.findViewById(R.id.paymentMethodArea);
        privacyArea = view.findViewById(R.id.privacyArea);
        logoutTxt.setText(generalFunc.retrieveLangLBl("", "LBL_LOGOUT"));
        otherHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_OTHER_TXT"));

        myBookingArea = view.findViewById(R.id.myBookingArea);

        addMoneyArea = view.findViewById(R.id.addMoneyArea);
        sendMoneyArea = view.findViewById(R.id.sendMoneyArea);
        personalDetailsArea = view.findViewById(R.id.personalDetailsArea);
        changesPasswordArea = view.findViewById(R.id.changesPasswordArea);
        changesCurrancyArea = view.findViewById(R.id.changesCurrancyArea);
        changeslanguageArea = view.findViewById(R.id.changeslanguageArea);
        termsArea = view.findViewById(R.id.termsArea);
        liveChatArea = view.findViewById(R.id.liveChatArea);
        contactUsArea = view.findViewById(R.id.contactUsArea);
        verifyEmailArea = view.findViewById(R.id.verifyEmailArea);
        verifyMobArea = view.findViewById(R.id.verifyMobArea);
        verifyEmailArea = view.findViewById(R.id.verifyEmailArea);
        verifyMobArea = view.findViewById(R.id.verifyMobArea);
        notificationView = view.findViewById(R.id.notificationView);
        paymentView = view.findViewById(R.id.paymentView);
        rewardView = view.findViewById(R.id.rewardView);
        myBookingView = view.findViewById(R.id.myBookingView);
        addMoneyView = view.findViewById(R.id.addMoneyView);
        aboutUsView = view.findViewById(R.id.aboutUsView);
        myWalletView = view.findViewById(R.id.myWalletView);
        statisticsView = view.findViewById(R.id.statisticsView);
        wayBillView = view.findViewById(R.id.wayBillView);
        sendMoneyView = view.findViewById(R.id.sendMoneyView);
        personalDetailsView = view.findViewById(R.id.personalDetailsView);
        changePasswordView = view.findViewById(R.id.personalDetailsView);
        changeCurrencyView = view.findViewById(R.id.changeCurrencyView);
        changeLangView = view.findViewById(R.id.changeLangView);
        termsView = view.findViewById(R.id.termsView);
        livechatView = view.findViewById(R.id.livechatView);
        mySubView = view.findViewById(R.id.mySubView);
        myServiceView = view.findViewById(R.id.myServiceView);
        manageGallleryView = view.findViewById(R.id.manageGallleryView);
        myAvailView = view.findViewById(R.id.myAvailView);
        verifyEmailView = view.findViewById(R.id.verifyEmailView);
        verifyMobView = view.findViewById(R.id.verifyMobView);
        bookingArea = view.findViewById(R.id.bookingArea);
        inviteArea = view.findViewById(R.id.inviteArea);
        topUpArea = view.findViewById(R.id.topUpArea);

        logOutArea = view.findViewById(R.id.logOutArea);

        myPaymentHTxt = view.findViewById(R.id.myPaymentHTxt);
        mybookingHTxt = view.findViewById(R.id.mybookingHTxt);
        addMoneyHTxt = view.findViewById(R.id.addMoneyHTxt);
        sendMoneyHTxt = view.findViewById(R.id.sendMoneyHTxt);
        personalDetailsHTxt = view.findViewById(R.id.personalDetailsHTxt);
        changePasswordHTxt = view.findViewById(R.id.changePasswordHTxt);
        changeCurrencyHTxt = view.findViewById(R.id.changeCurrencyHTxt);
        changeLanguageHTxt = view.findViewById(R.id.changeLanguageHTxt);
        supportHTxt = view.findViewById(R.id.supportHTxt);
        livechatHTxt = view.findViewById(R.id.livechatHTxt);
        contactUsHTxt = view.findViewById(R.id.contactUsHTxt);
        myWalletArea = view.findViewById(R.id.myWalletArea);
        headerwalletArea = view.findViewById(R.id.headerwalletArea);
        emeContactArea = view.findViewById(R.id.emeContactArea);
        bankDetailsArea = view.findViewById(R.id.bankDetailsArea);
        manageDocArea = view.findViewById(R.id.manageDocArea);
        mySubArea = view.findViewById(R.id.mySubArea);
        myServiceArea = view.findViewById(R.id.myServiceArea);
        manageGalleryArea = view.findViewById(R.id.manageGalleryArea);
        myAvailArea = view.findViewById(R.id.myAvailArea);
        statisticsArea = view.findViewById(R.id.statisticsArea);
        userFeedbackArea = view.findViewById(R.id.userFeedbackArea);
        wayBillArea = view.findViewById(R.id.wayBillArea);
        inviteFriendArea = view.findViewById(R.id.inviteFriendArea);
        helpArea = view.findViewById(R.id.helpArea);
        aboutusArea = view.findViewById(R.id.aboutusArea);

        notificationArrow = view.findViewById(R.id.notificationArrow);
        paymentArrow = view.findViewById(R.id.paymentArrow);
        privacyArrow = view.findViewById(R.id.privacyArrow);
        termsArrow = view.findViewById(R.id.termsArrow);
        mywalletArrow = view.findViewById(R.id.mywalletArrow);
        inviteArrow = view.findViewById(R.id.inviteArrow);
        helpArrow = view.findViewById(R.id.helpArrow);
        aboutusArrow = view.findViewById(R.id.aboutusArrow);
        statisticsArrow = view.findViewById(R.id.statisticsArrow);
        mybookingArrow = view.findViewById(R.id.mybookingArrow);
        addMoneyArrow = view.findViewById(R.id.addMoneyArrow);
        sendMoneyArrow = view.findViewById(R.id.sendMoneyArrow);
        personalDetailsArrow = view.findViewById(R.id.personalDetailsArrow);
        changePasswordArrow = view.findViewById(R.id.changePasswordArrow);
        changeCurrencyArrow = view.findViewById(R.id.changeCurrencyArrow);
        changeLangArrow = view.findViewById(R.id.changeLangArrow);
        livechatArrow = view.findViewById(R.id.livechatArrow);
        contactUsArrow = view.findViewById(R.id.contactUsArrow);
        logoutArrow = view.findViewById(R.id.logoutArrow);
        emeContactArrow = view.findViewById(R.id.emeContactArrow);
        bankDetailsArrow = view.findViewById(R.id.bankDetailsArrow);
        manageGalleryArrow = view.findViewById(R.id.manageGalleryArrow);
        mySubArrow = view.findViewById(R.id.mySubArrow);
        myServiceArrow = view.findViewById(R.id.myServiceArrow);
        manageDocArrow = view.findViewById(R.id.manageDocArrow);
        myAvailArrow = view.findViewById(R.id.myAvailArrow);
        userFeedbackArrow = view.findViewById(R.id.userFeedbackArrow);
        wayBillArrow = view.findViewById(R.id.wayBillArrow);
        verifyMobsArrow = view.findViewById(R.id.verifyMobsArrow);
        verifyEmailArrow = view.findViewById(R.id.verifyEmailArrow);


        mywalletHTxt = view.findViewById(R.id.mywalletHTxt);
        inviteHTxt = view.findViewById(R.id.inviteHTxt);
        emeContactHTxt = view.findViewById(R.id.emeContactHTxt);
        bankDetailsHTxt = view.findViewById(R.id.bankDetailsHTxt);
        manageDocHTxt = view.findViewById(R.id.manageDocHTxt);
        mySubHTxt = view.findViewById(R.id.mySubHTxt);
        myServiceHTxt = view.findViewById(R.id.myServiceHTxt);
        manageGalleryHTxt = view.findViewById(R.id.manageGalleryHTxt);
        myAvailHTxt = view.findViewById(R.id.myAvailHTxt);
        statisticsHTxt = view.findViewById(R.id.statisticsHTxt);
        userFeedbackHTxt = view.findViewById(R.id.userFeedbackHTxt);
        wayBillHTxt = view.findViewById(R.id.wayBillHTxt);
        helpHTxt = view.findViewById(R.id.helpHTxt);
        aboutusHTxt = view.findViewById(R.id.aboutusHTxt);
        toolbar_profile = view.findViewById(R.id.toolbar_profile);
        verifyEmailHTxt = view.findViewById(R.id.verifyEmailHTxt);
        verifyMobHTxt = view.findViewById(R.id.verifyMobHTxt);


        notificationArea.setOnClickListener(new setOnClickList());
        paymentMethodArea.setOnClickListener(new setOnClickList());
        privacyArea.setOnClickListener(new setOnClickList());

        myBookingArea.setOnClickListener(new setOnClickList());
        bookingArea.setOnClickListener(new setOnClickList());
        inviteArea.setOnClickListener(new setOnClickList());
        topUpArea.setOnClickListener(new setOnClickList());

        logOutArea.setOnClickListener(new setOnClickList());
        myWalletArea.setOnClickListener(new setOnClickList());
        headerwalletArea.setOnClickListener(new setOnClickList());
        emeContactArea.setOnClickListener(new setOnClickList());
        inviteFriendArea.setOnClickListener(new setOnClickList());
        helpArea.setOnClickListener(new setOnClickList());
        aboutusArea.setOnClickListener(new setOnClickList());

        backImg.setOnClickListener(new setOnClickList());

        addMoneyArea.setOnClickListener(new setOnClickList());
        sendMoneyArea.setOnClickListener(new setOnClickList());
        personalDetailsArea.setOnClickListener(new setOnClickList());
        changesPasswordArea.setOnClickListener(new setOnClickList());
        changesCurrancyArea.setOnClickListener(new setOnClickList());
        changeslanguageArea.setOnClickListener(new setOnClickList());
        termsArea.setOnClickListener(new setOnClickList());
        liveChatArea.setOnClickListener(new setOnClickList());
        contactUsArea.setOnClickListener(new setOnClickList());
        verifyEmailArea.setOnClickListener(new setOnClickList());
        verifyMobArea.setOnClickListener(new setOnClickList());
        editProfileImage.setOnClickListener(new setOnClickList());
        bankDetailsArea.setOnClickListener(new setOnClickList());
        manageDocArea.setOnClickListener(new setOnClickList());
        mySubArea.setOnClickListener(new setOnClickList());
        myServiceArea.setOnClickListener(new setOnClickList());
        manageGalleryArea.setOnClickListener(new setOnClickList());
        myAvailArea.setOnClickListener(new setOnClickList());
        statisticsArea.setOnClickListener(new setOnClickList());
        userFeedbackArea.setOnClickListener(new setOnClickList());
        wayBillArea.setOnClickListener(new setOnClickList());
        verifyEmailArea.setOnClickListener(new setOnClickList());
        verifyMobArea.setOnClickListener(new setOnClickList());


        if (generalFunc.isRTLmode()) {
            backImg.setRotation(0);
            notificationArrow.setRotation(180);
            paymentArrow.setRotation(180);
            privacyArrow.setRotation(180);
            termsArrow.setRotation(180);
            mywalletArrow.setRotation(180);
            inviteArrow.setRotation(180);
            helpArrow.setRotation(180);
            aboutusArrow.setRotation(180);
            mybookingArrow.setRotation(180);
            addMoneyArrow.setRotation(180);
            sendMoneyArrow.setRotation(180);
            personalDetailsArrow.setRotation(180);
            changePasswordArrow.setRotation(180);
            changeCurrencyArrow.setRotation(180);
            changeLangArrow.setRotation(180);
            livechatArrow.setRotation(180);
            contactUsArrow.setRotation(180);
            logoutArrow.setRotation(180);
            emeContactArrow.setRotation(180);
            bankDetailsArrow.setRotation(180);
            manageGalleryArrow.setRotation(180);
            manageDocArrow.setRotation(180);
            mySubArrow.setRotation(180);
            myServiceArrow.setRotation(180);
            myAvailArrow.setRotation(180);
            userFeedbackArrow.setRotation(180);
            statisticsArrow.setRotation(180);
            wayBillArrow.setRotation(180);
            verifyEmailArrow.setRotation(180);
            verifyMobsArrow.setRotation(180);

        }


        NestedScrollView scroller = (NestedScrollView) view.findViewById(R.id.scroll);
        scroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {

                    Log.i(TAG, "Scroll DOWN");

                    if (scrollY > getResources().getDimension(R.dimen._75sdp)) {
                        toolbar_profile.setVisibility(View.VISIBLE);
                    }
                }
                if (scrollY < oldScrollY) {
                    Log.i(TAG, "Scroll UP");
                    if (scrollY < getResources().getDimension(R.dimen._75sdp)) {
                        toolbar_profile.setVisibility(View.GONE);
                    }

                }

                if (scrollY == 0) {
                    Log.i(TAG, "TOP SCROLL");

                }

                if (scrollY == (v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight())) {
                    Log.i(TAG, "BOTTOM SCROLL");
                }
            }
        });

    }


    public void setLabel() {
        walletHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_WALLET_BALANCE"));
        verifyEmailHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_EMAIL_VERIFY"));
        verifyMobHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MOBILE_VERIFY"));


        if (generalFunc.isDeliverOnlyEnabled()) {
            if (app_type.equalsIgnoreCase(Utils.CabGeneralType_UberX) || (app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX) && isUfxServicesEnabled) && !generalFunc.isDeliverOnlyEnabled()) {
                myServiceHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MANANGE_SERVICES"));

            } else {
                myServiceHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MANAGE_VEHICLES"));
            }


        } else {


            if (app_type.equalsIgnoreCase(Utils.CabGeneralType_UberX) || (app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX) && isUfxServicesEnabled) && !generalFunc.isDeliverOnlyEnabled()) {
                myServiceHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MANANGE_SERVICES"));

            } else {
                myServiceHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MANAGE_VEHICLES"));
            }
        }


        if ((app_type.equalsIgnoreCase(Utils.CabGeneralType_UberX) || (app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX) && isUfxServicesEnabled)) && generalFunc.getJsonValueStr("SERVICE_PROVIDER_FLOW", obj_userProfile).equalsIgnoreCase("PROVIDER") && !generalFunc.isDeliverOnlyEnabled()) {
            manageGallleryView.setVisibility(View.VISIBLE);
            manageGalleryArea.setVisibility(View.VISIBLE);
        } else {
            manageGallleryView.setVisibility(View.GONE);
            manageGalleryArea.setVisibility(View.GONE);
        }

        if (app_type.equalsIgnoreCase(Utils.CabGeneralType_UberX) || (app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX) && isUfxServicesEnabled) && !generalFunc.isDeliverOnlyEnabled()) {
            myAvailArea.setVisibility(View.VISIBLE);
            myAvailView.setVisibility(View.VISIBLE);
        } else {
            myAvailView.setVisibility(View.GONE);
            myAvailArea.setVisibility(View.GONE);
        }


        if (app_type.equalsIgnoreCase(Utils.CabGeneralType_Ride) && !generalFunc.isDeliverOnlyEnabled()) {


            userFeedbackHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_RIDER_FEEDBACK"));
        } else if (app_type.equalsIgnoreCase("Delivery")) {

            userFeedbackHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SENDER_fEEDBACK"));
        } else {
            userFeedbackHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_USER_FEEDBACK"));
        }

        if (generalFunc.getJsonValueStr("WAYBILL_ENABLE", obj_userProfile) != null && generalFunc.getJsonValueStr("WAYBILL_ENABLE", obj_userProfile).equalsIgnoreCase("yes")) {
            wayBillArea.setVisibility(View.VISIBLE);
            wayBillView.setVisibility(View.VISIBLE);
        } else {
            wayBillArea.setVisibility(View.GONE);
            wayBillView.setVisibility(View.GONE);
        }

        wayBillHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MENU_WAY_BILL"));
        statisticsHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_STATISTICS"));
        myAvailHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MY_AVAILABILITY"));
        manageGalleryHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MANAGE_GALLARY"));
        mySubHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MY_SUBSCRIPTION"));
        manageDocHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MANAGE_DOCUMENT"));
        bankDetailsHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_BANK_DETAIL"));
        emeContactHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_EMERGENCY_CONTACT"));
        topupTxt.setText(generalFunc.retrieveLangLBl("", "LBL_TOP_UP"));
        headerwalletTxt.setText(generalFunc.retrieveLangLBl("", "LBL_WALLET_TXT"));
        inviteTxt.setText(generalFunc.retrieveLangLBl("", "LBL_INVITE"));
        bookingTxt.setText(generalFunc.retrieveLangLBl("", "LBL_BOOKING"));
        generalSettingHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_GENERAL_SETTING"));
        notificationHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_NOTIFICATIONS"));
        paymentHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PAYMENT_METHOD"));
        privacyHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PRIVACY_POLICY_TEXT"));
        termsHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_TERMS_CONDITION"));
        myPaymentHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PAYMENT"));
        mywalletHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MY_WALLET"));
        inviteHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_INVITE_FRIEND_TXT"));
        // helpHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HELP_CENTER"));
        helpHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_FAQ_TXT"));
        aboutusHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ABOUT_US_TXT"));
        mybookingHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MY_BOOKINGS"));
        addMoneyHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY"));
        sendMoneyHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SEND_MONEY"));
        accountHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ACCOUNT_SETTING"));
        personalDetailsHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PERSONAL_DETAILS"));
        changePasswordHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CHANGE_PASSWORD_TXT"));
        changeCurrencyHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CHANGE_CURRENCY"));
        changeLanguageHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CHANGE_LANGUAGE"));
        supportHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SUPPORT"));
        livechatHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_LIVE_CHAT"));
        contactUsHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CONTACT_US_TXT"));
        logoutTxt.setText(generalFunc.retrieveLangLBl("", "LBL_LOGOUT"));
        otherHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_OTHER_TXT"));


    }


    public void setuserInfo() {

        Log.e("CheckName", "::" + generalFunc.getJsonValueStr("vName", obj_userProfile) + " "
                + generalFunc.getJsonValueStr("vLastName", obj_userProfile));

        userNameTxt.setText(generalFunc.getJsonValueStr("vName", obj_userProfile) + " "
                + generalFunc.getJsonValueStr("vLastName", obj_userProfile));
        userNameTxt_toolbar.setText(generalFunc.getJsonValueStr("vName", obj_userProfile) + " "
                + generalFunc.getJsonValueStr("vLastName", obj_userProfile));
        userEmailTxt.setText(generalFunc.getJsonValueStr("vEmail", obj_userProfile));

        (new AppFunctions(getActContext())).checkProfileImage(userImgView, userProfileJson, "vImage");
        (new AppFunctions(getActContext())).checkProfileImage(userImgView_toolbar, userProfileJson, "vImage");
        walletVxt.setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("user_available_balance", obj_userProfile)));
        if (!generalFunc.getJsonValueStr("eEmailVerified", obj_userProfile).equalsIgnoreCase("YES")) {
            verifyEmailArea.setVisibility(View.VISIBLE);
            verifyEmailView.setVisibility(View.VISIBLE);
        } else {
            verifyEmailArea.setVisibility(View.GONE);
            verifyEmailView.setVisibility(View.GONE);
        }


        if (!generalFunc.getJsonValueStr("ePhoneVerified", obj_userProfile).equalsIgnoreCase("YES")) {
            verifyMobArea.setVisibility(View.VISIBLE);
            verifyMobView.setVisibility(View.VISIBLE);
        } else {
            verifyMobArea.setVisibility(View.GONE);
            verifyMobView.setVisibility(View.GONE);
        }

        boolean isTransferMoneyEnabled = generalFunc.retrieveValue(Utils.ENABLE_GOPAY_KEY).equalsIgnoreCase("Yes");

        if (!isTransferMoneyEnabled) {
            sendMoneyArea.setVisibility(View.GONE);
            sendMoneyView.setVisibility(View.GONE);

        }

        boolean isOnlyCashEnabled = generalFunc.getJsonValue("APP_PAYMENT_MODE", userProfileJson).equalsIgnoreCase("Cash");
        if (isOnlyCashEnabled) {
            addMoneyArea.setVisibility(View.GONE);
            topUpArea.setVisibility(View.GONE);
            addMoneyView.setVisibility(View.GONE);
        }



        if (generalFunc.retrieveValue(Utils.FEMALE_RIDE_REQ_ENABLE).equalsIgnoreCase("yes") && !generalFunc.isDeliverOnlyEnabled()) {
            if (generalFunc.getJsonValueStr("eGender", obj_userProfile).equalsIgnoreCase("feMale")) {
                editProfileImage.setImageResource(R.drawable.ic_settings_new);
            } else {
                if (generalFunc.getJsonValueStr("eGender", obj_userProfile).equals("")) {
                    editProfileImage.setImageResource(R.drawable.ic_settings_new);

                } else {
                    editProfileImage.setImageResource(R.drawable.ic_edit);
                }
            }
        } else {
            editProfileImage.setImageResource(R.drawable.ic_edit);
        }

    }


    public void buildLanguageList() {


        language_list.clear();
        JSONArray languageList_arr = generalFunc.getJsonArray(generalFunc.retrieveValue(Utils.LANGUAGE_LIST_KEY));

        for (int i = 0; i < languageList_arr.length(); i++) {
            JSONObject obj_temp = generalFunc.getJsonObject(languageList_arr, i);


            if ((generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY)).equals(generalFunc.getJsonValueStr("vCode", obj_temp))) {
                selected_language_code = generalFunc.getJsonValueStr("vCode", obj_temp);

                default_selected_language_code = selected_language_code;
                selLanguagePosition = i;
            }

            HashMap<String, String> data = new HashMap<>();
            data.put("vTitle", generalFunc.getJsonValueStr("vTitle", obj_temp));
            data.put("vCode", generalFunc.getJsonValueStr("vCode", obj_temp));


            language_list.add(data);
        }
        if (language_list.size() < 2) {
            changeslanguageArea.setVisibility(View.GONE);
        } else {
            changeslanguageArea.setVisibility(View.VISIBLE);

        }
        if (language_list.size() < 2) {
            changeslanguageArea.setVisibility(View.GONE);
        } else {
            changeslanguageArea.setVisibility(View.VISIBLE);

        }

        buildCurrencyList();

    }


    public void updateProfile() {


        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "updateUserProfileDetail");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("vName", generalFunc.getJsonValue("vName", userProfileJson));
        parameters.put("vLastName", generalFunc.getJsonValue("vLastName", userProfileJson));
        parameters.put("vPhone", generalFunc.getJsonValue("vPhone", userProfileJson));
        parameters.put("vPhoneCode", generalFunc.getJsonValue("vPhoneCode", userProfileJson));
        parameters.put("vCountry", generalFunc.getJsonValue("vCountry", userProfileJson));
        parameters.put("vEmail", generalFunc.getJsonValue("vEmail", userProfileJson));
        parameters.put("CurrencyCode", selected_currency);
        parameters.put("LanguageCode", selected_language_code);
        parameters.put("UserType", Utils.app_type);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {

                    String currentLangCode = generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY);
                    String vCurrencyPassenger = generalFunc.getJsonValue("vCurrencyDriver", userProfileJson);

                    String messgeJson = generalFunc.getJsonValue(Utils.message_str, responseString);
                    generalFunc.storeData(Utils.USER_PROFILE_JSON, messgeJson);
                    responseString = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);




                    if (!currentLangCode.equals(selected_language_code) || !selected_currency.equals(vCurrencyPassenger)) {
                        new SetUserData(responseString, generalFunc, getActContext(), false);

                        GenerateAlertBox alertBox = generalFunc.notifyRestartApp();
                        alertBox.setCancelable(false);
                        alertBox.setBtnClickList(btn_id -> {

                            if (btn_id == 1) {
                                //  generalFunc.restartApp();
                                generalFunc.storeData(Utils.LANGUAGE_CODE_KEY, selected_language_code);
                                generalFunc.storeData(Utils.DEFAULT_CURRENCY_VALUE, selected_currency);
                                changeLanguagedata(selected_language_code);
                            }
                        });
                    }

                } else {
                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                }
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }


    public void buildCurrencyList() {
        currency_list.clear();
        JSONArray currencyList_arr = generalFunc.getJsonArray(generalFunc.retrieveValue(Utils.CURRENCY_LIST_KEY));
        if (currencyList_arr!=null){
            for (int i = 0; i < currencyList_arr.length(); i++) {
                JSONObject obj_temp = generalFunc.getJsonObject(currencyList_arr, i);

                HashMap<String, String> data = new HashMap<>();
                data.put("vName", generalFunc.getJsonValueStr("vName", obj_temp));
                data.put("vSymbol", generalFunc.getJsonValueStr("vSymbol", obj_temp));
                if (!selected_currency.equalsIgnoreCase("") && selected_currency.equalsIgnoreCase(generalFunc.getJsonValueStr("vName", obj_temp))) {
                    selCurrancyPosition = i;
                }
                currency_list.add(data);
            }

            if (currency_list.size() < 2) {
                changeCurrencyView.setVisibility(View.GONE);
                changesCurrancyArea.setVisibility(View.GONE);
            } else {
                changeCurrencyView.setVisibility(View.VISIBLE);
                changesCurrancyArea.setVisibility(View.VISIBLE);

            }
        }else {
            changeCurrencyView.setVisibility(View.GONE);
            changesCurrancyArea.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        updateUserProfileObj();
        Logger.d("Onresume", ":: fragment called" + "::" + generalFunc.getJsonValueStr("user_available_balance", obj_userProfile));


        setuserInfo();
    }


    int selCurrancyPosition = -1;
    int selLanguagePosition = -1;


    private void updateUserProfileObj() {
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        obj_userProfile = generalFunc.getJsonObject(userProfileJson);
        String UFX_SERVICE_AVAILABLE = generalFunc.getJsonValueStr("UFX_SERVICE_AVAILABLE", obj_userProfile);
        app_type = generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile);
        isUfxServicesEnabled = !Utils.checkText(UFX_SERVICE_AVAILABLE) || (UFX_SERVICE_AVAILABLE!=null &&UFX_SERVICE_AVAILABLE.equalsIgnoreCase("Yes"));

        ENABLE_FAVORITE_DRIVER_MODULE_KEY = generalFunc.retrieveValue(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY);
        isDeliverOnlyEnabled = generalFunc.isDeliverOnlyEnabled();
        isAnyDeliverOptionEnabled = generalFunc.isAnyDeliverOptionEnabled();
        SITE_TYPE = generalFunc.getJsonValueStr("SITE_TYPE", obj_userProfile);
        SITE_TYPE_DEMO_MSG = generalFunc.getJsonValueStr("SITE_TYPE_DEMO_MSG", obj_userProfile);
        selected_currency = generalFunc.getJsonValue("vCurrencyDriver", userProfileJson);
        default_selected_currency = selected_currency;
    }


    public void showLanguageList() {


        OpenListView.getInstance(getActContext(), getSelectLangText(), language_list, OpenListView.OpenDirection.CENTER, true, position -> {


            selLanguagePosition = position;
            selected_language_code = language_list.get(selLanguagePosition).get("vCode");
            generalFunc.storeData(Utils.DEFAULT_LANGUAGE_VALUE, language_list.get(selLanguagePosition).get("vTitle"));

            if (generalFunc.getMemberId().equalsIgnoreCase("")) {
                generalFunc.storeData(Utils.LANGUAGE_CODE_KEY, selected_language_code);
                generalFunc.storeData(Utils.DEFAULT_CURRENCY_VALUE, selected_currency);
                changeLanguagedata(selected_language_code);
            } else {
                updateProfile();
            }
        }).show(selLanguagePosition, "vTitle");
    }

    public void showCurrencyList() {

        OpenListView.getInstance(getActContext(), generalFunc.retrieveLangLBl("", "LBL_SELECT_CURRENCY"), currency_list, OpenListView.OpenDirection.CENTER, true, position -> {


            selCurrancyPosition = position;
            selected_currency_symbol = currency_list.get(selCurrancyPosition).get("vSymbol");
            selected_currency = currency_list.get(selCurrancyPosition).get("vName");
            if (generalFunc.getMemberId().equalsIgnoreCase("")) {
                generalFunc.storeData(Utils.LANGUAGE_CODE_KEY, selected_language_code);
                generalFunc.storeData(Utils.DEFAULT_CURRENCY_VALUE, selected_currency);
                changeLanguagedata(selected_language_code);
            } else {
                updateProfile();
            }
        }).show(selCurrancyPosition, "vName");

    }

    public void showPasswordBox() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());


        LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.change_passoword_layout, null);

        final String required_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD");
        final String noWhiteSpace = generalFunc.retrieveLangLBl("Password should not contain whitespace.", "LBL_ERROR_NO_SPACE_IN_PASS");
        final String pass_length = generalFunc.retrieveLangLBl("Password must be", "LBL_ERROR_PASS_LENGTH_PREFIX")
                + " " + Utils.minPasswordLength + " " + generalFunc.retrieveLangLBl("or more character long.", "LBL_ERROR_PASS_LENGTH_SUFFIX");
        final String vPassword = generalFunc.getJsonValueStr("vPassword", obj_userProfile);

        final MaterialEditText previous_passwordBox = (MaterialEditText) dialogView.findViewById(R.id.editBox);
        previous_passwordBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_CURR_PASS_HEADER"));
        previous_passwordBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        previous_passwordBox.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        if (vPassword.equals("")) {
            previous_passwordBox.setVisibility(View.GONE);
        }

        final MaterialEditText newPasswordBox = (MaterialEditText) dialogView.findViewById(R.id.newPasswordBox);

        ImageView cancelImg = (ImageView) dialogView.findViewById(R.id.cancelImg);
        MTextView submitTxt = (MTextView) dialogView.findViewById(R.id.submitTxt);
        MTextView cancelTxt = (MTextView) dialogView.findViewById(R.id.cancelTxt);
        MTextView subTitleTxt = (MTextView) dialogView.findViewById(R.id.subTitleTxt);
        subTitleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CHANGE_PASSWORD_TXT"));


        newPasswordBox.setFloatingLabelText(generalFunc.retrieveLangLBl("", "LBL_UPDATE_PASSWORD_HEADER_TXT"));
        newPasswordBox.setHint(generalFunc.retrieveLangLBl("", "LBL_UPDATE_PASSWORD_HINT_TXT"));
        newPasswordBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        newPasswordBox.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        final MaterialEditText reNewPasswordBox = (MaterialEditText) dialogView.findViewById(R.id.reNewPasswordBox);

        reNewPasswordBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        reNewPasswordBox.setFloatingLabelText(generalFunc.retrieveLangLBl("", "LBL_UPDATE_CONFIRM_PASSWORD_HEADER_TXT"));
        reNewPasswordBox.setHint(generalFunc.retrieveLangLBl("", "LBL_UPDATE_CONFIRM_PASSWORD_HEADER_TXT"));

        reNewPasswordBox.setTransformationMethod(new AsteriskPasswordTransformationMethod());


        builder.setView(dialogView);


        cancelImg.setOnClickListener(v -> alertDialog.dismiss());
        cancelTxt.setOnClickListener(v -> alertDialog.dismiss());
        submitTxt.setOnClickListener(v -> {

            boolean isCurrentPasswordEnter = Utils.checkText(previous_passwordBox) ?
                    (Utils.getText(previous_passwordBox).contains(" ") ? Utils.setErrorFields(previous_passwordBox, noWhiteSpace)
                            : (Utils.getText(previous_passwordBox).length() >= Utils.minPasswordLength ? true : Utils.setErrorFields(previous_passwordBox, pass_length)))
                    : Utils.setErrorFields(previous_passwordBox, required_str);

            boolean isNewPasswordEnter = Utils.checkText(newPasswordBox) ?
                    (Utils.getText(newPasswordBox).contains(" ") ? Utils.setErrorFields(newPasswordBox, noWhiteSpace)
                            : (Utils.getText(newPasswordBox).length() >= Utils.minPasswordLength ? true : Utils.setErrorFields(newPasswordBox, pass_length)))
                    : Utils.setErrorFields(newPasswordBox, required_str);

            boolean isReNewPasswordEnter = Utils.checkText(reNewPasswordBox) ?
                    (Utils.getText(reNewPasswordBox).contains(" ") ? Utils.setErrorFields(reNewPasswordBox, noWhiteSpace)
                            : (Utils.getText(reNewPasswordBox).length() >= Utils.minPasswordLength ? true : Utils.setErrorFields(reNewPasswordBox, pass_length)))
                    : Utils.setErrorFields(reNewPasswordBox, required_str);

            if ((!vPassword.equals("") && isCurrentPasswordEnter == false) || isNewPasswordEnter == false || isReNewPasswordEnter == false) {
                return;
            }

            if (!Utils.getText(newPasswordBox).equals(Utils.getText(reNewPasswordBox))) {
                Utils.setErrorFields(reNewPasswordBox, generalFunc.retrieveLangLBl("", "LBL_VERIFY_PASSWORD_ERROR_TXT"));
                return;
            }

            changePassword(Utils.getText(previous_passwordBox), Utils.getText(newPasswordBox), previous_passwordBox);

        });

        builder.setView(dialogView);
        alertDialog = builder.create();


        if (generalFunc.isRTLmode()) {
            generalFunc.forceRTLIfSupported(alertDialog);
        }

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(getActContext().getResources().getDrawable(R.drawable.all_roundcurve_card));
        alertDialog.show();

//        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                boolean isCurrentPasswordEnter = Utils.checkText(previous_passwordBox) ?
//                        (Utils.getText(previous_passwordBox).contains(" ") ? Utils.setErrorFields(previous_passwordBox, noWhiteSpace)
//                                : (Utils.getText(previous_passwordBox).length() >= Utils.minPasswordLength ? true : Utils.setErrorFields(previous_passwordBox, pass_length)))
//                        : Utils.setErrorFields(previous_passwordBox, required_str);
//
//                boolean isNewPasswordEnter = Utils.checkText(newPasswordBox) ?
//                        (Utils.getText(newPasswordBox).contains(" ") ? Utils.setErrorFields(newPasswordBox, noWhiteSpace)
//                                : (Utils.getText(newPasswordBox).length() >= Utils.minPasswordLength ? true : Utils.setErrorFields(newPasswordBox, pass_length)))
//                        : Utils.setErrorFields(newPasswordBox, required_str);
//
//                boolean isReNewPasswordEnter = Utils.checkText(reNewPasswordBox) ?
//                        (Utils.getText(reNewPasswordBox).contains(" ") ? Utils.setErrorFields(reNewPasswordBox, noWhiteSpace)
//                                : (Utils.getText(reNewPasswordBox).length() >= Utils.minPasswordLength ? true : Utils.setErrorFields(reNewPasswordBox, pass_length)))
//                        : Utils.setErrorFields(reNewPasswordBox, required_str);
//
//                if ((!vPassword.equals("") && isCurrentPasswordEnter == false) || isNewPasswordEnter == false || isReNewPasswordEnter == false) {
//                    return;
//                }
//
//                if (!Utils.getText(newPasswordBox).equals(Utils.getText(reNewPasswordBox))) {
//                    Utils.setErrorFields(reNewPasswordBox, generalFunc.retrieveLangLBl("", "LBL_VERIFY_PASSWORD_ERROR_TXT"));
//                    return;
//                }
//
//                changePassword(Utils.getText(previous_passwordBox), Utils.getText(newPasswordBox), previous_passwordBox);
//            }
//        });
//
//        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.dismiss();
//            }
//        });

    }

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }

            public char charAt(int index) {
                return '*'; // This is the important part
            }

            public int length() {
                return mSource.length(); // Return default
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }

    public void changePassword(String currentPassword, String password, MaterialEditText previous_passwordBox) {

        if (SITE_TYPE.equals("Demo")) {
            generalFunc.showGeneralMessage("", SITE_TYPE_DEMO_MSG);
            return;
        }

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "updatePassword");
        parameters.put("UserID", generalFunc.getMemberId());
        parameters.put("pass", password);
        parameters.put("CurrentPassword", currentPassword);
        parameters.put("UserType", Utils.app_type);

        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {
            JSONObject responseStringObject = generalFunc.getJsonObject(responseString);

            if (responseStringObject != null && !responseStringObject.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseStringObject);

                if (isDataAvail == true) {
                    alertDialog.dismiss();
                    generalFunc.storeData(Utils.USER_PROFILE_JSON, generalFunc.getJsonValueStr(Utils.message_str, responseStringObject));
                    updateUserProfileObj();
                } else {
                    previous_passwordBox.setText("");

                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValueStr(Utils.message_str, responseStringObject)));
                }
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.execute();
    }


    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Utils.hideKeyboard(getActContext());
            Bundle bn = new Bundle();
            switch (view.getId()) {
                case R.id.backImg:
                    //onBackPressed();
                    break;
                case R.id.personalDetailsArea:

                    obj_userProfile = generalFunc.getJsonObject(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));

                    if (generalFunc.retrieveValue(Utils.FEMALE_RIDE_REQ_ENABLE).equalsIgnoreCase("yes") && !generalFunc.isDeliverOnlyEnabled()) {

                        if (generalFunc.getJsonValueStr("eGender", obj_userProfile).equals("")) {
                            genderDailog();

                        } else {
                            new StartActProcess(getActContext()).startActForResult(MyProfileActivity.class, bn, Utils.MY_PROFILE_REQ_CODE);
                        }
                    } else {
                        new StartActProcess(getActContext()).startActForResult(MyProfileActivity.class, bn, Utils.MY_PROFILE_REQ_CODE);
                    }

                    break;
                case R.id.editProfileImage:


                    obj_userProfile = generalFunc.getJsonObject(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));

                    if (generalFunc.retrieveValue(Utils.FEMALE_RIDE_REQ_ENABLE).equalsIgnoreCase("yes") && !generalFunc.isDeliverOnlyEnabled()) {
                        if (generalFunc.getJsonValueStr("eGender", obj_userProfile).equalsIgnoreCase("feMale")) {
                            new StartActProcess(getActContext()).startAct(PrefranceActivity.class);
                        } else {
                            if (generalFunc.getJsonValueStr("eGender", obj_userProfile).equals("")) {
                                genderDailog();

                            } else {
                                new StartActProcess(getActContext()).startActForResult(MyProfileActivity.class, bn, Utils.MY_PROFILE_REQ_CODE);
                            }
                        }
                    } else {
                        new StartActProcess(getActContext()).startActForResult(MyProfileActivity.class, bn, Utils.MY_PROFILE_REQ_CODE);
                    }


                    break;
                case R.id.bookingArea:
                case R.id.myBookingArea:


//                    new StartActProcess(getActContext()).startActForResult(HistoryActivity.class, bn, MY_BOOKING_REQ_CODE);
                    new StartActProcess(getActContext()).startActForResult(BookingsActivity.class, bn, MY_BOOKING_REQ_CODE);
                    break;


                case R.id.notificationArea:
                    new StartActProcess(getActContext()).startAct(NotificationActivity.class);
                    break;


                case R.id.paymentMethodArea:
                    bn.putBoolean("fromcabselection", false);
                    new StartActProcess(getActContext()).startActForResult(CardPaymentActivity.class, bn, Utils.CARD_PAYMENT_REQ_CODE);
                    break;
                case R.id.privacyArea:
                    bn.putString("staticpage", "33");
                    new StartActProcess(getActContext()).startActWithData(StaticPageActivity.class, bn);
                    break;


                case R.id.changesPasswordArea:
                    showPasswordBox();
                    break;
                case R.id.changesCurrancyArea:
                    showCurrencyList();
                    break;
                case R.id.changeslanguageArea:
                    showLanguageList();
                    break;
                case R.id.termsArea:
                    bn.putString("staticpage", "4");
                    new StartActProcess(getActContext()).startActWithData(StaticPageActivity.class, bn);
                    break;


                case R.id.manageGalleryArea:
                    new StartActProcess(getActContext()).startAct(MyGalleryActivity.class);
                    break;


                case R.id.headerwalletArea:
                case R.id.myWalletArea:

                    new StartActProcess(getActContext()).startActForResult(MyWalletActivity.class, bn, Utils.MY_PROFILE_REQ_CODE);
                    break;

                case R.id.topUpArea:
                case R.id.addMoneyArea:
                    bn.putBoolean("isAddMoney", true);
                    new StartActProcess(getActContext()).startActForResult(MyWalletActivity.class, bn, Utils.MY_PROFILE_REQ_CODE);
                    break;
                case R.id.sendMoneyArea:
                    bn.putBoolean("isSendMoney", true);
                    new StartActProcess(getActContext()).startActForResult(MyWalletActivity.class, bn, Utils.MY_PROFILE_REQ_CODE);
                    break;
                case R.id.inviteArea:
                case R.id.inviteFriendArea:
                    new StartActProcess(getActContext()).startActWithData(InviteFriendsActivity.class, bn);

                    break;
                case R.id.helpArea:
                    new StartActProcess(getActContext()).startAct(HelpActivity.class);
                    break;
                case R.id.liveChatArea:
                    startChatActivity();
                    break;
                case R.id.aboutusArea:
                    bn.putString("staticpage", "1");
                    new StartActProcess(getActContext()).startActWithData(StaticPageActivity.class, bn);
                    break;
                case R.id.contactUsArea:
                    new StartActProcess(getActContext()).startAct(ContactUsActivity.class);
                    break;

                case R.id.emeContactArea:
                    new StartActProcess(getActContext()).startAct(EmergencyContactActivity.class);
                    break;
                case R.id.bankDetailsArea:
                    new StartActProcess(getActContext()).startActWithData(BankDetailActivity.class, bn);
                    break;

                case R.id.mySubArea:
                    new StartActProcess(getActContext()).startAct(SubscriptionActivity.class);
                    break;

                case R.id.myServiceArea:
                    bn.putString("iDriverVehicleId", generalFunc.getJsonValueStr("iDriverVehicleId", obj_userProfile));
                    bn.putString("app_type", app_type);
                    if (getActContext() instanceof LiveTaskListActivity) {
                        bn.putString("isDriverOnline", "true");
                    }

                    if (generalFunc.isDeliverOnlyEnabled()) {
                        new StartActProcess(getActContext()).startActWithData(ManageVehiclesActivity.class, bn);
                    } else {
                        if (app_type.equalsIgnoreCase(Utils.CabGeneralType_UberX) || ((app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX) && isUfxServicesEnabled) && generalFunc.getJsonValueStr("eShowVehicles", obj_userProfile).equalsIgnoreCase("No"))) {
                            bn.putString("UBERX_PARENT_CAT_ID", generalFunc.getJsonValueStr("UBERX_PARENT_CAT_ID", obj_userProfile));
                            (new StartActProcess(getActContext())).startActWithData(UfxCategoryActivity.class, bn);
                        } else if ((app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX) && isUfxServicesEnabled) && generalFunc.getJsonValueStr("eShowVehicles", obj_userProfile).equalsIgnoreCase("Yes")) {
                            bn.putString("apptype", app_type);
                            bn.putString("selView", "vehicle");
                            bn.putInt("totalVehicles", 1);
                            bn.putString("UBERX_PARENT_CAT_ID", app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX) ? generalFunc.getJsonValueStr("UBERX_PARENT_CAT_ID", obj_userProfile) : "");
                            new StartActProcess(getActContext()).startActWithData(UploadDocTypeWiseActivity.class, bn);

                        } else {
                            new StartActProcess(getActContext()).startActWithData(ManageVehiclesActivity.class, bn);
                        }
                    }

                    break;

                case R.id.manageDocArea:
                    bn.putString("PAGE_TYPE", "Driver");
                    bn.putString("iDriverVehicleId", "");
                    bn.putString("doc_file", "");
                    bn.putString("iDriverVehicleId", "");
                    bn.putString("seltype", app_type);
                    new StartActProcess(getActContext()).startActWithData(ListOfDocumentActivity.class, bn);
                    break;
                case R.id.userFeedbackArea:
                    new StartActProcess(getActContext()).startActWithData(DriverFeedbackActivity.class, bn);
                    break;

                case R.id.myAvailArea:
                    new StartActProcess(getActContext()).startAct(SetAvailabilityActivity.class);
                    break;
                case R.id.statisticsArea:
                    new StartActProcess(getActContext()).startActWithData(StatisticsActivity.class, bn);
                    break;
                case R.id.wayBillArea:

                    JSONObject last_trip_data = generalFunc.getJsonObject("TripDetails", obj_userProfile);
                    if (generalFunc.getJsonValueStr("eSystem", last_trip_data).equalsIgnoreCase(Utils.eSystem_Type) || generalFunc.isDeliverOnlyEnabled()) {
                        bn.putString("eSystem", "yes");
                    }
                    new StartActProcess(getActContext()).startActWithData(WayBillActivity.class, bn);
                    break;
                case R.id.logOutArea:

                    final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                    generateAlert.setCancelable(false);
                    generateAlert.setBtnClickList(btn_id -> {
                        if (btn_id == 0) {
                            generateAlert.closeAlertBox();
                        } else {
                            if (internetConnection.isNetworkConnected()) {
                                MyApp.getInstance().logOutFromDevice(false);
                            } else {
                                generalFunc.showMessage(logOutArea, generalFunc.retrieveLangLBl("", "LBL_NO_INTERNET_TXT"));
                            }
                        }

                    });
                    generateAlert.setContentMessage(generalFunc.retrieveLangLBl("Logout", "LBL_LOGOUT"), generalFunc.retrieveLangLBl("Are you sure you want to logout?", "LBL_WANT_LOGOUT_APP_TXT"));
                    generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_YES"));
                    generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_NO"));
                    generateAlert.showAlertBox();
                    break;


                case R.id.verifyEmailArea:
                    bn.putString("msg", "DO_EMAIL_VERIFY");
                    new StartActProcess(getActContext()).startActForResult(VerifyInfoActivity.class, bn, Utils.VERIFY_MOBILE_REQ_CODE);
                    break;
                case R.id.verifyMobArea:
                    bn.putString("msg", "DO_PHONE_VERIFY");
                    new StartActProcess(getActContext()).startActForResult(VerifyInfoActivity.class, bn, Utils.VERIFY_MOBILE_REQ_CODE);
                    break;


            }
        }
    }

    public void callgederApi(String egender) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "updateUserGender");
        parameters.put("UserType", Utils.userType);
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("eGender", egender);


        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {
            JSONObject responseStringObject = generalFunc.getJsonObject(responseString);

            boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseStringObject);

            String message = generalFunc.getJsonValueStr(Utils.message_str, responseStringObject);
            if (isDataAvail) {
                Bundle bn = new Bundle();
                generalFunc.storeData(Utils.USER_PROFILE_JSON, message);
                obj_userProfile = generalFunc.getJsonObject(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));
                new StartActProcess(getActContext()).startActForResult(MyProfileActivity.class, bn, Utils.MY_PROFILE_REQ_CODE);
            }
        });
        exeWebServer.execute();
    }

    public void genderDailog() {


        final Dialog builder = new Dialog(getActContext(), R.style.Theme_Dialog);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        builder.setContentView(R.layout.gender_view);
        builder.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

//        LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View dialogView = inflater.inflate(R.layout.gender_view, null);

        final MTextView genderTitleTxt = (MTextView) builder.findViewById(R.id.genderTitleTxt);
        final MTextView maleTxt = (MTextView) builder.findViewById(R.id.maleTxt);
        final MTextView femaleTxt = (MTextView) builder.findViewById(R.id.femaleTxt);
        final ImageView gendercancel = (ImageView) builder.findViewById(R.id.gendercancel);
        final ImageView gendermale = (ImageView) builder.findViewById(R.id.gendermale);
        final ImageView genderfemale = (ImageView) builder.findViewById(R.id.genderfemale);
        final LinearLayout male_area = (LinearLayout) builder.findViewById(R.id.male_area);
        final LinearLayout female_area = (LinearLayout) builder.findViewById(R.id.female_area);

        genderTitleTxt.setText(generalFunc.retrieveLangLBl("Select your gender to continue", "LBL_SELECT_GENDER"));
        maleTxt.setText(generalFunc.retrieveLangLBl("Male", "LBL_MALE_TXT"));
        femaleTxt.setText(generalFunc.retrieveLangLBl("FeMale", "LBL_FEMALE_TXT"));

        gendercancel.setOnClickListener(v -> builder.dismiss());

        male_area.setOnClickListener(v -> {
            callgederApi("Male");
            builder.dismiss();

        });
        female_area.setOnClickListener(v -> {
            callgederApi("Female");
            builder.dismiss();

        });

        builder.show();

    }


    public String getSelectLangText() {
        return ("" + generalFunc.retrieveLangLBl("Select", "LBL_SELECT_LANGUAGE_HINT_TXT"));
    }

    public void changeLanguagedata(String langcode) {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "changelanguagelabel");
        parameters.put("vLang", langcode);
        ExecuteWebServerUrl exeWebServer = new ExecuteWebServerUrl(getActContext(), parameters);
        exeWebServer.setLoaderConfig(getActContext(), true, generalFunc);
        exeWebServer.setDataResponseListener(responseString -> {
            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {


                    generalFunc.storeData(Utils.languageLabelsKey, generalFunc.getJsonValue(Utils.message_str, responseString));
                    generalFunc.storeData(Utils.LANGUAGE_IS_RTL_KEY, generalFunc.getJsonValue("eType", responseString));
                    generalFunc.storeData(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY, generalFunc.getJsonValue("vGMapLangCode", responseString));
                    GeneralFunctions.clearAndResetLanguageLabelsData(MyApp.getInstance().getApplicationContext());
                    new Handler().postDelayed(() -> generalFunc.restartApp(), 100);

                }
            }
        });
        exeWebServer.execute();
    }

    private void startChatActivity() {

        String vName = generalFunc.getJsonValue("vName", userProfileJson);
        String vLastName = generalFunc.getJsonValue("vLastName", userProfileJson);

        String driverName = vName + " " + vLastName;
        String driverEmail = generalFunc.getJsonValue("vEmail", userProfileJson);

        Utils.LIVE_CHAT_LICENCE_NUMBER = generalFunc.getJsonValue("LIVE_CHAT_LICENCE_NUMBER", userProfileJson);
        HashMap<String, String> map = new HashMap<>();
        map.put("FNAME", vName);
        map.put("LNAME", vLastName);
        map.put("EMAIL", driverEmail);
        map.put("USERTYPE", Utils.userType);

        Intent intent = new Intent(getActivity(), ChatWindowActivity.class);
        intent.putExtra(ChatWindowActivity.KEY_LICENCE_NUMBER, Utils.LIVE_CHAT_LICENCE_NUMBER);
        intent.putExtra(ChatWindowActivity.KEY_VISITOR_NAME, driverName);
        intent.putExtra(ChatWindowActivity.KEY_VISITOR_EMAIL, driverEmail);
        intent.putExtra(ChatWindowActivity.KEY_GROUP_ID, Utils.userType + "_" + generalFunc.getMemberId());

        intent.putExtra("myParam", map);
        startActivity(intent);
    }


    public Context getActContext() {
        return getActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateUserProfileObj();
        setuserInfo();
    }
}
