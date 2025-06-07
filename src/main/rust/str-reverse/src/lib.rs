/*******************************************************************************
 *     ___                  _   ____  ____
 *    / _ \ _   _  ___  ___| |_|  _ \| __ )
 *   | | | | | | |/ _ \/ __| __| | | |  _ \
 *   | |_| | |_| |  __/\__ \ |_| |_| | |_) |
 *    \__\_\\__,_|\___||___/\__|____/|____/
 *
 *  Copyright (c) 2014-2019 Appsicle
 *  Copyright (c) 2019-2023 QuestDB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/
use std::ptr::null_mut;
use jni::JNIEnv;
use jni::objects::{JClass, JString, JValue, JValueGen};
use jni::sys::{jboolean, jint, jlong, jobject, jstring};
use regex::{Match, Regex};

#[no_mangle]
pub extern "system" fn Java_io_questdb_jni_example_rust_Main_reversedString(
    mut env: JNIEnv,
    _class: JClass,
    input: JString) -> jstring {
    let input: String =
        env.get_string(&input).expect("Couldn't get java string!").into();
    let reversed: String = input.chars().rev().collect();
    let reversed = format!("{}: {}", std::env!("REVERSED_STR_PREFIX"), reversed);
    let output = env.new_string(reversed)
        .expect("Couldn't create java string!");
    output.into_raw()
}

#[no_mangle]
pub extern "system" fn Java_io_questdb_jni_example_rust_Main_isMatch(
    mut _env: JNIEnv,
    _class: JClass,
    pattern: JString,
    haystack: JString) -> jboolean {
    // TODO: deal with errors
    let rs_regex: String = _env.get_string(&pattern)
        .expect("Couldn't get java string for regex!").into();
    let regex = Regex::new(&rs_regex).expect("Invalid regex pattern");

    let rs_haystack: String = _env.get_string(&haystack)
        .expect("Couldn't get java string for pattern!").into();

    regex.is_match(&rs_haystack).into()
}

// Implements static native long createNative(String pattern);
#[no_mangle]
pub extern "system" fn Java_io_questdb_jni_example_rust_JniRsRegex_createNative(
    mut _env: JNIEnv,
    _class: JClass,
    pattern: JString) -> jlong {

    // TODO: deal with errors
    let regex_str: String = _env.get_string(&pattern)
        .expect("Couldn't get java string for regex!").into();

    Box::into_raw(Box::new(Regex::new(&regex_str)
        .expect("Invalid regex pattern"))) as jlong
}

// Implements static native void closeNative(long rsRegexPtr)
#[no_mangle]
pub extern "system" fn Java_io_questdb_jni_example_rust_JniRsRegex_closeNative(
    _env: JNIEnv,
    _class: JClass,
    rs_regex_ptr: jlong) {

    // Convert the pointer back to a Box and drop it
    if rs_regex_ptr != 0 {
        unsafe {
            let _ = Box::from_raw(rs_regex_ptr as *mut Regex);
        }
    }
}

// Implements static native int findNative(long rsRegexPtr, String haystack, int start);
#[no_mangle]
pub extern "system" fn Java_io_questdb_jni_example_rust_JniRsRegex_findNative(
    mut _env: JNIEnv,
    _class: JClass,
    rs_regex_ptr: jlong,
    haystack: JString,
    start: i32) -> jobject {

    // Convert the pointer back to a Box
    let regex = unsafe {
        &*(rs_regex_ptr as *const Regex)
    };

    // Get the haystack string
    let haystack_str: String = _env.get_string(&haystack)
        .expect("Couldn't get java string for haystack!").into();

    // Perform the search
    match regex.find(&haystack_str[start as usize..]) {
        Some(m) => {
            let class = _env.find_class("io/questdb/jni/example/rust/RsRegex$Match")
                .expect("Couldn't find RsRegex$Match");

            _env.new_object(
                class,
                "(II)V",
                &[JValue::from(m.start() as jint + start), JValue::from(m.end() as jint + start)]
            ).expect("Couldn't create MatchResult object!").into_raw()
        }
        None => null_mut()
    }
}

// implements static native int isMatchNative(long rsRegexPtr, String haystack, int start);
#[no_mangle]
pub extern "system" fn Java_io_questdb_jni_example_rust_JniRsRegex_isMatchNative(
    mut _env: JNIEnv,
    _class: JClass,
    rs_regex_ptr: jlong,
    haystack: JString) -> jboolean {

    // Convert the pointer back to a Box
    let regex = unsafe {
        &*(rs_regex_ptr as *const Regex)
    };
    // Get the haystack string
    let haystack_str: String = _env.get_string(&haystack)
        .expect("Couldn't get java string for haystack!").into();
    // Check if the regex matches the haystack
    regex.is_match(&haystack_str).into()
}

#[cfg(test)]
mod tests {
    #[test]
    fn test_rubber_duck() {
        assert_ne!("rubber", "duck");
    }
}