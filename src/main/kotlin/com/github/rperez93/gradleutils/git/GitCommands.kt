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

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

@Suppress("MemberVisibilityCanBePrivate")
class GitCommands {

    private val _runtime = Runtime.getRuntime()

    var error: String? = null

    fun runCommand(command: String): String? {
        val process = _runtime.exec(command)
        val error =
            readFromInputStream(process.errorStream)
        if (error != null && error.isNotEmpty()) {
            this.error = error
            return null
        }
        return readFromInputStream(process.inputStream)
    }

    fun obtainLastTagId() = runCommand(COMMAND_GIT_OBTAIN_LAST_TAG_ID)
    fun getTagsInOrigin() = runCommand(COMMAND_GIT_GET_TAGS_IN_ORIGIN)
    fun getTagDescription(tagId: String) = runCommand("$COMMAND_GIT_OBTAIN_TAG_DESCRIPTION $tagId")

    companion object {
        private const val COMMAND_GIT_OBTAIN_LAST_TAG_ID = "git rev-list --tags --max-count=1"
        private const val COMMAND_GIT_GET_TAGS_IN_ORIGIN = "git ls-remote --tags origin"
        private const val COMMAND_GIT_OBTAIN_TAG_DESCRIPTION = "git describe --tags "

        private fun readFromInputStream(inputStream: InputStream): String? {
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var data: String? = null

            with(bufferedReader) {
                var line: String?
                do {
                    line = readLine()
                    if (line != null) {
                        data = if (data == null) {
                            line
                        } else {
                            data + "\n" + line
                        }
                    }
                } while (line != null)
                close()
            }
            return data
        }
    }

}