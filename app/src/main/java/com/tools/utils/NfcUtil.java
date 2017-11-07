package com.tools.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.MifareClassic;
import android.os.Vibrator;

/**
 * Created by milo on 2017/11/3.
 */

public class NfcUtil {
    // Hex help
    private final static byte[] HEX_CHAR_TABLE = {(byte) '0', (byte) '1',
            (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6',
            (byte) '7', (byte) '8', (byte) '9', (byte) 'A', (byte) 'B',
            (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F'};

    private Context mContext;
    private Vibrator vibrator;//震动
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;


    public NfcUtil(Context context) {
        mContext = context;
//        openNfc();
//        closeNfc();
    }

    private void closeNfc() {
        //需要在onPause中配置
//        if (checkNfcAble()) {
//            mAdapter.disableForegroundDispatch(mContext);
//        }
    }

    private void openNfc() {
        //需要在onResume中配置
//        if (checkNfcAble()) {
//            mAdapter.enableForegroundDispatch(mContext, mPendingIntent, mFilters, mTechLists);
//        }
    }

    private boolean checkNfcAble() {
        boolean isAble = false;
        if (mAdapter != null && mAdapter.isEnabled()) {
            isAble = true;
        }
        return isAble;
    }

    private void initNfc() {
        // 获取震动服务
        vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        // 获取系统NFC
        mAdapter = NfcAdapter.getDefaultAdapter(mContext);
        mPendingIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mFilters = new IntentFilter[]{ndef};
        mTechLists = new String[][]{new String[]{MifareClassic.class.getName()}, new String[]{android.nfc.tech.NfcV.class.getName()}};
    }


    /**
     * convert a byte arrary to hex string
     *
     * @param raw byte arrary
     * @param len lenght of the arrary.
     * @return hex string.
     */
    public static String getHexString(byte[] raw, int len) {
        byte[] hex = new byte[2 * len];
        int index = 0;
        int pos = 0;

        for (byte b : raw) {
            if (pos >= len)
                break;
            pos++;
            int v = b & 0xFF;
            hex[index++] = HEX_CHAR_TABLE[v >>> 4];
            hex[index++] = HEX_CHAR_TABLE[v & 0xF];
        }
        return new String(hex);
    }

    public static String getUid(Intent intent) {
        byte[] myNFCID = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);

        if (myNFCID != null && myNFCID.length > 0) {
            return getHexString(myNFCID, myNFCID.length);
        }
        return "";
    }

}
