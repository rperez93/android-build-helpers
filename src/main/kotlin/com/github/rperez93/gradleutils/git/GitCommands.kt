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


import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.gradle.api.Project
import java.io.File

@Suppress("MemberVisibilityCanBePrivate")
class GitCommands (private val _project: Project){

    private val _repository: Repository by lazy {
        val ceilDir = File(System.getProperty("user.dir"))
            .parentFile.parentFile

        FileRepositoryBuilder().apply {
            findGitDir(_project.projectDir)
            addCeilingDirectory(ceilDir)
            isMustExist = true
        }.build()
    }

    fun obtainLastTag(): String? {

        println(_repository.directory)

        val revWalk = RevWalk(_repository)

        return Git(_repository).tagList().call().apply {
            sortWith { ref1, ref2 ->

                val ref1Date = try {
                    revWalk.parseTag(ref1.objectId).taggerIdent.`when`
                } catch (e: Exception) {
                    revWalk.parseCommit(ref1.objectId).authorIdent.`when`
                }

                val ref2Date = try {
                    revWalk.parseTag(ref2.objectId).taggerIdent.`when`
                } catch (e: Exception) {
                    revWalk.parseCommit(ref2.objectId).authorIdent.`when`
                }

                ref1Date.compareTo(ref2Date)
            }
        }.lastOrNull()?.name
    }

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