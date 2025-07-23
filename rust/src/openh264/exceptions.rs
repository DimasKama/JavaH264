use jni::JNIEnv;

pub fn throw_runtime_exception<T: AsRef<str>>(env: &mut JNIEnv, message: T) {
    let _ = env.throw(("java/lang/RuntimeException", message));
}

pub fn throw_encoder_exception<T: AsRef<str>>(env: &mut JNIEnv, message: T) {
    let _ = env.throw(("ru/dimaskama/javah264/exception/EncoderException", message));
}

pub fn throw_illegal_argument_exception<T: AsRef<str>>(env: &mut JNIEnv, message: T) {
    let _ = env.throw(("java/lang/IllegalArgumentException", message));
}
