package cn.xiaocool.dezhischool.utils;


import cn.xiaocool.dezhischool.callback.VersionUpdateImpl;

public class VersionUpdate {

    /**
     * 请求服务器，检查版本是否可以更新
     *
     * @param versionUpdate
     */
     public static void checkVersion(final VersionUpdateImpl versionUpdate,String apkurl) {
         //从网络请求获取到的APK下载路径，此处是随便找的链接
         versionUpdate.bindService(apkurl);
     }
}
