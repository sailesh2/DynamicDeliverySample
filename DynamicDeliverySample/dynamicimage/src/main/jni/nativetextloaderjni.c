#include <jni.h>

JNIEXPORT jstring JNICALL
Java_su_dynamicimage_NativeTextLoaderJNI_getText(JNIEnv *env, jobject instance) {


    char* text = "HELLO FROM NATIVE C";


    return (*env)->NewStringUTF(env, text);
}