package io.github.gdrfgdrf.cutetranslationapi.text

import net.minecraft.text.*
import net.minecraft.text.ClickEvent.Action
import net.minecraft.text.HoverEvent.EntityContent
import net.minecraft.text.HoverEvent.ItemStackContent

class CuteText private constructor(raw: String, private val formatSymbol: String = "&") {
    private var finalString = raw

    private var clickAction: Action? = null
    private var clickActionValue: String? = null

    private var hoverActionValue: Any? = null

    private var style: Style? = null
    private var text: MutableText? = null

    fun clickAction(action: Action): CuteText {
        check()
        this.clickAction = action
        return this
    }

    fun openUrl(value: String): CuteText {
        check()
        clickAction(Action.OPEN_URL)
        clickActionValue(value)
        return this
    }

    fun openFile(value: String): CuteText {
        check()
        clickAction(Action.OPEN_FILE)
        clickActionValue(value)
        return this
    }

    fun runCommand(value: String): CuteText {
        check()
        clickAction(Action.RUN_COMMAND)
        clickActionValue(value)
        return this
    }

    fun suggestCommand(value: String): CuteText {
        check()
        clickAction(Action.SUGGEST_COMMAND)
        clickActionValue(value)
        return this
    }

    fun changePage(value: String): CuteText {
        check()
        clickAction(Action.CHANGE_PAGE)
        clickActionValue(value)
        return this
    }

    fun clickActionValue(value: String): CuteText {
        check()
        this.clickActionValue = value
        return this
    }

    fun showText(value: String): CuteText {
        return showText(Text.of(value))
    }

    fun showText(value: Text): CuteText {
        check()
        hoverActionValue(value)
        return this
    }

    fun showItem(value: ItemStackContent): CuteText {
        check()
        hoverActionValue(value)
        return this
    }

    fun showEntity(value: EntityContent): CuteText {
        check()
        hoverActionValue(value)
        return this
    }

    private fun hoverActionValue(value: Any): CuteText {
        check()
        this.hoverActionValue = value
        return this
    }

    fun format(vararg any: Any): CuteText {
        check()
        finalString = finalString.format(any)
        return this
    }

    private fun check() {
        if (text != null && style != null) {
            throw IllegalStateException("This cute text has been built, no further changes can be made")
        }
    }

    private fun buildStyle(): Style {
        if (this.style != null) {
            return this.style!!
        }

        this.style = Style.EMPTY

        val clickEvent = if (clickAction != null && clickActionValue != null) {
            ClickEvent(clickAction, clickActionValue)
        } else {
            null
        }
        clickEvent?.let {
            this.style = this.style!!.withClickEvent(it)
        }

        var hoverEvent: HoverEvent? = null
        if (hoverActionValue != null) {
            if (hoverActionValue is Text) {
                hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverActionValue as Text)
            }
            if (hoverActionValue is ItemStackContent) {
                hoverEvent = HoverEvent(HoverEvent.Action.SHOW_ITEM, hoverActionValue as ItemStackContent)
            }
            if (hoverActionValue is EntityContent) {
                hoverEvent = HoverEvent(HoverEvent.Action.SHOW_ENTITY, hoverActionValue as EntityContent)
            }
        }
        hoverEvent?.let {
            this.style = this.style!!.withHoverEvent(it)
        }

        return this.style!!
    }

    fun build(): Text {
        if (this.text != null) {
            return this.text!!
        }
        finalString = replaceFormatSymbol(formatSymbol, finalString)
        this.text = Text.of(finalString) as MutableText
        val buildStyle = buildStyle()
        this.text!!.styled {
            buildStyle
        }

        return this.text!!
    }

    companion object {
        fun of(raw: String, formatSymbol: String = "&"): CuteText = CuteText(raw, formatSymbol)

        fun replaceFormatSymbol(formatSymbol: String, string: String): String {
            return string
                .replace(formatSymbol + "0", "§0")
                .replace(formatSymbol + "1", "§1")
                .replace(formatSymbol + "2", "§2")
                .replace(formatSymbol + "3", "§3")
                .replace(formatSymbol + "4", "§4")
                .replace(formatSymbol + "5", "§5")
                .replace(formatSymbol + "6", "§6")
                .replace(formatSymbol + "7", "§7")
                .replace(formatSymbol + "8", "§8")
                .replace(formatSymbol + "9", "§9")
                .replace(formatSymbol + "a", "§a")
                .replace(formatSymbol + "b", "§b")
                .replace(formatSymbol + "c", "§c")
                .replace(formatSymbol + "d", "§d")
                .replace(formatSymbol + "e", "§e")
                .replace(formatSymbol + "f", "§f")
                .replace(formatSymbol + "k", "§k")
                .replace(formatSymbol + "l", "§l")
                .replace(formatSymbol + "m", "§m")
                .replace(formatSymbol + "n", "§n")
                .replace(formatSymbol + "o", "§o")
                .replace(formatSymbol + "r", "§r")
        }
    }
}