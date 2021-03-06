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
package com.novoda.imageloader.core.file;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.novoda.imageloader.core.Util;
import com.novoda.imageloader.core.file.util.FileUtil;

public class FileUtilTest {
	
	private FileUtil fileUtil = new FileUtil();
	private File cacheDir;
	
	@Before 
	public void before() {
		cacheDir = new File(Util.FOLDER_FOR_TEST_TMP_FILES);
		cacheDir.mkdirs();
	}
	
	@After 
	public void after() throws IOException {
		FileUtils.deleteDirectory(cacheDir);
	} 
	
	@Test 
	public void shouldPrepareTheCacheDir() {
		
	}
	
	@Test 
	public void shouldDeleteAllFiles() throws IOException {
		File f1 = createFile("1");
		File f2 = createFile("2");
		
		assertTrue(fileUtil.deleteFileCache(cacheDir.getAbsolutePath()));

		assertFalse(f1.exists());
		assertFalse(f2.exists());
	}
	
	@Test 
	public void shouldReduceFiles() throws IOException {
		File f1 = createFile("1", 10000);
		File f2 = createFile("2");
		File f3 = createFile("3", 105000);
		File f4 = createFile("4", System.currentTimeMillis());
		
		assertTrue(fileUtil.reduceFileCache(cacheDir.getAbsolutePath(), 24*3600*1000));

		assertFalse(f1.exists());
		assertTrue(f2.exists());
		assertFalse(f3.exists());
		assertTrue(f4.exists());
	}
	
	private File createFile(String name) {
		return createFile(name, -1);
	}
	
	private File createFile(String name, long lastModified) {
		try {
			File f1 = new File(cacheDir, name + ".tmp");
			FileUtils.write(f1, name);
			if(lastModified != -1) { 
				f1.setLastModified(lastModified);		
			}
			return f1;
		} catch (Exception e) {
			Assert.fail("Can't crete file for the test" + e.getMessage());
			throw new RuntimeException("Can't crete file for the test " + e.getMessage());
		}
	}

}
