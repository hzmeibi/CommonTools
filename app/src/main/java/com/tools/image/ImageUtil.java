package com.tools.image;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.GifTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tools.BuildConfig;
import com.tools.R;

import java.io.File;

/**
 * 图片加载工具
 * 支持加载图片、Gif、Bitmap
 */
public class ImageUtil {
    private static Integer image_load = R.anim.image_load;//加载动画

    /**
     * DecodeFormat:
     * PREFER_ARGB_8888 高质量
     * PREFER_RGB_565 低质量
     */
    private static DecodeFormat decodeFormat = DecodeFormat.PREFER_ARGB_8888;//默认图片质量 - 高质量
    /**
     * DiskCacheStrategy:
     * ALl 保存原图到缓存和转换后保存到缓存
     * NONE 不缓存
     * SOURCE 仅保存原图到缓存
     * RESULT 转换后保存到缓存
     */
    private static DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.SOURCE;//默认图片缓存策略


    /**
     * 加载图片
     *
     * @param object    加载对象
     * @param imageView 显示控件
     * @param failedId  占位图
     */
    public static void loadImage(Object object, ImageView imageView, int failedId) {
        load(imageView.getContext(), object, imageView, 0, 0, failedId, failedId, image_load, null, decodeFormat, null, diskCacheStrategy);
    }

    /**
     * 加载图片
     *
     * @param object    加载对象
     * @param imageView 显示控件
     * @param failedId  占位图
     */
    public static void loadImage(Object object, ImageView imageView, int failedId, int loadingId) {
        load(imageView.getContext(), object, imageView, 0, 0, loadingId, failedId, image_load, null, decodeFormat, null, diskCacheStrategy);
    }

    /**
     * 加载图片
     *
     * @param object         加载对象
     * @param imageView      显示控件
     * @param failedId       占位图
     * @param transformation 转换器  GlideCircleTransform-圆形  GlideRoundTransform-圆角
     * @param width          width
     * @param height         height
     */
    public static void loadImage(Object object, ImageView imageView, int failedId, Transformation transformation, int width, int height) {
        load(imageView.getContext(), object, imageView, width, height, failedId, failedId, image_load, false, decodeFormat, transformation, diskCacheStrategy);
    }

    /**
     * 加载图片
     *
     * @param object         加载对象
     * @param imageView      显示控件
     * @param failedId       占位图
     * @param transformation 转换器  GlideCircleTransform-圆形  GlideRoundTransform-圆角
     */
    public static void loadImage(Object object, ImageView imageView, int failedId, Transformation transformation) {
        load(imageView.getContext(), object, imageView, 0, 0, failedId, failedId, image_load, false, decodeFormat, transformation, diskCacheStrategy);
    }

    /**
     * 加载图片
     *
     * @param object    加载对象
     * @param imageView 显示控件
     * @param failedId  占位图
     * @param width     宽
     * @param height    高
     */
    public static void loadImage(Object object, ImageView imageView, int failedId, int width, int height) {
        load(imageView.getContext(), object, imageView, width, height, failedId, failedId, image_load, null, decodeFormat, null, diskCacheStrategy);
    }

    /**
     * 加载图片
     *
     * @param object            加载对象
     * @param imageView         显示控件
     * @param failedId          占位图
     * @param decodeFormat      图片质量
     * @param diskCacheStrategy 硬盘缓存策略
     */
    public static void loadImage(Object object, ImageView imageView, int failedId, DecodeFormat decodeFormat, DiskCacheStrategy diskCacheStrategy) {
        load(imageView.getContext(), object, imageView, 0, 0, failedId, failedId, image_load, null, decodeFormat, null, diskCacheStrategy);
    }


    /**
     * 加载图片
     *
     * @param object            加载对象
     * @param imageView         显示控件
     * @param failedId          占位图
     * @param width             重设宽度
     * @param height            重设高度
     * @param decodeFormat      图片质量
     * @param diskCacheStrategy 硬盘缓存策略
     */
    public static void loadImage(Object object, ImageView imageView, int failedId, int width, int height, DecodeFormat decodeFormat, DiskCacheStrategy diskCacheStrategy) {
        load(imageView.getContext(), object, imageView, width, height, failedId, failedId, image_load, null, decodeFormat, null, diskCacheStrategy);
    }


    /**
     * 加载Gif
     *
     * @param object            加载对象
     * @param imageView         显示控件
     * @param failedId          占位图
     * @param width             重设宽度
     * @param height            重设高度
     * @param decodeFormat      图片质量
     * @param diskCacheStrategy 硬盘缓存策略
     */
    public static void loadGif(Object object, ImageView imageView, int failedId, int width, int height, DecodeFormat decodeFormat, DiskCacheStrategy diskCacheStrategy) {
        load(imageView.getContext(), object, imageView, width, height, failedId, failedId, image_load, true, decodeFormat, null, diskCacheStrategy);
    }

    /**
     * 加载图片
     *
     * @param object            加载对象
     * @param imageView         显示控件
     * @param failedId          占位图
     * @param width             重设宽度
     * @param height            重设高度
     * @param decodeFormat      图片质量
     * @param diskCacheStrategy 硬盘缓存策略
     */
    public static void loadBitmap(Object object, ImageView imageView, int failedId, int width, int height, DecodeFormat decodeFormat, DiskCacheStrategy diskCacheStrategy) {
        load(imageView.getContext(), object, imageView, width, height, failedId, failedId, image_load, false, decodeFormat, null, diskCacheStrategy);
    }


    /**
     * 加载图片（转换， 圆角、圆形）
     *
     * @param object            加载对象
     * @param imageView         显示控件
     * @param failedId          占位图
     * @param width             重设宽度
     * @param height            重设高度
     * @param isGif             是否是Gif
     * @param decodeFormat      图片质量
     * @param transformation    图片转换
     * @param diskCacheStrategy 硬盘缓存策略
     */
    public static void loadCircleImage(Object object, ImageView imageView, int failedId, int width, int height, Boolean isGif, DecodeFormat decodeFormat, Transformation transformation, DiskCacheStrategy diskCacheStrategy) {
        load(imageView.getContext(), object, imageView, width, height, failedId, failedId, image_load, isGif, decodeFormat, transformation, diskCacheStrategy);
    }

    /**
     * 加载图片
     *
     * @param context           上下文
     * @param loadObj           加载对象
     * @param imageView         图片
     * @param overrideW         重设宽度
     * @param overrideH         重设高度
     * @param holderId          加载中图片Id
     * @param errorId           错误图片Id
     * @param animId            动画Id
     * @param asGif             是否指定图片为Gif类型，null不指定，true指定为Gif，false指定为Bitmap
     * @param format            图片质量，只有设置图片为bitmap类型时才有效
     * @param transformation    图片转换
     * @param diskcacheStrategy 硬盘缓存策略
     */
    private static void load(Context context, Object loadObj, ImageView imageView, int overrideW, int overrideH, int holderId, int errorId, int animId, Boolean asGif, DecodeFormat format, Transformation transformation, DiskCacheStrategy diskcacheStrategy) {
        final RequestManager manager = Glide.with(context);

        DrawableTypeRequest request = null;
        if (loadObj instanceof Integer) {
            request = manager.load((Integer) loadObj);
        } else if (loadObj instanceof String) {
            request = manager.load((String) loadObj);
        } else if (loadObj instanceof Uri) {
            request = manager.load((Uri) loadObj);
        } else if (loadObj instanceof File) {
            request = manager.load((File) loadObj);
        }

        if (request == null) {
            return;
        }

        if (asGif != null) {
            if (asGif) {
                final GifTypeRequest gifTypeRequest = request.asGif();
                load(gifTypeRequest, imageView, overrideW, overrideH, holderId, errorId, animId, true, format, transformation, diskcacheStrategy);
            } else {
                final BitmapTypeRequest bitmapTypeRequest = request.asBitmap();
                load(bitmapTypeRequest, imageView, overrideW, overrideH, holderId, errorId, animId, false, format, transformation, diskcacheStrategy);
            }
        } else {
            load(request, imageView, overrideW, overrideH, holderId, errorId, animId, null, format, transformation, diskcacheStrategy);
        }
    }

    /**
     * 因为是链式调用，需要RequestBuilder层层调用才能全部生效，所以这里需要特殊处理
     *
     * @param request           请求
     * @param imageView         图片
     * @param overrideW         重设宽度
     * @param overrideH         重设高度
     * @param holderId          加载中图片Id
     * @param errorId           错误图片Id
     * @param animId            动画Id
     * @param asGif             是否指定图片为Gif类型，null不指定，true指定为Gif，false指定为Bitmap
     * @param format            图片质量，只有设置图片为bitmap类型时才有效
     * @param transformation    图片转换
     * @param diskcacheStrategy 硬盘缓存策略
     */
    @SuppressWarnings("unchecked")
    private static void load(GenericRequestBuilder request, ImageView imageView, int overrideW, int overrideH, int holderId, int errorId, int animId, Boolean asGif, DecodeFormat format, Transformation transformation, DiskCacheStrategy diskcacheStrategy) {
        // 通过builder一步一步构建，最后调用into才能设置生效；如果只是request调into不行
        GenericRequestBuilder requestBuilder = null;
        if (holderId != 0) {
            requestBuilder = request.placeholder(holderId);
        }
        if (errorId != 0) {
            if (requestBuilder != null) {
                requestBuilder.error(errorId);
            } else {
                requestBuilder = request.error(errorId);
            }
        }
        if (transformation != null) {
            if (requestBuilder != null) {
                requestBuilder.transform(transformation);
            } else {
                requestBuilder = request.transform(transformation);
            }
        }
        if (animId != 0) {
            if (requestBuilder != null) {
                requestBuilder.animate(animId);
            } else {
                requestBuilder = request.animate(animId);
            }
        }
        if (diskcacheStrategy != null) {
            if (requestBuilder != null) {
                requestBuilder.diskCacheStrategy(diskcacheStrategy);
            } else {
                requestBuilder = request.diskCacheStrategy(diskcacheStrategy);
            }
        }
        if (overrideW != 0 && overrideH != 0) {
            if (requestBuilder != null) {
                requestBuilder.override(overrideW, overrideH);
            } else {
                requestBuilder = request.override(overrideW, overrideH);
            }
        }
        if (asGif != null && !asGif) {
            if (requestBuilder != null && requestBuilder instanceof BitmapRequestBuilder) {
                // bitmap格式的特殊处理图片质量
                final BitmapRequestBuilder bitmapRequestBuilder = ((BitmapRequestBuilder) requestBuilder).format(format);
                bitmapRequestBuilder.into(imageView);
                return;
            } else if (requestBuilder == null && request instanceof BitmapTypeRequest) {
                // bitmap格式的特殊处理图片质量
                final BitmapRequestBuilder bitmapRequestBuilder = ((BitmapTypeRequest) request).format(format);
                bitmapRequestBuilder.into(imageView);
                return;
            }
        }
        if (requestBuilder != null) {
            if (BuildConfig.APP_DEBUG) {
                requestBuilder.listener(new RequestListener() {
                    @Override
                    public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                        Log.e("TAG", "ImageUtil-189行-onException(): " + e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                });
            }
            requestBuilder.into(imageView);
        } else {
            request.into(imageView);
        }
    }


    /**
     * 获取文件url
     *
     * @param path
     * @return
     */
    public String getFileUrl(String path) {
        return "file://" + path;
    }

    /**
     * 获取asset路径
     *
     * @param assetFileName
     * @return
     */
    public String getAssetUrl(String assetFileName) {
        return "file:///android_asset/" + assetFileName;
    }

    /**
     * 获取raw路径
     *
     * @param context
     * @param rawId
     * @return
     */
    public String getRawUrl(Context context, int rawId) {
        return "android.resource://" + context.getPackageName() + "/raw/" + rawId;
    }

    /**
     * 获取drawable路径
     *
     * @param context
     * @param drawableId
     * @return
     */
    public String getDrawableUrl(Context context, int drawableId) {
        return "android.resource://" + context.getPackageName() + "/drawable/" + drawableId;
    }
}
