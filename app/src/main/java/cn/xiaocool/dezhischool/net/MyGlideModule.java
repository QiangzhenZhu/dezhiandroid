package cn.xiaocool.dezhischool.net;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import java.lang.annotation.Annotation;

/**
 * Created by 10835 on 2018/2/27.
 */

public class MyGlideModule extends AppGlideModule {

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
