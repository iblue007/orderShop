package com.xjt.ordershop.aop.checkLogin;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.xjt.ordershop.ui.LoginMainActivity;
import com.xjt.ordershop.util.BaseConfigPreferences;
import com.xjt.ordershop.util.Global;
import com.xjt.ordershop.util.LogUtils;
import com.xjt.ordershop.util.MessageUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Create by xuqunxing on  2020/2/29
 */
@Aspect
public class CheckLoginAop {

    @Pointcut("execution(@com.xjt.ordershop.aop.checkLogin.CheckLoginImpl * *(..))")//方法切入点
    public void methodAnnotated() {
    }

    @Around("methodAnnotated()")//在连接点进行方法替换
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Context context = null;
        final Object object = joinPoint.getThis();
        //    LogUtils.e("======", "======" + object.toString());
        if (object instanceof Context) {
            context = (Context) object;
        } else if (object instanceof Fragment) {
            context = ((Fragment) object).getActivity();
        }
        if (context == null) {
            context = Global.getContext();
        }

        if (TextUtils.isEmpty(BaseConfigPreferences.getInstance(context).getLoginAccount())) {
          //  MessageUtils.show(Global.getContext(), "账号未登录");
            context.startActivity(new Intent(context, LoginMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            LogUtils.e("======","======checkLOgin1111");
            return;
        }
        LogUtils.e("======","======checkLOgin2222");
        joinPoint.proceed();//执行原方法
    }

}
