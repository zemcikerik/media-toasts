#include "jni_interop.h"
#include "media_handler.hpp"

static void throw_jni_exception(JNIEnv* env, const std::string& name);

media_handler::windows::windows_media_handler* windows_media_handler;
jobject java_handler;

JNIEXPORT void JNICALL Java_dev_zemco_mediatoasts_handlers_WindowsMediaHandler_initialize(JNIEnv* env, const jobject handler)
{
    if (windows_media_handler != nullptr)
    {
        throw_jni_exception(env, "AlreadyInitializedException");
        return;
    }

    winrt::init_apartment();

    JavaVM* jvm;
    env->GetJavaVM(&jvm);

    const auto handler_class = env->GetObjectClass(handler);
    const auto on_media_callback_method = env->GetMethodID(handler_class, "onMediaInfoNativeCallback", "(Ljava/lang/String;Ljava/lang/String;)V");

    java_handler = env->NewGlobalRef(handler);
    windows_media_handler = new media_handler::windows::windows_media_handler([=](const media_info& info)
    {
        JNIEnv* callback_env;
        const auto callback_env_ref = reinterpret_cast<void**>(&callback_env);

        const auto attachment_state = jvm->GetEnv(callback_env_ref, JNI_VERSION_10);

        if (attachment_state == JNI_EDETACHED)
            jvm->AttachCurrentThread(callback_env_ref, nullptr);

        callback_env->PushLocalFrame(4);

        const auto artist = callback_env->NewStringUTF(info.artist.c_str());
        const auto title = callback_env->NewStringUTF(info.title.c_str());

        callback_env->CallVoidMethod(java_handler, on_media_callback_method, artist, title);
        callback_env->PopLocalFrame(nullptr);

        if (attachment_state == JNI_EDETACHED)
            jvm->DetachCurrentThread();
    });
}


JNIEXPORT void JNICALL Java_dev_zemco_mediatoasts_handlers_WindowsMediaHandler_dispose(JNIEnv* env, jobject)
{
    if (windows_media_handler == nullptr)
    {
        throw_jni_exception(env, "NotInitializedException");
        return;
    }

    delete windows_media_handler;
    windows_media_handler = nullptr;
    java_handler = {};
}


static void throw_jni_exception(JNIEnv* env, const std::string& name)
{
    const std::string fqn = "dev/zemco/mediatoasts/exceptions/" + name;

    const auto exception_class = env->FindClass(fqn.c_str());
    const auto exception_constructor = env->GetMethodID(exception_class, "<init>", "()V");
    const auto exception = env->NewObject(exception_class, exception_constructor);

    env->Throw(reinterpret_cast<jthrowable>(exception));
}
