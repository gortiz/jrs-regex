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

import io.questdb.jni.example.rust.Main;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class LibTest {

  @Test
  public void testLibrary() {
    assertEquals("Great Scott, A reversed string!: !dlroW olleH", Main.reversedString("Hello World!"));
  }

  @Test
  public void testIsMatch() {
    assertTrue(Main.isMatch("Hello", "Hello World!"));
    assertFalse(Main.isMatch("Goodbye", "Hello World!"));
    assertTrue(Main.isMatch("World", "Hello World!"));
    assertFalse(Main.isMatch("world", "Hello World!")); // case-sensitive
  }

  @Test
  public void testIsMatchObj() {
    RsRegex.Factory factory = new JniRsRegex.Factory();
    try (RsRegex regex = factory.create("Hello")) {
      assertTrue(regex.isMatch("Hello World!"));
      assertFalse(regex.isMatch("Goodbye World!"));
    }
  }

  @Test
  public void testFind() {
    RsRegex.Factory factory = new JniRsRegex.Factory();
    try (RsRegex regex = factory.create("World")) {
      assertEquals(new RsRegex.Match(6, 11), regex.find("Hello World!"));
      assertEquals(new RsRegex.Match(6, 11), regex.find("Hello World!", 0));
      assertEquals(new RsRegex.Match(6, 11), regex.find("Hello World!", 5));
      assertEquals(new RsRegex.Match(6, 11), regex.find("Hello World!", 6));
      assertNull(regex.find("Hello World!", 7));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindNegativeStart() {
    RsRegex.Factory factory = new JniRsRegex.Factory();
    try (RsRegex regex = factory.create("World")) {
      regex.find("Hello World!", -1);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindStartGreaterThanHaystack() {
    RsRegex.Factory factory = new JniRsRegex.Factory();
    try (RsRegex regex = factory.create("World")) {
      regex.find("Hello World!", 20);
    }
  }
}
