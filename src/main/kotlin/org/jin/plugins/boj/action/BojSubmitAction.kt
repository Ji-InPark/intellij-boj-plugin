package org.jin.plugins.boj.action

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.ui.Messages
import java.util.regex.Pattern

class BojSubmitAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val file = event.getData(PlatformDataKeys.VIRTUAL_FILE)
        val fileName = file?.name.toString()

        val pattern = Pattern.compile("(\\d+)")
        val matcher = pattern.matcher(fileName)

        if (!matcher.find()) {
            Messages.showInfoMessage("파일 명에 백준 문제 번호를 포함해서 만들어주세요.", "Error")
            return
        }

        val code = file?.contentsToByteArray()?.toString(Charsets.UTF_8)
        if (code.isNullOrEmpty()) {
            Messages.showInfoMessage("코드가 비어있습니다.", "Error")
            return
        }

        // copy code to clipboard
        val clipboard = java.awt.Toolkit.getDefaultToolkit().systemClipboard
        val selection = java.awt.datatransfer.StringSelection(code)
        clipboard.setContents(selection, selection)

        val bojId = matcher.group(1)
        BrowserUtil.open("https://www.acmicpc.net/submit/$bojId")
    }
}