package com.xjt.ordershop.base.basehttp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseIntArray;

import com.xjt.ordershop.R;


public class ResultCodeMap {

	public static final int SERVER_RESPONSE_CODE_SUCCESS = 0;
	public static final int SERVER_RESPONSE_CODE_NOIMP = -1;
	public static final int SERVER_RESPONSE_CODE_1 = 1;
	public static final int SERVER_RESPONSE_CODE_2 = 2;
	public static final int SERVER_RESPONSE_CODE_3 = 3;
	public static final int SERVER_RESPONSE_CODE_4 = 4;
	public static final int SERVER_RESPONSE_CODE_5 = 5;
	public static final int SERVER_RESPONSE_CODE_6 = 6;
	public static final int SERVER_RESPONSE_CODE_7 = 7;
	public static final int SERVER_RESPONSE_CODE_8 = 8;
	public static final int SERVER_RESPONSE_CODE_997 = 997;
	public static final int SERVER_RESPONSE_CODE_998 = 998;
	public static final int SERVER_RESPONSE_CODE_999 = 999;
	
	public static final int SERVER_RESPONSE_CODE_8800 = 8800;

	public static final int RESULT_CODE_NOTHING = -10;
	public static final int RESULT_CODE_EXCEPTION = -11;
	public static final int RESULT_CODE_VERIFY_FAILED = -12;
	public static final int RESULT_CODE_FILE_NOT_FOUND = -13;

	@SuppressLint("UseSparseArrays")
	private static SparseIntArray resultCodeMap = new SparseIntArray();
	static {
		resultCodeMap.put(SERVER_RESPONSE_CODE_SUCCESS, R.string.server_response_dict_success);
		resultCodeMap.put(SERVER_RESPONSE_CODE_NOIMP, R.string.server_response_dict_no_imp);
		resultCodeMap.put(SERVER_RESPONSE_CODE_1, R.string.server_response_dict_no_login);
		resultCodeMap.put(SERVER_RESPONSE_CODE_2, R.string.server_response_dict_invalidate_code);
		resultCodeMap.put(SERVER_RESPONSE_CODE_3, R.string.server_response_dict_params_error);
		resultCodeMap.put(SERVER_RESPONSE_CODE_4, R.string.server_response_dict_params_illegal);
		resultCodeMap.put(SERVER_RESPONSE_CODE_5, R.string.server_response_dict_verify_error);
		resultCodeMap.put(SERVER_RESPONSE_CODE_6, R.string.server_response_dict_encript_error);
		resultCodeMap.put(SERVER_RESPONSE_CODE_7, R.string.server_response_dict_decript_error);
		resultCodeMap.put(SERVER_RESPONSE_CODE_8, R.string.server_response_dict_no_login);
		
		resultCodeMap.put(SERVER_RESPONSE_CODE_997, R.string.server_response_dict_body_too_long);
		resultCodeMap.put(SERVER_RESPONSE_CODE_998, R.string.server_response_dict_server_maintain);
		resultCodeMap.put(SERVER_RESPONSE_CODE_999, R.string.server_response_dict_inner_error);
		
		resultCodeMap.put(SERVER_RESPONSE_CODE_8800, R.string.server_response_dict_client_data_decode_error);

		resultCodeMap.put(RESULT_CODE_EXCEPTION, R.string.server_response_dict_data_error);
		resultCodeMap.put(RESULT_CODE_NOTHING, R.string.server_response_dict_data_null);
	}
	
	public static String getCodeDesc(Context ctx, int resultCode){
		int resId = resultCodeMap.get(resultCode, 0);
		if (resId == 0)
			resId = R.string.server_response_dict_unknown_error;
		return ctx.getString(resId);
	}
}
