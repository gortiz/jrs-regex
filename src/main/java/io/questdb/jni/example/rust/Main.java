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

package io.questdb.jni.example.rust;

import io.questdb.jar.jni.JarJniLoader;

public class Main {
    static {
        JarJniLoader.loadLib(
                Main.class,

                // A platform-specific path is automatically suffixed to path below.
                "/io/questdb/jni/example/rust/libs",

                // The "lib" prefix and ".so|.dynlib|.dll" suffix are added automatically as needed.
                "str_reverse");
    }

    public static native String reversedString(String str);

    public static native boolean isMatch(String pattern, String haystack);

    public static void main(String[] args) {
        System.out.println(reversedString("Hello World!"));
    }
}
