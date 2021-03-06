/**
 * Copyright 2012 Novoda Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.novoda.imageloader.core.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.novoda.imageloader.core.file.util.FileUtil;

public class CacheCleaner extends IntentService {

    public static final String CLEAN_CACHE_ACTION = "com.novoda.imageloader.core.action.CLEAN_CACHE";
    public static final String EXPIRATION_PERIOD_EXTRA = "com.novoda.imageloader.core.extra.EXPIRATION_PERIOD";
    public static final String CACHE_DIR_EXTRA = "com.novoda.imageloader.core.extra.CACHE_DIR";

    public static final Intent getCleanCacheIntent(String directoryFullPath, long expirationPeriod) {
        Intent i = new Intent(CLEAN_CACHE_ACTION);
        i.putExtra(EXPIRATION_PERIOD_EXTRA, expirationPeriod);
        i.putExtra(CACHE_DIR_EXTRA, directoryFullPath);
        return i;
    }

    public CacheCleaner() {
        this("CacheCleaner");
    }

    public CacheCleaner(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        new CacheCleanerUtil().onReceive(this, intent);
    }

    /* package */static class CacheCleanerUtil {

        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            if (!intent.hasExtra(CACHE_DIR_EXTRA)) {
                return;
            }
            String cacheDir = intent.getStringExtra(CACHE_DIR_EXTRA);
            if (cacheDir == null || cacheDir.length() == 0) {
                return;
            }
            if (CLEAN_CACHE_ACTION.equals(action)) {
                long exipiredPeriod = intent.getLongExtra(EXPIRATION_PERIOD_EXTRA, -1);
                try {
                    new FileUtil().reduceFileCache(cacheDir, exipiredPeriod);
                } catch (Throwable t) {
                    // Don't have to fail in case there
                }
            }
        }
    }

}
