package com.tomtom.nk2.tools.buildsupport.ktlintcustomrules

import com.pinterest.ktlint.core.KtLint
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.isPartOf
import com.pinterest.ktlint.core.ast.isRoot
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.PsiComment
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement

// This file is based on a copy of https://github.com/pinterest/ktlint/blob/master/ktlint-ruleset-standard/src/main/kotlin/com/pinterest/ktlint/ruleset/standard/MaxLineLengthRule.kt
// we've the following version: https://github.com/pinterest/ktlint/tree/b35abdc05e9395485dad3c45cfd9aaaf457391b9
// The changes that we've made are marked with comments, but we've basically reverted the exclusion
// of checking on comment lines, except when they are explicitly disabled or contain a hyperlink.
class MaxLineLengthOnCommentsRule : Rule("max-line-length-on-comment"), Rule.Modifier.Last {

    private var maxLineLength: Int = -1
    private var rangeTree = RangeTree()

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        if (node.isRoot()) {
            val editorConfig = node.getUserData(KtLint.EDITOR_CONFIG_USER_DATA_KEY)!!
            maxLineLength = editorConfig.maxLineLength
            if (maxLineLength <= 0) {
                return
            }
            val errorOffset = arrayListOf<Int>()
            val text = node.text
            val lines = text.split("\n")
            var offset = 0
            for (line in lines) {
                if (line.length > maxLineLength) {
                    val el = node.psi.findElementAt(offset + line.length - 1)!!.node
                        // Start of modifications for comment length check.
                        if (el.isPartOf(PsiComment::class)) {
                            // Allow ktlint-disable comments to exceed max line length.
                            if (!el.text.startsWith("// ktlint-disable")) {
                                // Comments containing a hyperlink can exceed the limit as they
                                // can't be broken into multiple lines.
                                if (!line.contains("https?://".toRegex())) {
                                    errorOffset.add(offset)
                                }
                            }
                        }
                    // End of modifications for comment length check.
                    }
                offset += line.length + 1
            }
            rangeTree = RangeTree(errorOffset)
        } else if (!rangeTree.isEmpty() && node.psi is LeafPsiElement) {
            rangeTree
                .query(node.startOffset, node.startOffset + node.textLength)
                .forEach { offset ->
                    emit(offset, "Exceeded max line length ($maxLineLength)", false)
                }
        }
    }
}

class RangeTree(seq: List<Int> = emptyList()) {

    private var emptyArrayView = ArrayView(0, 0)
    private var arr: IntArray = seq.toIntArray()

    init {
        if (arr.isNotEmpty()) {
            arr.reduce { p, n -> require(p <= n) { "Input must be sorted" }; n }
        }
    }

    // runtime: O(log(n)+k), where k is number of matching points.
    // space: O(1).
    fun query(vmin: Int, vmax: Int): RangeTree.ArrayView {
        var r = arr.size - 1
        if (r == -1 || vmax < arr[0] || arr[r] < vmin) {
            return emptyArrayView
        }
        // binary search for min(arr[l] >= vmin).
        var l = 0
        while (l < r) {
            val m = (r + l) / 2
            if (vmax < arr[m]) {
                r = m - 1
            } else if (arr[m] < vmin) {
                l = m + 1
            } else {
                // arr[l] ?<=? vmin <= arr[m] <= vmax ?<=? arr[r].
                if (vmin <= arr[l]) break else l++ // optimization.
                r = m
            }
        }
        if (l > r || arr[l] < vmin) {
            return emptyArrayView
        }
        // find max(k) such as arr[k] < vmax.
        var k = l
        while (k < arr.size) {
            if (arr[k] >= vmax) {
                break
            }
            k++
        }
        return ArrayView(l, k)
    }

    fun isEmpty() = arr.isEmpty()

    inner class ArrayView(private var l: Int, private val r: Int) {

        val size: Int = r - l

        fun get(i: Int): Int {
            if (i < 0 || i >= size) {
                throw IndexOutOfBoundsException()
            }
            return arr[l + i]
        }

        inline fun forEach(cb: (v: Int) -> Unit) {
            var i = 0
            while (i < size) {
                cb(get(i++))
            }
        }

        override fun toString(): String {
            if (l == r) {
                return "[]"
            }
            val sb = StringBuilder("[")
            var i = l
            while (i < r) {
                sb.append(arr[i]).append(", ")
                i++
            }
            sb.replace(sb.length - 2, sb.length, "")
            sb.append("]")
            return sb.toString()
        }
    }
}
