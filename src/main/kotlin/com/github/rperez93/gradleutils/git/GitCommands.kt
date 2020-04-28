/*
 * Copyright 2019 Rafael Pérez
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

package com.github.rperez93.gradleutils.git


import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.lib.Repository
import java.io.File
import java.lang.RuntimeException

@Suppress("MemberVisibilityCanBePrivate")
class GitCommands {

    private val _repository: Repository by lazy {
        val ceilDir = File(System.getProperty("user.dir"))
                .parentFile.parentFile

        FileRepositoryBuilder().apply {
            findGitDir()
            addCeilingDirectory(ceilDir)
            isMustExist = true
        }.build()
    }

    fun obtainLastTag(): String? =
        _repository
            .refDatabase
            .getRefsByPrefix(Constants.R_TAGS)
            .firstOrNull()?.name

    companion object {
        fun String.extractVersion(): String {
            val lastSlashIndex = lastIndexOf("/")
            val substr = if (lastSlashIndex > -1) substring(lastSlashIndex + 1) else this
            return if (substr.startsWith("v")) {
                substr.substring(1)
            } else {
                throw RuntimeException("The version tag should start with a \"v\"")
            }
        }
    }
}