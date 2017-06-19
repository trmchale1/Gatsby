#include <jni.h>
#include <android/log.h>
#include <iostream>

extern "C"

JNIEXPORT void JNICALL
Java_com_example_trmchale_gatsby005_PhotoProcessing_test(
        JNIEnv *env,
        jobject /* this */) {
        int x = 0;
    __android_log_print(ANDROID_LOG_VERBOSE, "MyApp", "%i", x);
}
