package io.github.gdrfgdrf.cutetranslationapi.text

import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.MutableText
import net.minecraft.text.Text

class CuteTranslation private constructor() {
    private val list = ArrayList<CuteText>()
    private var built: MutableText? = null

    fun size(): Int {
        return list.size
    }

    fun get(index: Int): CuteText {
        return list[index]
    }

    fun append(cuteText: CuteText): CuteTranslation {
        list.add(cuteText)
        return this
    }

    fun appendAll(cuteTexts: Array<out CuteText>): CuteTranslation {
        cuteTexts.forEach(this::append)
        return this
    }

    fun append(raw: String, formatSymbol: String = "&"): CuteTranslation {
        return append(CuteText.of(raw, formatSymbol))
    }

    fun appendAll(raws: Array<out String>, formatSymbol: String = "&"): CuteTranslation {
        raws.forEach {
            this.append(it, formatSymbol)
        }
        return this
    }

    fun insert(index: Int, cuteText: CuteText): CuteTranslation {
        list.add(index, cuteText)
        return this
    }

    fun insert(index: Int, raw: String, formatSymbol: String = "&"): CuteTranslation {
        return insert(index, CuteText.of(raw, formatSymbol))
    }

    fun replace(index: Int, cuteText: CuteText): CuteTranslation {
        list[index] = cuteText
        return this
    }

    fun replace(index: Int, raw: String, formatSymbol: String = "&"): CuteTranslation {
        return replace(index, CuteText.of(raw, formatSymbol))
    }

    fun build(): Text {
        if (this.built != null) {
            return this.built!!
        }

        this.built = Text.of("") as MutableText
        if (list.isEmpty()) {
            return this.built!!
        }

        list.forEach { cuteText ->
            this.built!!.append(cuteText.build())
        }
        return this.built!!
    }

    fun print(logger: (Text) -> Unit) {
        logger(build())
    }

    fun send(serverPlayerEntity: ServerPlayerEntity) {
        val text = build()
        serverPlayerEntity.sendMessage(text, false)
    }

    companion object {
        fun create(): CuteTranslation = CuteTranslation()

        fun of(raw: String, formatSymbol: String = "&"): CuteTranslation {
            val cuteText = CuteText.of(raw, formatSymbol)
            return create().append(cuteText)
        }

        fun build(vararg cuteTexts: CuteText): CuteTranslation {
            val cuteTranslation = create()
            cuteTranslation.appendAll(cuteTexts)
            return cuteTranslation
        }

        fun build(vararg raws: String): CuteTranslation {
            val cuteTranslation = create()
            cuteTranslation.appendAll(raws)
            return cuteTranslation
        }
    }
}