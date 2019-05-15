#include <jni.h>
#include <android/log.h>
#include <string>
#include <string.h>

#define LOGI(...)  __android_log_print(ANDROID_LOG_WARN,"http://www.mmednet.com",__VA_ARGS__);

#define KEY_AES "fb0ba80fad895664"
#define KEY_DES "f25e6046"
#define KEY_DESede "f94832a5cd428328fb4b06a5"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_mmednet_library_http_parse_HttpHeader_format(JNIEnv *env, jclass, jstring res,
                                                      jstring method_type) {
    LOGI("%s", "copyright");
    jstring result(res);

    const char *name = "decode";
    const char *method_char = env->GetStringUTFChars(method_type, JNI_FALSE);
    const char *signature = "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;";

    jclass encrypt_clazz = env->FindClass("com/mmednet/library/http/code/Encrypt");
    jmethodID decode_id = env->GetStaticMethodID(encrypt_clazz, name, signature);


    //AES
    if (strcmp(method_char, "1") == 0) {
        jstring key = env->NewStringUTF(KEY_AES);
        jstring method = env->NewStringUTF("AES");
        result = (jstring) env->CallStaticObjectMethod(
                encrypt_clazz, decode_id, res, key, method);
        env->DeleteLocalRef(key);
        env->DeleteLocalRef(method);
    }

    //DES
    if (strcmp(method_char, "2") == 0) {
        jstring key = env->NewStringUTF(KEY_DES);
        jstring method = env->NewStringUTF("DES");
        result = (jstring) env->CallStaticObjectMethod(
                encrypt_clazz, decode_id, res, key, method);
        env->DeleteLocalRef(key);
        env->DeleteLocalRef(method);
    }

    //DESede
    if (strcmp(method_char, "3") == 0) {
        jstring key = env->NewStringUTF(KEY_DESede);
        jstring method = env->NewStringUTF("DESede");
        result = (jstring) env->CallStaticObjectMethod(
                encrypt_clazz, decode_id, res, key, method);
        env->DeleteLocalRef(key);
        env->DeleteLocalRef(method);
    }

    env->DeleteLocalRef(res);
    env->ReleaseStringUTFChars(method_type, method_char);
    env->DeleteLocalRef(method_type);
    env->DeleteLocalRef(encrypt_clazz);

    return result;
}
