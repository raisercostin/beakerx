/*
 *  Copyright 2017 TWO SIGMA OPEN SOURCE, LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.twosigma.beakerx.kernel;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.TimeZone;
import java.util.UUID;

public class Utils {

  /**
   * The timezone to use when generating time stamps.
   */
  private static final TimeZone UTC = TimeZone.getTimeZone("UTC");
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
  private static final DateTimeFormatter df = DateTimeFormatter.ofPattern(DATE_FORMAT);
  public static final String EMPTY_STRING = "";

  private static UUIDStrategy UUID_STRATEGY_DEFAULT = () -> UUID.randomUUID().toString();

  private static UUIDStrategy commUUIDStrategy = UUID_STRATEGY_DEFAULT;

  public static String timestamp() {
    ZonedDateTime now = ZonedDateTime.now(ZoneId.of(UTC.getID()));
    return df.format(now);
  }

  public static String uuid() {
    return UUID.randomUUID().toString();
  }

  public static String commUUID() {
    return commUUIDStrategy.get();
  }

  public static String getUsString(String[] input) {
    StringBuilder ret = new StringBuilder();
    if (input != null && input.length > 0) {
      for (String s : input) {
        ret.append(s + "\n");
      }
    }
    return ret.toString();
  }

  public static String getAsString(Collection<String> input) {
    if (input == null || input.isEmpty()) {
      return EMPTY_STRING;
    }
    return getUsString(input.toArray(new String[input.size()]));
  }

  private static int fixedUUIDCounter;

  public static synchronized void setFixedCommUUID(String uuid) {
    fixedUUIDCounter = 0;
    commUUIDStrategy = () -> {
      fixedUUIDCounter++;
      return uuid + fixedUUIDCounter;
    };
  }

  public static synchronized void setDefaultCommUUID() {
    commUUIDStrategy = UUID_STRATEGY_DEFAULT;
  }

  interface UUIDStrategy {
    String get();
  }


}